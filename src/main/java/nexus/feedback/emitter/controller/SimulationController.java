package nexus.feedback.emitter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import nexus.feedback.emitter.service.FeedbackEmitterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Simulation", description = "Endpoints para controle de simulação de carga")
@RestController
public class SimulationController {

    private final FeedbackEmitterService emitterService;

    public SimulationController(FeedbackEmitterService emitterService) {
        this.emitterService = emitterService;
    }

    @Operation(summary = "Inicia a simulação de carga assíncrona", 
               description = "Dispara a quantidade informada de requisições POST em background utilizando Virtual Threads.")
    @ApiResponse(responseCode = "200", description = "Simulação iniciada com sucesso")
    @PostMapping("/api/v1/simulation/start")
    public String startSimulation(
            @Parameter(description = "Quantidade de requisições HTTP a serem geradas", example = "1000")
            @RequestParam(defaultValue = "20") int amount) {
        
        Thread.ofVirtual().start(() -> emitterService.emitMassiveFeedbacks(amount));
        return "Simulação de " + amount + " requisições iniciada em background!";
    }
}
