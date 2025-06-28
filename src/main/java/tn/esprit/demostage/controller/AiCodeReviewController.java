package tn.esprit.demostage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.demostage.service.IGitHubAnalyzerService;

@RestController
@RequestMapping("/ai-review")
public class AiCodeReviewController {

    private final IGitHubAnalyzerService analyzerService;

    public AiCodeReviewController(IGitHubAnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    @PostMapping
    public ResponseEntity<String> reviewCode(@RequestParam String githubUrl) {
        try {
            String response = analyzerService.analyzeAndSuggest(githubUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) 
             ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur : " + e.getMessage());
        }
    }
}
