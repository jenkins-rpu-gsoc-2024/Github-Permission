package com.gsoc;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No file path provided.");
            System.exit(1);
        }

        String yamlFilePath = args[0];
        TeamDefinition team = yamlTeamLoader.loadTeam(yamlFilePath);
        TeamUpdater.updateTeam(team);

    }
}