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
public class AcumularInTest {

    @Autowired
    private AcumularIn acumularIn;
    private String sesionId;
    private BigDecimal valor;

    @BeforeEach
    public void setup() {
        this.valor = BigDecimal.valueOf(1000 * Math.random());
        this.sesionId = UUID.randomUUID().toString();
        acumularIn = new AcumularIn();
    }

    @Test
    public void esInValidoAll() {
        assertTrue("esInvalido", acumularIn.esInValido());
    }

    @Test
    public void esInValido() {
        acumularIn.setSesionId(UUID.randomUUID().toString());
        acumularIn.setValor(valor);
        assertEquals("esInvalido", false, acumularIn.esInValido());
    }

    @Test
    public void esInValidoSesionId() {
        acumularIn.setSesionId(sesionId);
        assertTrue("esInvalido", acumularIn.esInValido());
    }

    @Test
    public void esInValidoValor() {
        acumularIn.setValor(BigDecimal.ZERO);
        assertTrue("esInvalido", acumularIn.esInValido());
    }

    @Test
    public void shouldEqualsParams() {
        acumularIn.setSesionId(sesionId);
        acumularIn.setValor(valor);

        assertEquals("code", valor, acumularIn.getValor());
        assertEquals("data", sesionId, acumularIn.getSesionId());
    }
}