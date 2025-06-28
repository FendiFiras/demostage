package tn.esprit.demostage.service;

import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class GitHubAnalyzerService implements IGitHubAnalyzerService {

    private final IOpenAiService openAiService;

    public GitHubAnalyzerService(IOpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Override
    public String analyzeAndSuggest(String githubUrl) throws Exception {
        String subPath = "demostage/tree/main/src/main/java/tn/esprit/demostage";
        Path tempRepo = Files.createTempDirectory("clonedRepo");

        try (Git git = Git.cloneRepository()
                .setURI(githubUrl)
                .setDirectory(tempRepo.toFile())
                .call()) {
            Path codePath = tempRepo.resolve(subPath);
            String pmdResult = Analyzer.runAnalysis(codePath);

            // Créer un prompt IA
            String prompt = "Voici le résultat d'une analyse statique sur du code Java :\n\n" +
                    pmdResult + "\n\n" +
                    "Peux-tu proposer des corrections pour ces erreurs ?";

            // Envoyer à Ollama
            String suggestion = openAiService.getSuggestions(prompt);

            // Retourner les deux : erreurs + suggestion
            return "🧪 Résultats PMD :\n" + pmdResult + "\n\n🤖 Suggestions IA :\n" + suggestion;
        } finally {
            // Nettoyer le répertoire temporaire
            try {
                Files.walk(tempRepo)
                        .sorted((a, b) -> b.compareTo(a))
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                // Gérer silencieusement ou journaliser si nécessaire
                            }
                        });
            } catch (IOException e) {
                // Gérer l'erreur de nettoyage si nécessaire
            }

        }
    }
}