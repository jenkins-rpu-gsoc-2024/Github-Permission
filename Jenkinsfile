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
                    // detect changes in permissions/*.yaml
                    def gitDiffFiles = sh(script: "git diff --name-only HEAD~1 permissions/*.yaml", returnStdout: true).trim()
                    if (gitDiffFiles) {
                        echo 'Changes in permissions/*.yaml detected.'
                        // further check whether involves github_team
                        gitDiffFiles.tokenize('\n').each { file ->
                            def gitDiffDetails = sh(script: "git diff HEAD~1 -- '${file}'", returnStdout: true).trim()
                            if (gitDiffDetails.contains('github_team')) {
                                echo "Updating GitHub team for file: ${file}"
                                sh "java -jar target/githubpermission.jar '${file}'"
                            } else {
                                echo "No changes to github_team detected in file: ${file}"
                            }
                        }
                    } else {
                        echo 'No changes to permissions/*.yaml files detected.'
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
