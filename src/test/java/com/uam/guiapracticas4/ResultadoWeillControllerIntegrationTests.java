package com.uam.guiapracticas4;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
class ResultadoWeillControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registrarResultadoValidoDevuelveCreated() throws Exception {
        mockMvc.perform(post("/api/resultados-weill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombreParticipante": "Carlos Martínez",
                                  "edad": 21,
                                  "puntaje": 78,
                                  "clasificacion": "Superior al promedio",
                                  "fechaRealizacion": "2024-06-15"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.puntaje").value(78));
    }

    @Test
    void datosInvalidosDevuelvenBadRequestConDetalle() throws Exception {
        mockMvc.perform(post("/api/resultados-weill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombreParticipante": "",
                                  "edad": 4,
                                  "puntaje": 110,
                                  "clasificacion": "",
                                  "fechaRealizacion": "2999-01-01"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value(400))
                .andExpect(jsonPath("$.errores", hasKey("nombreParticipante")))
                .andExpect(jsonPath("$.errores", hasKey("edad")))
                .andExpect(jsonPath("$.errores", hasKey("puntaje")))
                .andExpect(jsonPath("$.errores", hasKey("clasificacion")))
                .andExpect(jsonPath("$.errores", hasKey("fechaRealizacion")));
    }

    @Test
    void resultadoInexistenteDevuelveNotFound() throws Exception {
        mockMvc.perform(get("/api/resultados-weill/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Resultado Weill no encontrado"));
    }
}
