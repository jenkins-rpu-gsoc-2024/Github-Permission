# GitHub-Permission

## About

`GitHub-Permission` is a tool designed to automate the management of GitHub permissions within the Jenkinsci organization's Repository Permission Updater (RPU). Operating independently, yet complementing the RPU, this tool leverages an "as-code" approach with YAML configurations to dynamically define and update permissions, ensuring a streamlined and efficient management process.

## Links
You can find the proposal for this project [here](https://docs.google.com/document/d/17QfpBgqGglhTTS_VLv5PKxXn-UYLlTq14GPwYJZ79Zg/edit?usp=sharing)

The Pull Request can be found [here](https://github.com/jenkins-infra/repository-permissions-updater/pull/3998)

## Features

1. **Automated Team Updates**: Automatically updates teams when a pull request that modifies a YAML file is merged.
2. **Consistency Checks**: Ensures team members listed in the YAML file correspond with actual GitHub developers.

## Prerequisites

- Java JDK 17 or higher
- Maven 3.6.3 or higher

## Repository Configuration

### Branch Setup
Ensure that your work is conducted on the appropriate branch, such as the "action" branch for updates related to action implementations.

### Secrets Management
Securely manage sensitive data like API keys or tokens by setting them as secrets:
- Navigate to your repository on GitHub.
- Go to **Settings** > **Secrets** > **Actions**.
- Click **New repository secret** and add your secret, e.g., `ORG_TOKEN`.

## Local Setup

### Configuration and Running

The project is configured to generate two distinct JAR files from the same codebase without altering the existing RPU system:

1. **Update `pom.xml`**:
   - Adjust the `pom.xml` file to guide Maven on how to separately build each JAR with specific requirements.

2. **Set up Assembly Files**:
   - Utilize assembly definitions to specify what to include and exclude in each JAR, ensuring they are equipped with the necessary resources to function independently.

3. **Run the Project**:
   ```bash
   mvn clean package  # Builds the project and generates JAR files.
   java -jar target/jar1.jar  # Represents the original RPU JAR.
   java -jar target/jar2.jar  # Represents the GitHub-Permission project, triggered by GitHub API separately from RPU.
