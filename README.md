# Github-Permission

## About

This project is a tool designed to support the Jenkinsci organization's Repository Permission Updater (RPU) by automating the management of GitHub permissions. While it is created to complement the RPU, it operates independently. The tool uses an "as-code" approach, employing YAML configurations to dynamically define and update permissions, facilitating a clean and efficient management process.

## Features

1. Automatically update teams when a pull request that modifies the YAML file is merged.
2. Ensuring that the team members correspond with the developers in the YAML file.

## Managing Permissions

## Installation

### Docker
  1. Install Docker in your laptop
  2. Execute the following Docker command to pull and run the official Jenkins LTS image, mapping port 8080 for web access
    docker run -p 8080:8080 -p 50000:50000 --name jenkins -d jenkins/jenkins:lts
  3. Open a browser and navigate to http://localhost:8080 to start the Jenkins setup
  4. Keep the Docker container running while using this project to maintain access to Jenkins.


### Jenkins Pipeline Setting

  SCM Type: Git
  
  Repository URL: Input the URL of your GitHub repository.
  
  Branch: Typically */main.
  
  Jenkinsfile Path: Path to your Jenkinsfile if not at the root.
  
  Build Triggers: "GitHub hook trigger for GITScm polling"
  
  Credentials: Two credentials are necessary. One is an SSH Username with the private key, another is a Secret text with an ID "github-token"

### ngrok
  1. Install ngrok
  2. To expose your Jenkins server to the internet, run code below in your terminal
    ngrok http 8080 (Replace 8080 with the port used in your Docker command if different.)
  3. This command provides a public URL (e.g., https://12345678.ngrok-free.app) that forwards to http://localhost:8080.
  
### GitHub webhook Setting
  1. Go to your GitHub repository
  2. Settings > Webhooks > Add webhook
  3. Payload URL: enter the ngrok URL followed by /github-webhook/ (e.g., http://12345678.ngrok.io/github-webhook/).
  4. Content type: choose application/json
  5. Which events would you like to trigger this webhook? choose Just the push event.
  6. Click Add webhook to save your settings and activate the webhook.
  
