// Same as RPU at most part, but added a new field for team name
package com.gsoc;

import java.util.List;

public class GithubTeamDefinition {

    private String RepoName = "";
    private String TeamName = "";
    private List<String> developers;

    public GithubTeamDefinition(String RepoName, String TeamName, List<String> developers) {
        this.RepoName = RepoName;
        this.TeamName = TeamName;
        this.developers = developers;
    }

    public GithubTeamDefinition() {
    }

    public String getName() {
        return RepoName;
    }

    public void setName(String name) {
        this.RepoName = name;
    }

    public List<String> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<String> developers) {
        this.developers = developers;
    }

    // Lauren part - team name
    public String getTeamName(){
        return TeamName;
    }

    public void setTeamName(String TeamName){
        this.TeamName = TeamName;
    }
}
