package com.leonardo.calculadora.controllers.operaciones;

import com.leonardo.calculadora.logic.entradas.operaciones.AcumularIn;
import com.leonardo.calculadora.logic.entradas.operaciones.EliminarIn;
import com.leonardo.calculadora.logic.entradas.operaciones.OperarIn;
import com.leonardo.calculadora.logic.operaciones.OperadorFabrica;
import com.leonardo.calculadora.logic.respuestas.RespuestaEstandar;
import com.leonardo.calculadora.services.core.FrameworkService;
import com.leonardo.calculadora.services.operaciones.CalculadoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;


@RestController
public class CalculadoraController {

    private final FrameworkService fw;
    private final CalculadoraService calculadoraService;

    @Autowired
    public CalculadoraController(
            FrameworkService fw,
            CalculadoraService calculadoraService
    ) {
        this.fw = fw;
        this.calculadoraService = calculadoraService;
    }

    @RequestMapping(
            value = "/calculadora",
            method = RequestMethod.PUT,
            consumes = "application/json"
    )
    public void agregar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AcumularIn body = fw.getBody(req, AcumularIn.class);
        if (body == null || body.esInValido()) {
            fw.sendBadRequestJSON(req, resp, body);
            return;
        }

        try {
            int code = this.calculadoraService.agregar(body.getSesionId(), body.getValor());
            RespuestaEstandar respuestaEstandar = new RespuestaEstandar(code, "ok");
            fw.sendJSON(req, resp, respuestaEstandar);
        } catch (Exception err) {
            fw.sendErrorJSON(req, resp, err);
        }
    }


    @RequestMapping(
            value = "/calculadora",
            method = RequestMethod.POST,
            consumes = "application/json"
    )
    public void operar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        OperarIn body = fw.getBody(req, OperarIn.class);
        if (body == null || body.esInValido()) {
            fw.sendBadRequestJSON(req, resp, body);
            return;
        }

        try {
            BigDecimal respuesta = this.calculadoraService.operar(
                    body.getSesionId(),
                    OperadorFabrica.obtenerInstancia(body.getOperador())
            );
            RespuestaEstandar respuestaEstandar = new RespuestaEstandar(1, respuesta);
            fw.sendJSON(req, resp, respuestaEstandar);
        } catch (Exception err) {
            fw.sendErrorJSON(req, resp, err);
        }
    }


    @RequestMapping(
            value = "/calculadora",
            method = RequestMethod.DELETE
    )
    public void limpiar(HttpServletRequest req, HttpServletResponse resp, @RequestParam String sesionId) throws IOException {
        EliminarIn eliminarIn = new EliminarIn(sesionId);

        try {
            int code = this.calculadoraService.eliminar(eliminarIn.getSesionId());
            RespuestaEstandar respuestaEstandar = new RespuestaEstandar(code, code == 1 ? "ok" : "fail");
            fw.sendJSON(req, resp, respuestaEstandar);
        } catch (Exception err) {
            fw.sendErrorJSON(req, resp, err);
        }
    }
}
