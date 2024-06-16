// Same as RPU at most part, but added a new field for team name
package com.gsoc;

public class TeamDefinition {

    private String name = "";
    private String TeamName = ""; // Lauren part - team name
    private String[] developers = new String[0];

    public TeamDefinition(String name, String TeamName) {
        this.name = name;
        this.TeamName = TeamName; // Lauren part - team name
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getDevelopers() {
        return developers.clone();
    }

    public void setDevelopers(String[] developers) {
        this.developers = developers.clone();
    }

    // Lauren part - team name
    public String getTeamName(){
        return TeamName;
    }

    public void setTeamName(String TeamName){
        this.TeamName = TeamName;
    }
}
