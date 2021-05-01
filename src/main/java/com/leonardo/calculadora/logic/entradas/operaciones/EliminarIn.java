package com.leonardo.calculadora.logic.entradas.operaciones;

import com.dslplatform.json.CompiledJson;
import org.springframework.stereotype.Component;

@Component
@CompiledJson
public class EliminarIn {

    private String sesionId = null;

    // requerido para serialización
    public EliminarIn() {
    }

    public EliminarIn(String sesionId) {
        this.sesionId = sesionId;
    }


    /* =================================================================================================================
    * getters y setter para serialización
    ================================================================================================================= */

    public boolean esInValido() {
        return sesionId == null;
    }

    public String getSesionId() {
        return sesionId;
    }

    public void setSesionId(String sesionId) {
        this.sesionId = sesionId;
    }

}

