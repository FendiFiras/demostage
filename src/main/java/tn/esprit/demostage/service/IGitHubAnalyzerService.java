package tn.esprit.demostage.service;

public interface IGitHubAnalyzerService {
    String analyzeAndSuggest(String githubRepoUrl) throws Exception;

}
