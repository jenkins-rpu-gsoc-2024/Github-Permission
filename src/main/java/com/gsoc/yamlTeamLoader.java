package com.gsoc;
import org.yaml.snakeyaml.Yaml;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class yamlTeamLoader {

    public static GithubTeamDefinition loadTeam(String filePath) {
        Yaml yaml = new Yaml();

        try (InputStream inputStream = new FileInputStream(filePath)) {
            Map<String, Object> data = yaml.load(inputStream);
            String repoPath = "";
            String teamName = "";
            List<String> developers = null;
            
            if (data.containsKey("github")) {
                repoPath = (String) data.get("github");
            }
            
            if (data.containsKey("github_team")) {
                teamName = (String) data.get("github_team");
            }

            
            if (data.containsKey("developers")){
                developers = (List<String>) data.get("developers");
            }

            return new GithubTeamDefinition(repoPath,teamName,developers);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load: " + filePath, e);
        }
    }
}
