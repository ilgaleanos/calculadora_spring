package com.leonardo.calculadora.logic.operaciones;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OperadorPotenciaImpl implements Operador {

    @Override
    public BigDecimal operar(BigDecimal a, BigDecimal b) {
        return a.pow(b.intValue());
    }

    @Override
    public BigDecimal indentidad() {
        return BigDecimal.ZERO;
    }
}
