package com.leonardo.calculadora.logic.entradas.operaciones;

import com.dslplatform.json.CompiledJson;

@CompiledJson
public class OperarIn {

    private String sesionId = null;
    private String operador = null;

    // requerido para serialización
    public OperarIn() {
    }


    /* =================================================================================================================
    * getters y setter para serialización
    ================================================================================================================= */

    public boolean esInValido() {
        return sesionId == null || operador == null;
    }

    public String getSesionId() {
        return sesionId;
    }

    public void setSesionId(String sesionId) {
        this.sesionId = sesionId;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }
}

