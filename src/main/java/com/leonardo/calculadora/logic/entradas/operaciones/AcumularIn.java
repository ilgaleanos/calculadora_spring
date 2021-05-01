package com.leonardo.calculadora.logic.entradas.operaciones;

import com.dslplatform.json.CompiledJson;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@CompiledJson
public class AcumularIn {

    private String sesionId = null;
    private BigDecimal valor = null;

    // requerido para serialización
    public AcumularIn() {
    }


    /* =================================================================================================================
    * getters y setter para serialización
    ================================================================================================================= */

    public boolean esInValido() {
        return sesionId == null || valor == null;
    }

    public String getSesionId() {
        return sesionId;
    }

    public void setSesionId(String sesionId) {
        this.sesionId = sesionId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}

