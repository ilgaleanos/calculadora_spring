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

    /**
     * Comunicador entre el servicio del controlador y el servicio de redis
     * En particular usamos la función rPush para manejar la lista directamente en redis
     *
     * @param sesionId: cadena de indetificacion única de la sesión
     * @param valor:    valor a agregar a los operandos
     * @return bandera que nos indica si pudo o no agregar el operando
     */
    public int agregarMemoria(String sesionId, BigDecimal valor) {
        return redisService.rPushRedis(sesionId, toByteArray(valor), TIMEOUT);
    }

    /**
     * Funcion de calculo de los operandos acumulados, la idea es traer trozos de informacion de la lista
     * del tamaño bloque para ir operando profgresivamente y no saturar la memoria del api con toda la lista
     * acumulada, la operacion ya es bastante saturante en memoria y esto da un poco mas de estabilidad al sistema
     *
     * @param sesionId: cadena de indetificacion unica de la sesión
     * @param operador: implementacion de un operador
     * @param bloque:   tamaño del bash de datos de la lista para traer de redis
     * @return resultado de ejecutar la operación
     */
    public BigDecimal calcularAcumulado(String sesionId, Operador operador, int bloque) {
        int puntero;
        int ronda = 1;

        List<byte[]> memoriaPura = redisService.lRangeRedis(sesionId, 0, bloque);
        BigDecimal resultado = null;

        do {
            BigDecimal resultadoParcial = reducer(operador, memoriaPura, (ronda - 1) * bloque + memoriaPura.size());
            if (resultado == null) {
                resultado = resultadoParcial;
            } else {
                resultado = operador.operar(resultado, resultadoParcial);
            }
            puntero = ronda * bloque + 1;
            ronda++;

            memoriaPura = redisService.lRangeRedis(sesionId, puntero, ronda * bloque);
        } while (memoriaPura.size() > 0);

        // guardamos la respuesta
        redisService.delRedis(sesionId);
        redisService.rPushRedis(sesionId, toByteArray(resultado), TIMEOUT);
        return resultado;
    }

    /**
     * Eliminamos lo relacionado con una sesion
     *
     * @param sesionId: sesion sobre la cual se va a ejecutar la accion
     * @return bandera que nos indica si se pudo o no realizar la limpieza
     */
    public int limpiarMemoria(String sesionId) {
        return redisService.delRedis(sesionId);
    }


    /*==================================================================================================================
     * funciones utiles
     *=================================================================================================================*/


    /**
     * Al manejar el redis driver a nivel de bytes necesitamos conversores de Bigdecimal a byte[]
     *
     * @param value: valor a convertir
     * @return bytes de la cadena de texto que representa el numero
     */
    public byte[] toByteArray(BigDecimal value) {
        return value.toString().getBytes(StandardCharsets.UTF_8);
    }


    /**
     * Tomamos los bytes almacenados y lo transformamos en instancias operables
     *
     * @param bytes: bytes de la cadena de texto que representa el numero
     * @return el nuuero operable
     */
    public BigDecimal toDouble(byte[] bytes) {
        return new BigDecimal(new String(bytes, StandardCharsets.UTF_8));
    }

    /**
     * Funcion de reductor, esta funcion nos provee una simplificacion a la logica de suma
     * ya que al tener que operar en bashes no podemos ejecutarlo de una sola 'operada' sino lo que llevavamos
     * adicionalmente tenemos en cuenta la identidad de cada operación para poder realizar los cálculos cuando
     * llegue algun paquete vacío. Adicionalmente se agrego un manejador de error para que si esta en DEBUG se pueda
     * Infromar del error al cliente.
     *
     * @param op
     * @param memoriaPura
     * @param maximasPosiciones
     * @return
     */
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
