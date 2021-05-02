package com.leonardo.calculadora.controllers.operaciones;

import com.leonardo.calculadora.services.operaciones.CalculadoraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class CalculadoraControllerTest {

    @MockBean
    private CalculadoraService calculadoraService;
    @Autowired
    private CalculadoraController CalculadoraController;
    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(CalculadoraController)
                .build();
    }

    /*==================================================================================================================
    * GET
    ==================================================================================================================*/
    @Test
    void noSoportaGet() throws Exception {
        mvc.perform(get("/calculadora")
                .contentType("application/json"))
                .andExpect(status().isMethodNotAllowed());
    }


    /*==================================================================================================================
    * PUT
    ==================================================================================================================*/

    @Test
    void agregarVacio() throws Exception {
        MvcResult respuesta = mvc.perform(put("/calculadora")
                .content("")
                .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String contenido = respuesta.getResponse().getContentAsString();
        assertTrue("agregarVacio", contenido.contains("invalid json"));
    }

    @Test
    void agregarSoloSesionId() throws Exception {
        MvcResult respuesta = mvc.perform(put("/calculadora")
                .content("")
                .contentType("application/json")
                .content("{\"sesionId\":\"leonardo\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String contenido = respuesta.getResponse().getContentAsString();
        assertTrue("agregarSoloSesionId", contenido.contains("required"));
    }

    @Test
    void agregarSoloValor() throws Exception {
        MvcResult respuesta = mvc.perform(put("/calculadora")
                .content("")
                .contentType("application/json")
                .content("{\"valor\":1}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String contenido = respuesta.getResponse().getContentAsString();
        assertTrue("agregarSoloValor", contenido.contains("required"));
    }

    @Test
    void agregarCompleto() throws Exception {

        given(this.calculadoraService.agregar("leonardo", BigDecimal.ONE)).willReturn(1);

        MvcResult respuesta = mvc.perform(put("/calculadora")
                .content("")
                .contentType("application/json")
                .content("{\"sesionId\":\"leonardo\",\"valor\":1}"))
                .andExpect(status().isOk())
                .andReturn();

        String contenido = respuesta.getResponse().getContentAsString();
        assertTrue("completo", contenido.contains("ok"));
    }

    @Test
    void agregarError() throws Exception {

        when(this.calculadoraService.agregar("leonardo", BigDecimal.ONE))
                .thenThrow(new ArithmeticException("division por cero"));

        MvcResult respuesta = mvc.perform(put("/calculadora")
                .content("")
                .contentType("application/json")
                .content("{\"sesionId\":\"leonardo\",\"valor\":1}"))
                .andExpect(status().is5xxServerError())
                .andReturn();

        String contenido = respuesta.getResponse().getContentAsString();
        assertTrue("agregarError", contenido.contains("error"));
    }

    /*==================================================================================================================
    * POST
    ==================================================================================================================*/

    @Test
    void operarVacio() throws Exception {
        MvcResult respuesta = mvc.perform(post("/calculadora")
                .content("")
                .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String contenido = respuesta.getResponse().getContentAsString();
        assertTrue("operarVacio", contenido.contains("invalid json"));
    }

    @Test
    void operarSoloSesionId() throws Exception {
        MvcResult respuesta = mvc.perform(post("/calculadora")
                .content("")
                .contentType("application/json")
                .content("{\"sesionId\":\"leonardo\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String contenido = respuesta.getResponse().getContentAsString();
        assertTrue("operarSoloSesionId", contenido.contains("required"));
    }

    @Test
    void operarSoloValor() throws Exception {
        MvcResult respuesta = mvc.perform(post("/calculadora")
                .content("")
                .contentType("application/json")
                .content("{\"valor\":1}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String contenido = respuesta.getResponse().getContentAsString();
        assertTrue("operarSoloValor", contenido.contains("required"));
    }

    @Test
    void operarCompleto() throws Exception {
        MvcResult respuesta = mvc.perform(post("/calculadora")
                .content("")
                .contentType("application/json")
                .content("{\"sesionId\":\"leonardo\",\"operador\":\"+\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String contenido = respuesta.getResponse().getContentAsString();
        assertTrue("completo", contenido.contains("data"));
    }

    @Test
    void operarError() throws Exception {
        MvcResult respuesta = mvc.perform(post("/calculadora")
                .content("")
                .contentType("application/json")
                .content("{\"sesionId\":\"leonardo\",\"operador\":\"\"}"))
                .andExpect(status().is5xxServerError())
                .andReturn();

        String contenido = respuesta.getResponse().getContentAsString();
        assertTrue("operarError", contenido.contains("error"));
    }


    /*==================================================================================================================
    * DELETE
    ==================================================================================================================*/


    @Test
    void limpiar() throws Exception {
        MvcResult respuesta = mvc.perform(delete("/calculadora?sesionId=")
                .content("")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String contenido = respuesta.getResponse().getContentAsString();
        assertTrue("completo", contenido.contains("data"));
    }


    @Test
    void limpiarError() throws Exception {
        mvc.perform(delete("/calculadora")
                .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}