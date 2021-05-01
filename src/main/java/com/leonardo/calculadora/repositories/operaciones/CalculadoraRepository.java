package com.leonardo.calculadora.repositories.operaciones;


import com.leonardo.calculadora.logic.operaciones.Operador;
import com.leonardo.calculadora.services.core.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Repository
public class CalculadoraRepository {

    private final RedisService redisService;
    private final long TIMEOUT = Long.parseLong(System.getenv("TIMEOUT_MEMORY"));


    @Autowired
    public CalculadoraRepository(
            RedisService redisService
    ) {
        this.redisService = redisService;
    }


    public int agregarMemoria(String sesionId, BigDecimal valor) {
        return redisService.rPushRedis(sesionId, toByteArray(valor), TIMEOUT);
    }


    public BigDecimal calcularAcumulado(String sessionId, Operador op) {
        int puntero = 0;
        int ronda = 1;
        int bloque = 20;

        List<byte[]> memoriaPura = redisService.lRangeRedis(sessionId, puntero, ronda * bloque);
        BigDecimal resultado = null;

        do {
            BigDecimal resultadoParcial = reducer(op, memoriaPura);
            if (resultado == null) {
                resultado = resultadoParcial;
            } else {
                resultado = op.operar(resultado, resultadoParcial);
            }
            puntero = ronda * bloque + 1;
            ronda++;

            memoriaPura = redisService.lRangeRedis(sessionId, puntero, ronda * bloque);
        } while (memoriaPura.size() > 0);

        // guardamos la respuesta
        redisService.delRedis(sessionId);
        redisService.rPushRedis(sessionId, toByteArray(resultado), TIMEOUT);
        return resultado;
    }


    public int limpiarMemoria(String sesionId) {
        return redisService.delRedis(sesionId);
    }


    /*==================================================================================================================
     * funciones utiles
     *=================================================================================================================*/


    public byte[] toByteArray(BigDecimal value) {
        return value.toString().getBytes(StandardCharsets.UTF_8);
    }


    public BigDecimal toDouble(byte[] bytes) {
        return new BigDecimal(new String(bytes, StandardCharsets.UTF_8));
    }


    private BigDecimal reducer(Operador op, List<byte[]> memoriaPura) {
        int memoriaPuraSize = memoriaPura.size();
        if (memoriaPura.size() == 0) {
            return op.indentidad();
        }

        BigDecimal resultado = toDouble(memoriaPura.get(0));
        for (int index = 1; index < memoriaPuraSize; index++) {
            resultado = op.operar(resultado, toDouble(memoriaPura.get(index)));
        }

        return resultado;
    }
}
