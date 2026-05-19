package nexus.feedback.emitter.controller;

import nexus.feedback.emitter.service.FeedbackEmitterService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SimulationController.class)
class SimulationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackEmitterService emitterService;

    @Test
    void deveIniciarSimulacaoComSucessoERetornarStatus200() throws Exception {
        mockMvc.perform(post("/api/v1/simulation/start")
                        .param("amount", "50"))
                .andExpect(status().isOk())
                .andExpect(content().string("Simulação de 50 requisições iniciada em background!"));

        Mockito.verify(emitterService, Mockito.timeout(1000).times(1))
                .emitMassiveFeedbacks(50);
    }
}
