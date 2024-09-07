package com.gsoc.backfill;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHTeam;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileCheck {

    private static final Logger logger = LoggerFactory.getLogger(FileCheck.class);

    public static Set<String> collectGithubTeams() throws IOException {
        Set<String> teamNames = new HashSet<>();
        int count = 0;

        GitHub github = new GitHubBuilder()
                    .withOAuthToken(System.getenv("GITHUB_OAUTH"))
                    .build();
        GHOrganization org = github.getOrganization("jenkinsci");
        Map<String, GHTeam> teams = org.getTeams();

        for (GHTeam team : teams.values()) {
            teamNames.add(team.getName());
            count++;
        }

        logger.info("Collected {} GitHub teams.", count);
        return teamNames;
    }

    public static Set<String> collectYamlTeams(String[] yamlFilePaths) throws IOException {
        Set<String> repoNames = new HashSet<>();

        for (String yamlFilePath : yamlFilePaths) {
            logger.info("Processing: " + yamlFilePath);

            Object team = FetchYamlData.loadTeam(yamlFilePath);

            if (team == null) {
                logger.warn("Skipping file due to parsing failure: " + yamlFilePath);
                continue;
            }

            if (team instanceof RepoYamlDefinition) {
                RepoYamlDefinition repoYamlDefinition = (RepoYamlDefinition) team;
                String repoName = repoYamlDefinition.getTeamName();
                repoNames.add(repoName);
            }
        }

        logger.info("Collected {} repository names from YAML files.", repoNames.size());
        return repoNames;
    }

    public static void compareAndUpdateFiles() throws IOException {
        // Collect GitHub and YAML teams into sets
        Set<String> githubTeams = collectGithubTeams();
        Set<String> yamlTeams = readFileToSet("yaml_teams.txt");

        // Find common teams and remove them from both sets
        Set<String> commonTeams = new HashSet<>(githubTeams);
        commonTeams.retainAll(yamlTeams);
        logger.info("Found {} common teams.", commonTeams.size());

        githubTeams.removeAll(commonTeams);
        yamlTeams.removeAll(commonTeams);

        // Write updated sets back to the files
        writeSetToFile("github_teams.txt", githubTeams);
        writeSetToFile("yaml_teams.txt", yamlTeams);

        logger.info("github_teams.txt and yaml_teams.txt have been updated.");
    }

    private static Set<String> readFileToSet(String fileName) throws IOException {
        Set<String> resultSet = new HashSet<>();
        File file = new File(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                resultSet.add(line.trim());
            }
        }
        return resultSet;
    }

    private static void writeSetToFile(String fileName, Set<String> set) throws IOException {
        File file = new File(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : set) {
                writer.write(line + "\n");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            Set<String> yamlTeams = collectYamlTeams(args);
            writeSetToFile("yaml_teams.txt", yamlTeams);
        }
        compareAndUpdateFiles();
    }
}
