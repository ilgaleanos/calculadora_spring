package com.leonardo.calculadora.logic.operaciones;

import java.math.BigDecimal;

public interface Operador {

    BigDecimal operar(BigDecimal a, BigDecimal b);

    BigDecimal indentidad();
}
