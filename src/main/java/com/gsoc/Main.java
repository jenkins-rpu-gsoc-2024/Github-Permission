package com.gsoc;

public class Main {
    public static void main(String[] args) {
        // TeamDefinition team = yamlTeamLoader.loadTeam("permissions/Test.YAML");
        // TeamUpdater.updateTeam(team);
        
        if (args.length == 0) {
            System.out.println("No file path provided.");
            System.exit(1);
        }

        String yamlFilePath = args[0];
        GithubTeamDefinition team = yamlTeamLoader.loadTeam(yamlFilePath);
        TeamUpdater.updateTeam(team);
    }
}