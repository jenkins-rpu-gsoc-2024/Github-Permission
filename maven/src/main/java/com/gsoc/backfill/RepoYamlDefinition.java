package com.gsoc.backfill;

import java.util.List;
import java.util.Set;

public class RepoYamlDefinition {

    private String repoName;
    private String orgName;
    private String teamName;
    private List<DeveloperInfo> developers;

    private Set<AdditionalTeamDefinition> additionalTeams;

    public RepoYamlDefinition(String repoName, String orgName, List<DeveloperInfo> developers, Set<AdditionalTeamDefinition> additionalTeams) {
        this.repoName = repoName;
        this.orgName = orgName;
        this.teamName = repoName + " Developers";
        this.developers = developers;
        this.additionalTeams = additionalTeams;
    }
    
    public RepoYamlDefinition() {
    }

    public String getRepoName() {
        return repoName;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getTeamName() {
        return teamName;
    }

    public List<DeveloperInfo> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<DeveloperInfo> developers) {
    }
    public Set<AdditionalTeamDefinition> getAdditionalTeams() {
        return additionalTeams;
    }

}