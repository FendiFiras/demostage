package tn.esprit.demostage.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Analyzer {

    public static String runAnalysis(Path codePath) {
        try {
            // 🔧 Chemin vers le script PMD sur ta machine
            String pmdPath = "C:/pmd-bin-7.15.0/bin/pmd.bat";
            Path reportPath = codePath.getParent().resolve("pmd-report.txt");
            Path cachePath = codePath.getParent().resolve("pmd-cache");

            ProcessBuilder builder = new ProcessBuilder(
                    pmdPath,
                    "check",
                    "-d", codePath.toString(),
                    "-R", "category/java/bestpractices.xml",
                    "-f", "text",
                    "-r", reportPath.toString(),
                    "--cache", cachePath.toString()
            );

            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();
            // Lire le rapport depuis le fichier
            return Files.readString(reportPath);

        } catch (IOException e) {
            return "Erreur d'entrée/sortie lors de l'exécution de PMD : " + e.getMessage();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaurer l'état d'interruption
            return "Interruption lors de l'exécution de PMD : " + e.getMessage();
        }
    }
}