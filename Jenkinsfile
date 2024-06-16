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
                    // Fetch the changes with details
                    def gitDiffDetails = sh(script: "git diff HEAD~1 -- permissions/*.yaml", returnStdout: true).trim()
                    echo "Detailed diff:\n${gitDiffDetails}"

                    // Use a regex to find files with 'github_team' changes
                    def pattern = ~/permissions\/(\S+\.yaml)(?=[\s\S]*?\bgithub_team\b)/
                    def matcher = gitDiffDetails =~ pattern
                    if (matcher) {
                        echo 'Changes related to github_team detected. Processing...'
                        matcher.each {
                            def fileName = it[1]
                            echo "Updating GitHub team for file: ${fileName}"
                            // Pass the filename to the Java/Groovy program
                            sh "java -jar /need/to/instead.jar '${fileName}'"
                        }
                    } else {
                        echo 'No changes to github_team detected in YAML files.'
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
