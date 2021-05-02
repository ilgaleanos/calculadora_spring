package com.leonardo.calculadora.logic.entradas.operaciones;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class OperarInTest {

    @Autowired
    private AcumularIn OperarIn;
    private String sesionId;
    private BigDecimal valor;

    @BeforeEach
    public void setup() {
        this.valor = BigDecimal.valueOf(1000 * Math.random());
        this.sesionId = UUID.randomUUID().toString();
        OperarIn = new AcumularIn();
    }

    @Test
    public void esInValidoAll() {
        assertTrue("esInvalido", OperarIn.esInValido());
    }

    @Test
    public void esInValido() {
        OperarIn.setSesionId(UUID.randomUUID().toString());
        OperarIn.setValor(valor);
        assertEquals("esInvalido", false, OperarIn.esInValido());
    }

    @Test
    public void esInValidoSesionId() {
        OperarIn.setSesionId(sesionId);
        assertTrue("esInvalido", OperarIn.esInValido());
    }

    @Test
    public void esInValidoValor() {
        OperarIn.setValor(BigDecimal.ZERO);
        assertTrue("esInvalido", OperarIn.esInValido());
    }

    @Test
    public void shouldEqualsParams() {
        OperarIn.setSesionId(sesionId);
        OperarIn.setValor(valor);

        assertEquals("code", valor, OperarIn.getValor());
        assertEquals("data", sesionId, OperarIn.getSesionId());
    }
}