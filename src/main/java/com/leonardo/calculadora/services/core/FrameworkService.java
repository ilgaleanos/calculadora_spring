package com.leonardo.calculadora.services.core;

import com.leonardo.calculadora.logic.respuestas.RespuestaEstandar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
public class FrameworkService {

    private final static boolean ENABLE_CORS = true;
    private static final String UTF8 = StandardCharsets.UTF_8.name();
    private static final byte[] RESPONSE_ERROR_500 = "{\"success\":0,\"data\":\"internal server error\"}".getBytes(StandardCharsets.UTF_8);


    private final Serializador serializador;


    @Autowired
    FrameworkService(
            Serializador serializador
    ) {
        this.serializador = serializador;
    }


    /*==================================================================================================================
     * MANEJADORES
     *=================================================================================================================*/


    /**
     * Escribe en el response los headers necesarios para permitir CORS
     *
     * @param resp el actual puntero de intercambio http
     */
    private void corsHeaders(HttpServletRequest req, HttpServletResponse resp) {
        String origin = req.getHeader("Origin");
        if (origin == null) return;
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, withCredentials, Cookie");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
    }


    /**
     * @param req:      la petición enviada por el usuario, necesaria por seguridad
     * @param resp:     el actual puntero de intercambio http
     * @param response: bytes con los datos de entrega en el intercambio http
     * @param tipo:     Tipo de elemento retornado al cliente
     */
    public void sendBYTES(HttpServletRequest req, HttpServletResponse resp, byte[] response, int length, String tipo) throws IOException {
        if (ENABLE_CORS) corsHeaders(req, resp);
        resp.setContentType(tipo);
        try (ServletOutputStream out = resp.getOutputStream()) {
            out.write(response, 0, length);
        }
    }


    /**
     * Finaliza el intercambio HTTP con el string proporcionado y lo escribe en el response
     *
     * @param req:      la petición enviada por el usuario, necesaria por seguridad
     * @param resp:     el actual puntero de intercambio http
     * @param response: estructura final con los datos de entrega en el intercambio http
     */
    public void sendJSON(HttpServletRequest req, HttpServletResponse resp, RespuestaEstandar response) throws IOException {
        if (ENABLE_CORS) corsHeaders(req, resp);
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            serializador.serialize(response, outputStream);
        }
    }


    /**
     * Finaliza el intercambio HTTP con el string proporcionado y lo escribe en el response
     *
     * @param req:     la petición enviada por el usuario, necesaria por seguridad
     * @param resp:    el actual puntero de intercambio http
     * @param entrada: parámetros requeridos por la petición
     */
    public void sendBadRequestJSON(HttpServletRequest req, HttpServletResponse resp, Object entrada) throws IOException {
        if (ENABLE_CORS) corsHeaders(req, resp);
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);

        HashMap<String, Object> response = new HashMap<>();
        response.put("success", 0);
        if (entrada != null) {
            Field[] parametros = entrada.getClass().getDeclaredFields();
            HashMap<String, String> campos = new HashMap<>(parametros.length);
            for (Field param : parametros) {
                campos.put(param.getName(), param.getType().getSimpleName());
            }
            response.put("required", campos);
        } else {
            response.put("message", "invalid json");
        }

        try (ServletOutputStream out = resp.getOutputStream()) {
            serializador.serialize(response, out);
        }
    }


    /**
     * Finaliza el intercambio HTTP con el string proporcionado y lo escribe en el response
     *
     * @param req:  la petición enviada por el usuario, necesaria por seguridad
     * @param resp: el actual puntero de intercambio http
     */
    public void sendErrorJSON(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (ENABLE_CORS) corsHeaders(req, resp);
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (ServletOutputStream out = resp.getOutputStream()) {
            out.write(RESPONSE_ERROR_500);
        }
    }


    /*==================================================================================================================
     *  obtener body raw
     *=================================================================================================================*/

    /**
     * Transforma el cuerpo de la petición a un Gson, por rendimiento pensado para peticiones con body pequeño
     *
     * @param req           HttpServletRequest
     * @param acumularClass
     * @return HashMap
     * @throws IOException cuando no pueda crear el reader
     */
    public <T> T getBody(HttpServletRequest req, Class<T> acumularClass) throws IOException {
        req.setCharacterEncoding(UTF8);
        try {
            return serializador.deserialize(acumularClass, req.getInputStream());
        } catch (Exception err) {
            return null;
        }
    }
}
