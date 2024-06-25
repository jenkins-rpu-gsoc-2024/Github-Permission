pipeline {
    agent any

    stages {
        stage('Initialization') {
            steps {
                echo 'Starting the pipeline...'
            }
        }
        stage('Check for Team Changes') {
            steps {
                script {
                    // detect changes in permissions/*.YAML
                    def gitDiffFiles = sh(script: "git diff --name-only HEAD~1 permissions/*.YAML", returnStdout: true).trim()
                    if (gitDiffFiles) {
                        echo 'Changes in permissions detected.'
                        // further check whether involves github_team
                        gitDiffFiles.tokenize('\n').each { file ->
                            def gitDiffDetails = sh(script: "git diff HEAD~1 -- '${file}'", returnStdout: true).trim()
                            def changesInGitHubTeam = gitDiffDetails.contains('github_team')
                            def changesInDevelopers = gitDiffDetails.contains('developers')

                            if (changesInGitHubTeam || changesInDevelopers) {
                                echo "Updating the team in: ${file}"
                                
                                withCredentials([
                                    string(credentialsId: 'github-token', variable: 'PERSONAL_TOKEN')
                                ]) {
                                    sh "java -jar target/githubpermission-1.0-SNAPSHOT-jar-with-dependencies.jar '${file}'"
                                }

                            } else {
                                echo "No changes to the team in: ${file}"
                            }
                        }
                    } else {
                        echo 'No changes to permissions files detected.'
                    }
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Building the project...'
            }
        }
        stage('Test') {
            steps {
                echo 'Running tests...'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying application...'
            }
        }
    }
    post {
        always {
            echo 'This is the post-build step to clean up or finalize things.'
        }
        success {
            echo 'Build was successful!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
