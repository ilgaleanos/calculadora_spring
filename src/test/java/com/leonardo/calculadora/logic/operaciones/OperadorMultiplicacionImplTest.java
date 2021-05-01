package com.leonardo.calculadora.logic.operaciones;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class OperadorMultiplicacionImplTest {

    @Autowired
    private OperadorMultiplicacionImpl operadorMultiplicacion;

    @Test
    public void shouldReturnMultiplicacionDiferentNumbers() throws Exception {
        double aDouble = 100 * Math.random();
        double bDouble = 100 * Math.random();
        BigDecimal a = new BigDecimal(String.valueOf(aDouble));
        BigDecimal b = new BigDecimal(String.valueOf(bDouble));

        double resultadoReal = aDouble * bDouble;
        BigDecimal resultado = operadorMultiplicacion.operar(a, b);

        assertTrue("Operación multiplicación", Math.abs(resultadoReal - resultado.doubleValue()) < 1E-10);
    }
}
