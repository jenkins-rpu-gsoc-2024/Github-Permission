package com.gsoc.backfill;

import java.util.List;

public class SpecialYamlDefinition {
    private String orgName;
    private String teamName;
    private List<DeveloperInfo> developers;

    private static final String DEFAULT_ORG_NAME = "jenkins-rpu-gsoc-2024";

    public SpecialYamlDefinition(String teamName, List<DeveloperInfo> developers) {
        this.orgName = DEFAULT_ORG_NAME;
        this.teamName = teamName;
        this.developers = developers;
    }

    public SpecialYamlDefinition() {
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


}
