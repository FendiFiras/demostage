package tn.esprit.demostage.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Analyzer {

    public static String runAnalysis(Path codePath) throws IOException {
        StringBuilder result = new StringBuilder("Analyse des fichiers Java dans : " + codePath + "\n\n");

        Files.walk(codePath)
                .filter(file -> file.toString().endsWith(".java"))
                .forEach(file -> result.append("- ").append(file.getFileName()).append("\n"));

        return result.toString();
    }
}

