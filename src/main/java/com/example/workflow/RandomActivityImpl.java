package com.example.workflow;

import jakarta.inject.Singleton;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@Singleton
class RandomActivityImpl implements RandomActivity {
    private final Logger log = getLogger(RandomActivityImpl.class);

    @Override
    public String random() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getJavaVersion() {
        StringBuilder versionInfo = new StringBuilder();

        try {
            var process = new ProcessBuilder("java", "-version").start();

            try (var reader = new BufferedReader(new InputStreamReader(
                    process.getErrorStream(), StandardCharsets.UTF_8
            ))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    versionInfo.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Failed to get java version, exit code: " + exitCode);
            }

            return versionInfo.toString().trim();
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to get Java version", e);
        }
    }

    @Override
    public void writeToFiles(String path, String content) {
        try {
            String fileName = path + UUID.randomUUID() + ".txt";
            Files.createDirectories(Paths.get(path));
            try (var writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write(content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void logValue(String value, String workflowId) {
        if (value.startsWith("42")) {
            throw new RuntimeException("Test exception");
        }
        log.info("Logged value: {} from workflowId: {}", value, workflowId);
    }

    @Override
    public void logWarning(String warning, String workflowId) {
        log.warn("Logged warning: {} from workflowId: {}", warning, workflowId);
    }
}
