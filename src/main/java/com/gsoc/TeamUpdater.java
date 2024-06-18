package com.gsoc;

import java.io.IOException;

import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTeam;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

public class TeamUpdater {

    public static void updateTeam(TeamDefinition team) {
        try {
            GitHub github = new GitHubBuilder().withOAuthToken("my_personal_token","Alaurant").build();
            String[] parts = team.getName().split("/");
            String orgName = parts[0];
            String repoName = parts[1];
            
            GHOrganization org = github.getOrganization(orgName);
            GHRepository repo = org.getRepository(repoName);
            GHTeam ghTeam = org.getTeamByName(team.getTeamName());

            if (repo != null && ghTeam != null) {
                // Adding the team to the repository with push access
                ghTeam.add(repo);
                System.out.println("Team '" + team.getTeamName() + "' added to repository: " + team.getName());
            } else {
                if (repo == null) {
                    System.out.println("Repository not found: " + repoName);
                }
                if (ghTeam == null) {
                    System.out.println("Team not found: " + team.getTeamName());
                }
            }
            
            
        } catch (IOException e) {
            System.out.println("Error connecting to GitHub or fetching repository.");
            e.printStackTrace();
        }
    }

}
