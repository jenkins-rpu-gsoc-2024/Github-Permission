package com.gsoc.backfill;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.kohsuke.github.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MergeByName {

    private static final Logger logger = LoggerFactory.getLogger(MergeByName.class);

    private GitHub github;

    public MergeByName(GitHub github) {
        this.github = github;
    }

    public void mergeRepoTeam(RepoYamlDefinition repoTeam) throws IOException {
        logger.info("Merging repo team: {}", repoTeam.getTeamName());
        String teamName = repoTeam.getTeamName();
        String repoName = repoTeam.getRepoName();
        String orgName = repoTeam.getOrgName();
        List<DeveloperInfo> developers = repoTeam.getDevelopers();
        GHOrganization org = github.getOrganization(orgName);
        GHRepository repo = org.getRepository(repoName);

        Set<GHTeam> allTeams = repo.getTeams();
        Set<AdditionalTeamDefinition> additionalTeams = repoTeam.getAdditionalTeams();
        GHTeam matchingTeam = null;

        for(GHTeam team: allTeams){
            if(team.getName().equals(teamName)){
                matchingTeam = team;
            }else{
                // GitHub API for Java not support check team permissions for a project
                additionalTeams.add(new AdditionalTeamDefinition(team.getName(), null));
            }
        }

        if (matchingTeam != null) {
            Set<GHUser> members = matchingTeam.getMembers();
            for (GHUser member : members) {
                String githubUsername = member.getLogin();

                boolean found = false;
                for (DeveloperInfo developer : developers) {
                    if (developer.getLdapUsername().equals(githubUsername)) {
                        logger.info("Merging Github username for: {}", githubUsername);
                        // merge the GitHub username if username is same
                        developer.setGithubUsername(githubUsername);
                        found = true;
                        break;
                    }
                }

                // if not found, add a new developer to list
                if (!found) {
                    DeveloperInfo newDeveloper = new DeveloperInfo(null,null);
                    newDeveloper.setGithubUsername(githubUsername);
                    developers.add(newDeveloper);
                    logger.info("Adding new  Github developer in list: {}", githubUsername);
                }
            }

        }else{
            if(!developers.isEmpty()){
                logger.error("Team not found: {}", teamName);
            }
        }


    }
    public void mergeSpecialTeam(SpecialYamlDefinition specialTeam) throws IOException {
        logger.info("Merging special team: {}", specialTeam.getTeamName());
        String teamName = specialTeam.getTeamName();
        String orgName = specialTeam.getOrgName();
        List<DeveloperInfo> developers = specialTeam.getDevelopers();

        GHOrganization org = github.getOrganization(orgName);

        GHTeam team = org.getTeamByName(teamName);

        if(team != null) {
            Set<GHUser> members = team.getMembers();
            for (GHUser member : members) {
                String githubUsername = member.getLogin();

                boolean found = false;
                for (DeveloperInfo developer : developers) {
                    if (developer.getLdapUsername().equals(githubUsername)) {
                        logger.info("Merging github username for: {}", githubUsername);
                        // merge the GitHub username if username is same
                        developer.setGithubUsername(githubUsername);
                        found = true;
                        break;
                    }
                }

                // if not found, add a new developer to list
                if (!found) {
                    DeveloperInfo newDeveloper = new DeveloperInfo(null,null);
                    newDeveloper.setGithubUsername(githubUsername);
                    developers.add(newDeveloper);
                }
            }
        } else {
            if(!developers.isEmpty()){
                logger.error("Team not found: {}", teamName);
            }
        }

    }


}
