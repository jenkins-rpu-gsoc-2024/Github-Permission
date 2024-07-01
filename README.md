# Github-Permission

## About

This project is a tool designed to support the Jenkinsci organization's Repository Permission Updater (RPU) by automating the management of GitHub permissions. While it is created to complement the RPU, it operates independently. The tool uses an "as-code" approach, employing YAML configurations to dynamically define and update permissions, facilitating a clean and efficient management process.

## Features

1. Automatically update teams when a pull request that modifies the YAML file is merged.
2. Ensuring that the team members correspond with the developers in the YAML file.

## How to run it locally (MacOS)

Open "terminal" and go to the repo file
run
  mvn clean package
run 
  java -jar target/jar1.jar 
it would be return "This is a test."
run
  java -jar target/jar2.jar permissions/MainProject.YAML
it would be return "No developers in the team. Team not created."

### What happen and how to modify

assembly folder 2xml file
pom setting two id and package and name it

