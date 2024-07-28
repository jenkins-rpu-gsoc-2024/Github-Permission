package com.gsoc.teamsync;

import java.util.Set;

public class GithubTeamDefinition {

    private String RepoName;
    private String TeamName;
    private Set<String> developers;

    public GithubTeamDefinition(String RepoName, String TeamName, Set<String> developers) {
        this.RepoName = RepoName;
        this.TeamName = TeamName;
        this.developers = developers;
    }

    public GithubTeamDefinition() {
    }

    public String getName() {
        return RepoName;
    }

    public String getTeamName(){
        return TeamName;
    }

    public Set<String> getDevelopers() {
        return developers;
    }
}
