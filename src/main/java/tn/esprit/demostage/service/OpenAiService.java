package tn.esprit.demostage.service;


import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
public class OpenAiService implements IOpenAiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";

    @Override
    public String getSuggestions(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = """
                {
                    "model": "llama3",
                    "prompt": "%s",
                    "stream": false
                }
                """.formatted(prompt);

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(OLLAMA_URL, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().get("response").toString();
        } else {
            return "Erreur lors de l'appel à Ollama.";
        }
    }

}
