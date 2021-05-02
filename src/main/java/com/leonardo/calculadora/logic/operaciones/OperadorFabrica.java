package com.leonardo.calculadora.logic.operaciones;

import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;

@Component
public class OperadorFabrica {

    /**
     * Esta es la fabrica de implementaciones, con esta funcion podemos generar cualquier implementacion de operacion
     * y nos permite agregar nuevas operaciones sin necesidad de afectar otros componentes
     *
     * @param operacion: operador de los cuales hemos realizado implementacion
     * @return una instancia de la implementacion de la
     */
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
