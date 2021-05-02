package com.leonardo.calculadora.logic.entradas.operaciones;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.springframework.test.util.AssertionErrors.*;

public class EliminarInTest {

    @Autowired
    private EliminarIn eliminarIn;
    private String sesionId;


    @BeforeEach
    public void setup() {
        this.sesionId = UUID.randomUUID().toString();
        eliminarIn = new EliminarIn();
    }

    @Test
    public void esInValido() {
        assertFalse("invalido", eliminarIn.esInValido());

        eliminarIn.setSesionId(sesionId);
        assertTrue("valido", eliminarIn.esInValido());
    }

    @Test
    public void shouldEqualsConstructor() {
        EliminarIn instancia = new EliminarIn(sesionId);
        assertEquals("sesionId", sesionId, instancia.getSesionId());
    }

    @Test
    public void shouldEqualsParams() {
        eliminarIn.setSesionId(sesionId);
        assertEquals("sesionId", sesionId, eliminarIn.getSesionId());
    }
}