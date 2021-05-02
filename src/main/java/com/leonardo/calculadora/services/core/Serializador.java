package com.leonardo.calculadora.services.core;

import com.dslplatform.json.DslJson;
import com.dslplatform.json.runtime.Settings;
import com.leonardo.calculadora.logic.respuestas.RespuestaEstandar;
import org.springframework.stereotype.Service;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * En esta función implementamos el serializador de nuestras peticiones
 */
@Service
public class Serializador {
    private final static DslJson.Settings<Object> settings = Settings.withRuntime().includeServiceLoader();
    public final static DslJson<Object> dslJson = new DslJson<>(settings);

    public void serialize(RespuestaEstandar response, ServletOutputStream outputStream) throws IOException {
        dslJson.serialize(response, outputStream);
    }

    public void serialize(HashMap<String, Object> response, ServletOutputStream outputStream) throws IOException {
        dslJson.serialize(response, outputStream);
    }

    public void serialize(Exception err, ServletOutputStream outputStream) throws IOException {
        dslJson.serialize(err, outputStream);
    }

    public <T> T deserialize(Class<T> acumularClass, ServletInputStream inputStream) throws IOException {
        return dslJson.deserialize(acumularClass, inputStream);
    }
}
