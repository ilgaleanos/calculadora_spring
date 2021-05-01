package com.leonardo.calculadora.logic.operaciones;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class OperadorFabricaTest {

    @Test
    public void instanceofOperadorSumaImpl() {
        Operador op = OperadorFabrica.obtenerInstancia("+");
        assertTrue(op instanceof OperadorSumaImpl);
    }

    @Test
    public void instanceofOperadorRestaImpl() {
        Operador op = OperadorFabrica.obtenerInstancia("-");
        assertTrue(op instanceof OperadorRestaImpl);
    }

    @Test
    public void instanceofOperadorMultiplicacionImpl() {
        Operador op = OperadorFabrica.obtenerInstancia("*");
        assertTrue(op instanceof OperadorMultiplicacionImpl);
    }

    @Test
    public void instanceofOperadorDivisionImpl() {
        Operador op = OperadorFabrica.obtenerInstancia("/");
        assertTrue(op instanceof OperadorDivisionImpl);
    }

    @Test
    public void instanceofOperadorPotenciaImpl() {
        Operador op = OperadorFabrica.obtenerInstancia("^");
        assertTrue(op instanceof OperadorPotenciaImpl);
    }

    @Test
    public void instanceofOperadorErrorImpl() {
        try {
            Operador op = OperadorFabrica.obtenerInstancia("");
        } catch (InvalidParameterException err) {
            assertTrue(true);
        }
    }
}