package com.leonardo.calculadora.logic.operaciones;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class OperadorRestaImplTest {

    @Autowired
    private OperadorRestaImpl operadorResta;

    @Test
    public void shouldReturnSumDiferentNumbers() throws Exception {
        double aDouble = 100 * Math.random();
        double bDouble = 100 * Math.random();
        BigDecimal a = new BigDecimal(String.valueOf(aDouble));
        BigDecimal b = new BigDecimal(String.valueOf(bDouble));

        double resultadoReal = aDouble - bDouble;
        BigDecimal resultado = operadorResta.operar(a, b);

        assertTrue("Operación resta", Math.abs(resultadoReal - resultado.doubleValue()) < 1E-10);
    }
}
