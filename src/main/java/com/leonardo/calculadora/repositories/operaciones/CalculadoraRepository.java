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


    public BigDecimal calcularAcumulado(String sessionId, Operador op, int bloque) {
        int puntero;
        int ronda = 1;

        List<byte[]> memoriaPura = redisService.lRangeRedis(sessionId, 0, bloque);
        BigDecimal resultado = null;

        do {
            BigDecimal resultadoParcial = reducer(op, memoriaPura, (ronda - 1) * bloque + memoriaPura.size());
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


    private BigDecimal reducer(Operador op, List<byte[]> memoriaPura, int maximasPosiciones) {
        int memoriaPuraSize = memoriaPura.size();
        if (memoriaPura.size() == 0) {
            return op.indentidad();
        }

        BigDecimal resultado = toDouble(memoriaPura.get(0));
        for (int index = 1; index < memoriaPuraSize; index++) {
            try {
                resultado = op.operar(resultado, toDouble(memoriaPura.get(index)));
            } catch (Exception err) {
                throw new ArithmeticException("Error con el valor " + new String(
                        memoriaPura.get(index), StandardCharsets.UTF_8
                ) + " elemento numero: " + (maximasPosiciones - index + 1));
            }
        }

        return resultado;
    }
}
