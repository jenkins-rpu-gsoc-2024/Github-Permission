# Github-Permission

## About

## Features

1. Automatically update teams when a pull request that modifies the YAML file is merged.
2. Ensuring that the team members correspond with the developers in the YAML file.

## Managing Permissions

## Installation

### Jenkins Pipeline Setting

  SCM Type: Git
  
  Repository URL: Input the URL of your GitHub repository.
  
  Branch: Typically */main.
  
  Jenkinsfile Path: Path to your Jenkinsfile if not at the root.
  
  Build Triggers: "GitHub hook trigger for GITScm polling"
  
  Credentials: Two credentials are necessary. One is an SSH Username with the private key, another is a Secret text with an ID "github-token"
  
