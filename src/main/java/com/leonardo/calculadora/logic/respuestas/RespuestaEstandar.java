package com.leonardo.calculadora.logic.respuestas;

import com.dslplatform.json.CompiledJson;
import org.springframework.stereotype.Component;

@Component
@CompiledJson
public class RespuestaEstandar {

    private int code = 0;
    private Object data;

    // requerido para serialización
    public RespuestaEstandar() {
    }

    public RespuestaEstandar(int code) {
        this.code = code;
    }

    public RespuestaEstandar(int code, Object data) {
        this.code = code;
        this.data = data;
    }


    /* =================================================================================================================
    * getters y setter para serialización
    ================================================================================================================= */


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
