package com.uam.guiapracticas4;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
class EmpleadoControllerIntegrationTests {

    private static final String EMPLEADO_VALIDO = """
            {
              "nombres": "Ana María",
              "apellidos": "López Pérez",
              "cargo": "Analista de sistemas",
              "salario": 18500.00
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarDevuelveOkYUnArregloJson() throws Exception {
        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void registrarDevuelveCreatedYElEmpleadoConId() throws Exception {
        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EMPLEADO_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nombres").value("Ana María"));
    }

    @Test
    void registrarDatosInvalidosDevuelveBadRequestConErrores() throws Exception {
        String empleadoInvalido = """
                {
                  "nombres": "",
                  "apellidos": "Pérez",
                  "cargo": "",
                  "salario": 0
                }
                """;

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(empleadoInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value(400))
                .andExpect(jsonPath("$.errores", hasKey("nombres")))
                .andExpect(jsonPath("$.errores", hasKey("cargo")))
                .andExpect(jsonPath("$.errores", hasKey("salario")));
    }

    @Test
    void buscarEmpleadoInexistenteDevuelveNotFound() throws Exception {
        mockMvc.perform(get("/api/empleados/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Empleado no encontrado"));
    }

    @Test
    void permiteRegistrarActualizarYEliminarUnEmpleado() throws Exception {
        String respuesta = mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EMPLEADO_VALIDO))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = respuesta.replaceAll(".*\\\"id\\\":(\\d+).*", "$1");
        String actualizacion = """
                {
                  "nombres": "Ana María",
                  "apellidos": "López Pérez",
                  "cargo": "Arquitecta de software",
                  "salario": 22000.00
                }
                """;

        mockMvc.perform(put("/api/empleados/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(actualizacion))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cargo").value("Arquitecta de software"));

        mockMvc.perform(delete("/api/empleados/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Empleado eliminado correctamente"));

        mockMvc.perform(get("/api/empleados/{id}", id))
                .andExpect(status().isNotFound());
    }
}
