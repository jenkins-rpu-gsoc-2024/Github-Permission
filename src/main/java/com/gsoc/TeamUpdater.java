package com.gsoc;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTeam;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

public class TeamUpdater {
    

    
    public static void updateTeam(GithubTeamDefinition team) {
        try {
            GitHub github = new GitHubBuilder().withOAuthToken(System.getenv("PERSONAL_TOKEN")).build();
            
            String[] parts = team.getName().split("/");
            String orgName = parts[0];
            String repoName = parts[1];
            
            GHOrganization org = github.getOrganization(orgName);
            GHRepository repo = org.getRepository(repoName);
            GHTeam ghTeam = org.getTeamByName(team.getTeamName());

            if (repo != null) {

                
                
                if (ghTeam == null) {
                    // Case 1: Team does not exist
                    ghTeam = org.createTeam(team.getTeamName()).privacy(GHTeam.Privacy.CLOSED).create();
                    updateDevelopers(github, ghTeam, team.getDevelopers(), Operation.ADD);
                    ghTeam.add(repo, GHOrganization.RepositoryRole.custom("push"));
                    System.out.println("Team: '" + team.getTeamName() + "' created and added to repository: " + repoName);
                
                }else{ 

                    // Case 2: Team exists
                    Set<String> currentMembers = new HashSet<>();
                    for (GHUser member : ghTeam.listMembers()) {
                        currentMembers.add(member.getLogin());
                    }

                    System.out.println("Current members before any operations: " + currentMembers);

                    // Case 2.1: developers to add
                    Set<String> toAdd = new HashSet<>(team.getDevelopers());
                    toAdd.removeAll(currentMembers);

                    if (!toAdd.isEmpty()) {
                        updateDevelopers(github, ghTeam, toAdd, Operation.ADD);
                    }

                    // Case 2.2: developers to remove
                    Set<String> toRemove = new HashSet<>(currentMembers);
                    toRemove.removeAll(team.getDevelopers());

                    if (!toRemove.isEmpty()) {
                        updateDevelopers(github, ghTeam, toRemove, Operation.REMOVE);
                    }
                }
            
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

    public enum Operation {
        ADD,
        REMOVE
    }

    public static void updateDevelopers(GitHub github, GHTeam ghTeam, Set<String> developers, Operation operation) {
        for (String dev : developers) {
            try {
                GHUser user = github.getUser(dev);
                if (operation == Operation.ADD) {
                    ghTeam.add(user);
                    System.out.println("Developer '" + dev + "' added to team: " + ghTeam.getName());
                } else if (operation == Operation.REMOVE) {
                    ghTeam.remove(user);
                    System.out.println("Developer '" + dev + "' removed from team: " + ghTeam.getName());
                }
            } catch (IOException e) {
                System.out.println("Developer '" + dev + "' not found in GitHub or error in updating: " + e.getMessage());
            }
        }
    }



}
