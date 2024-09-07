package com.gsoc.backfill;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class FetchYamlData {

    private static final Logger logger = LoggerFactory.getLogger(FetchYamlData.class);

    // Defines paths for permission and team YAML files
    private static final Path PERMISSIONS_PATH = Paths.get("permissions").toAbsolutePath().normalize();
    private static final Path TEAMS_PATH = Paths.get("teams").toAbsolutePath().normalize();

    private final Path resolvedPath;
    public FetchYamlData(String filePath) throws IOException {
        this.resolvedPath = resolveFilePath(filePath);
        loadYamlConfiguration(this.resolvedPath);
    }

    public static Object loadTeam(String filePath) throws IOException {
        Path resolvedPath = resolveFilePath(filePath);
        Map<String, Object> teamConfig = loadYamlConfiguration(resolvedPath);

        if (filePath.startsWith("permissions/")) {
            return parsePermissionsTeamDefinition(teamConfig);
        } else if (filePath.startsWith("teams/")) {
            return parseTeamsTeamDefinition(teamConfig);
        } else {
            throw new IllegalArgumentException("Unsupported file path: " + filePath);
        }
    }

    // Resolves and secures a YAML file path, protecting against path traversal attacks.
    private static Path resolveFilePath(String filePath) {
        Path basePath = filePath.startsWith("permissions/") ? PERMISSIONS_PATH : TEAMS_PATH;
        Path resolvedPath = basePath.resolve(filePath.replaceFirst("^(permissions/|teams/)", "")).normalize();

        if (!resolvedPath.startsWith(basePath)) {
            throw new SecurityException("Attempted path traversal out of allowed directory");
        }
        if (!resolvedPath.toString().endsWith(".yml")) {
            throw new SecurityException("Invalid file type");
        }
        if (!Files.exists(resolvedPath)) {
            throw new RuntimeException("File does not exist: " + resolvedPath);
        }
        return resolvedPath;
    }


    private static Map<String, Object> loadYamlConfiguration(Path path) {
        try (FileInputStream inputStream = new FileInputStream(path.toFile())) {
            Yaml yaml = new Yaml();
            return yaml.load(inputStream);
        } catch (Exception e) {
            logger.error("Failed to load YAML configuration: {}", path, e);
            throw new RuntimeException("Failed to load YAML configuration: " + path, e);
        }
    }

    private static RepoYamlDefinition parsePermissionsTeamDefinition(
            Map<String, Object> teamConfig) throws IOException {

        // Extract the repo name and org name from the GitHub key
        // e.g. github: &GH "jenkinsci/commons-lang3-api-plugin"
        // orgName = jenkinsci, repoName = commons-lang3-api-plugin
        if (!teamConfig.containsKey("github") || !(teamConfig.get("github") instanceof String)) {
            logger.error("The 'github' field is missing");
            return null;
        }
        String repoPath = (String) teamConfig.get("github");
        String[] parts = repoPath.split("/");
        if (parts.length < 2) {
            logger.error("Invalid GitHub path: {}", repoPath);
            return null;
        }
        String orgName = parts[0];
        String repoName = parts[1];

        // Extract the developers, example:
        // developers:
        //  - "user1"
        //  - "user2"
        List<DeveloperInfo> developers = extractDevelopers(teamConfig);

        return new RepoYamlDefinition(repoName, orgName, developers, new HashSet<>());
    }

    private static SpecialYamlDefinition parseTeamsTeamDefinition(Map<String, Object> teamConfig) throws IOException {
        String teamName = (String) teamConfig.getOrDefault("name", "");
        List<DeveloperInfo> developers = extractDevelopers(teamConfig);

        if (teamName == null || teamName.trim().isEmpty()) {
            // If developers is not empty, then team name is required
            if (!developers.isEmpty()) {
                throw new IllegalArgumentException("No valid team name found.");
            }
        }

        return new SpecialYamlDefinition(teamName, developers);
    }

    private static List<DeveloperInfo> extractDevelopers(Map<String, Object> teamConfig) {
        List<DeveloperInfo> developers = new ArrayList<>();
        Object devsObject = teamConfig.get("developers");
        if (devsObject instanceof List) {
            for (Object obj : (List<?>) devsObject) {
                if (obj instanceof String) {
                    developers.add(new DeveloperInfo((String) obj, null));
                } else {
                    throw new RuntimeException("Expected a list of developer usernames.");
                }
            }
        } else {
            throw new RuntimeException("Expected a list of developer usernames.");
        }
        return developers;
    }

}
