package tn.esprit.demostage.service;

import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class GitHubAnalyzerService implements IGitHubAnalyzerService {

    @Override
    public String analyzeAndSuggest(String githubUrl) throws Exception {
        // Tu peux hardcoder ou dériver le chemin ici si besoin
        String subPath = "Mini_projet_Restaurant/Backend/ReservationMicroservice/src/main/java/org/esprit/reservationmicroservice";

        // 1. Cloner
        Path tempRepo = Files.createTempDirectory("clonedRepo");
        Git.cloneRepository()
                .setURI(githubUrl)
                .setDirectory(tempRepo.toFile())
                .call();

        // 2. Aller dans le sous-dossier
        Path codePath = tempRepo.resolve(subPath);

        // 3. Lancer l’analyse (on va créer Analyzer après)
        return Analyzer.runAnalysis(codePath);
    }
}
