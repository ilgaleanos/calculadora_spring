package com.leonardo.calculadora.logic.operaciones;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class OperadorDivisionImpl implements Operador {

    @Override
    public BigDecimal operar(BigDecimal a, BigDecimal b) {
        return a.divide(b, new MathContext(64, RoundingMode.CEILING));
    }

    @Override
    public BigDecimal indentidad() {
        return BigDecimal.ONE;
    }
}
