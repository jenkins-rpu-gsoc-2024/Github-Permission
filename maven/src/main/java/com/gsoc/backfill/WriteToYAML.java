package com.gsoc.backfill;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.nodes.Node;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;




public class WriteToYAML {

    public static void writeRepoTeamToYAML(RepoYamlDefinition repoTeam, String filePath) throws IOException {

        
        // Yaml yaml = new Yaml();
        // Map<String, Object> existingData;

        // // 1. Read the existing YAML file content
        // try (FileInputStream inputStream = new FileInputStream(filePath)) {
        //     existingData = yaml.load(inputStream);
        // }

        // if (existingData == null) {
        //     existingData = new LinkedHashMap<>();
        // }

        // ensureQuotedStrings(existingData);

        // // 2. Update the developers field
        // List<Map<String, String>> developerDetails = new ArrayList<>();
        // for (DeveloperInfo developer : repoTeam.getDevelopers()) {
        //     Map<String, String> devMap = new LinkedHashMap<>();  // Use LinkedHashMap to maintain order
        //     devMap.put("ldap", developer.getLdapUsername() != null ? developer.getLdapUsername() : "");  // Handle empty values
        //     devMap.put("github", developer.getGithubUsername() != null ? developer.getGithubUsername() : "");  // Handle empty values
        //     developerDetails.add(devMap);
        // }

        // // Preserve other fields, only overwrite the developers section
        // existingData.put("developers", developerDetails);

        // // Adding new repository team and additional GitHub teams
        // existingData.put("repository_team", repoTeam.getTeamName());
        // List<Map<String, String>> githubTeams = new ArrayList<>();
        // for (AdditionalTeamDefinition team : repoTeam.getAdditionalTeams()) {
        //     Map<String, String> teamDetails = new LinkedHashMap<>();
        //     teamDetails.put("name", team.getName());
        //     teamDetails.put("role", "");
        //     githubTeams.add(teamDetails);
        // }
        // existingData.put("additional_github_teams", githubTeams);

        

        // // 3. Set up YAML serialization options
        // DumperOptions options = new DumperOptions();
        // options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        // options.setPrettyFlow(true);
        // options.setIndent(4);
        // options.setIndicatorIndent(2);

        // Representer representer = new Representer(options);
        // yaml = new Yaml(representer, options);

        // // 4. Write the updated content back to the YAML file
        // try (FileWriter writer = new FileWriter(filePath)) {
        //     yaml.dump(existingData, writer);
        // }
    }

    public static void writeSpecialTeamToYAML(SpecialYamlDefinition specialTeam, String filePath) throws IOException {
    
        
        Yaml yaml = new Yaml();
        Map<String, Object> existingData;
        
        // 1. Read the existing YAML file content
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            existingData = yaml.load(inputStream);
        }
    
        if (existingData == null) {
            existingData = new LinkedHashMap<>();
        }
        
        ensureQuotedStrings(existingData);
        
        // 2. Update the developers field
        List<Map<String, Object>> developerDetails = new ArrayList<>();
        for (DeveloperInfo developer : specialTeam.getDevelopers()) {
            Map<String, Object> devMap = new LinkedHashMap<>();  // Use LinkedHashMap to maintain order
            devMap.put("ldap", developer.getLdapUsername() != null ? new QuotedString(developer.getLdapUsername())  : new QuotedString(""));
            devMap.put("github", developer.getGithubUsername() != null ? new QuotedString(developer.getGithubUsername()) : new QuotedString("")); 
            developerDetails.add(devMap);
        }
    
        existingData.put("developers", developerDetails);
    
        // 3. Set up YAML serialization options

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(4); 
        options.setIndicatorIndent(2);
    
        MyRepresenter representer = new MyRepresenter(options);

        representer.getPropertyUtils().setSkipMissingProperties(true);
    

        Yaml newYaml = new Yaml(representer, options);
    
        // 4. Write the updated content back to the YAML file
        try (FileWriter writer = new FileWriter(filePath)) {
            newYaml.dump(existingData, writer);
        }
    } 

    private static void ensureQuotedStrings(Map<String, Object> data) {

        
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            if ("cd".equals(key) || "issues".equals(key)) {
                continue;
            }
            if (value instanceof String) {
                data.put(key, new QuotedString((String) value));
            }
        }
        
    }

}

