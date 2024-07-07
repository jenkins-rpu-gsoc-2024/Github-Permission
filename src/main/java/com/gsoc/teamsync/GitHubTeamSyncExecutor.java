package com.gsoc.teamsync;

public class GitHubTeamSyncExecutor {

    public static void main(String[] args) {
        // TeamDefinition team = yamlTeamLoader.loadTeam("permissions/Test.YAML");
        // TeamUpdater.updateTeam(team);
        
        if (args.length == 0) {
            System.out.println("No file path provided.");
            System.exit(1);
        }

        String yamlFilePath = args[0];
        GithubTeamDefinition team = YAMLTeamLoader.loadTeam(yamlFilePath);
        TeamUpdater.updateTeam(team);
    }
}