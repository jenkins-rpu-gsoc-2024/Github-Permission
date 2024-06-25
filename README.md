# Github-Permission

## About

## Managing Permissions

## Installation

### Jenkins Pipeline Setting

  SCM Type: Git
  
  Repository URL: Input the URL of your GitHub repository.
  
  Branch: Typically */main.
  
  Jenkinsfile Path: Path to your Jenkinsfile if not at the root.
  
  Build Triggers: "GitHub hook trigger for GITScm polling"
  
  Credentials: Two credentials are necessary. One is an SSH Username with private key, another is a Secret text with ID is "github-token"
  
