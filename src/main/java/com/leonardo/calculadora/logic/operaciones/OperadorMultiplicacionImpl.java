package com.leonardo.calculadora.logic.operaciones;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OperadorMultiplicacionImpl implements Operador {

    @Override
    public BigDecimal operar(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }

    @Override
    public BigDecimal indentidad() {
        return BigDecimal.ONE;
    }
}
