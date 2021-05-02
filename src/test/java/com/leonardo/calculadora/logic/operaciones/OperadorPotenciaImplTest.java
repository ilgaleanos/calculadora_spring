package com.leonardo.calculadora.logic.operaciones;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class OperadorPotenciaImplTest {

    @Autowired
    private OperadorPotenciaImpl operadorPotencia;

    @Test
    public void operar() throws Exception {
        double aDouble = 10 * Math.random();
        int bInt = (int) (5 * Math.random());
        BigDecimal a = new BigDecimal(String.valueOf(aDouble));
        BigDecimal b = new BigDecimal(String.valueOf(bInt));

        double resultadoReal = Math.pow(aDouble, bInt);
        BigDecimal resultado = operadorPotencia.operar(a, b);

        assertTrue("Operación potencia", Math.abs(resultadoReal - resultado.doubleValue()) < 1E-2);
    }
}
