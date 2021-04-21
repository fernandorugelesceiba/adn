package com.ceiba.transaccion.controlador;

import com.ceiba.ApplicationMock;
import com.ceiba.transaccion.comando.ComandoTransaccion;
import com.ceiba.transaccion.testdatabuilder.ComandoTransaccionTestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationMock.class)
@WebMvcTest(ComandoControladorTransacciones.class)
public class ComandoControladorTransaccionTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mocMvc;

    @Test
    public void crear() throws Exception {
        // arrange
        ComandoTransaccion transaccion = new ComandoTransaccionTestDataBuilder().conFechaValida("2016-03-04 11:30").build();

        // act - assert
        mocMvc.perform(post("/transacciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaccion)))
                .andExpect(status().isOk()).andExpect(content().json("{'valor': 2}"));
    }
}
