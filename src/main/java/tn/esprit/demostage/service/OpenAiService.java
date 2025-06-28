package tn.esprit.demostage.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAiService implements IOpenAiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";

    @Override
    public String getSuggestions(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama3");
        requestBody.put("prompt", prompt);
        requestBody.put("stream", false);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(OLLAMA_URL, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().containsKey("response")) {
                return response.getBody().get("response").toString();
            } else {
                return "Erreur Ollama : réponse inattendue ou vide";
            }
        } catch (Exception e) {
            return "Erreur lors de l'appel à Ollama : " + e.getMessage();
        }
    }
}