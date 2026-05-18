package nexus.feedback.emitter.service;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FeedbackEmitterService {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .executor(Executors.newVirtualThreadPerTaskExecutor())
            .build();

    private final String PROCESSOR_URL = "http://localhost:8081/api/v1/feedbacks";
    private final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final Random random = new Random();

    public void emitMassiveFeedbacks(int totalRequests) {
        
        int targetRequests = (totalRequests <= 0) ? 20 : totalRequests;
        
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            for (int i = 0; i < targetRequests; i++) {
                final int index = i;
                
                executor.submit(() -> {
                    try {
                        String randomDescription = generateRandomString(20);
                        
                        String jsonPayload = """
                            {
                              "title": "Erro detectado no lote automático - ID %s",
                              "description": "%s",
                              "customerEmail": "usuario_%d@nexus.com"
                            }
                            """.formatted(UUID.randomUUID(), randomDescription, index);

                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(PROCESSOR_URL))
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                                .build();

                        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                        // Correção do status HTTP esperado para criação (Spring retorna 201 Created)
                        if (response.statusCode() == 201) { 
                            successCount.incrementAndGet();
                        } else {
                            errorCount.incrementAndGet();
                        }
                    } catch (Exception e) {
                        errorCount.incrementAndGet();
                    }
                });
            }
        }

        System.out.printf("Carga finalizada! Sucesso: %d | Erros/Falhas: %d%n", successCount.get(), errorCount.get());
    }

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(index));
        }
        return sb.toString();
    }
}
