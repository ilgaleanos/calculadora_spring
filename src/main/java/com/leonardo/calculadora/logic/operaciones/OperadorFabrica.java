package com.leonardo.calculadora.logic.operaciones;

import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;

@Component
public class OperadorFabrica {

    public static Operador obtenerInstancia(String operacion) {
        switch (operacion) {
            case "+":
                return new OperadorSumaImpl();
            case "-":
                return new OperadorRestaImpl();
            case "*":
                return new OperadorMultiplicacionImpl();
            case "/":
                return new OperadorDivisionImpl();
            case "^":
                return new OperadorPotenciaImpl();
            default:
                throw new InvalidParameterException("Operacion: " + operacion);
        }
    }
}
