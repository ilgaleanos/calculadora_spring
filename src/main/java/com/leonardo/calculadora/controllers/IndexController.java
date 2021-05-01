package com.leonardo.calculadora.controllers;

import com.leonardo.calculadora.logic.respuestas.RespuestaEstandar;
import com.leonardo.calculadora.services.core.FrameworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


@RestController
public class IndexController {

    private static final Date iniciado = new Date();
    private final FrameworkService fw;


    @Autowired
    public IndexController(
            FrameworkService fw
    ) {
        this.fw = fw;
    }

    @RequestMapping(
            value = "/",
            method = RequestMethod.GET
    )
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RespuestaEstandar respuestaEstandar = new RespuestaEstandar(
                1,
                iniciado
        );
        fw.sendJSON(req, resp, respuestaEstandar);
    }
}
