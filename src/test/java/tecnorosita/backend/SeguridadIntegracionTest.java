package tecnorosita.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SeguridadIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void alEnviarCredencialesInvalidas_entoncesRetornaErrorDeAcceso() throws Exception {
        String credencialesFalsas = "{\"email\": \"hacker@correo.com\", \"password\": \"claveFalsa123\"}";

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credencialesFalsas))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void alAccederRutaProtegidaSinToken_entoncesRetornaProhibido() throws Exception {
        mockMvc.perform(get("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void alAccederRutaPublicaSwagger_entoncesRetornaExito() throws Exception {
        mockMvc.perform(get("/v3/api-docs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

