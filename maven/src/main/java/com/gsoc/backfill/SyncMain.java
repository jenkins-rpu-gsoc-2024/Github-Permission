package com.gsoc.backfill;

import java.io.IOException;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SyncMain {

    private static final Logger logger = LoggerFactory.getLogger(SyncMain.class);
    private GitHub github;

    public SyncMain(GitHub github) {
        this.github = github;
    }

    public static void main(String[] args) throws IOException {
        GitHub github = new GitHubBuilder()
                        .withOAuthToken(System.getenv("GITHUB_OAUTH"))
                        .build();
        SyncMain executor = new SyncMain(github);
        executor.run(args);
    }

    @SuppressWarnings("static-access")
    public void run(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No file path provided.");
        }

        for (String yamlFilePath : args) {
            try {
                logger.info("Processing team configuration for file: " + yamlFilePath);
                Object team = FetchYamlData.loadTeam(yamlFilePath);
                WriteToYAML writeToYAML = new WriteToYAML();
                MergeByName mergeByName = new MergeByName(github);

                if (team instanceof RepoYamlDefinition) {
                    mergeByName.mergeRepoTeam((RepoYamlDefinition) team);
                    writeToYAML.writeRepoTeamToYAML((RepoYamlDefinition) team, yamlFilePath);
                    logger.info("Repository team updated and written.");
                } else if (team instanceof SpecialYamlDefinition) {
                    mergeByName.mergeSpecialTeam((SpecialYamlDefinition) team);
                    writeToYAML.writeSpecialTeamToYAML((SpecialYamlDefinition) team, yamlFilePath);
                    logger.info("Special team updated and written.");
                } else {
                    throw new IllegalArgumentException("Unsupported team definition type.");
                }
            } catch (Exception e) {
                logger.error("Failed to update team for file " + yamlFilePath + ": " + e.getMessage(), e);
            }
        }
    }

    
}
