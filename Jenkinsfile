pipeline {
    agent any

    stages {
        stage('Initialization') {
            steps {
                echo 'Starting the pipeline...'
            }
        }
        stage('Check for Changes') {
            steps {
                script {
                    // fetch the changes between the last and the current commit
                    def gitDiff = sh(script: "git diff HEAD~1 --unified=0", returnStdout: true).trim()
                    echo "Changes detected:\n${gitDiff}"

                    // check if there are any changes
                    if (gitDiff == '') {
                        echo 'No changes detected between the last and the current commit.'
                    } else {
                        // other case
                        echo 'Analyzing changes...'
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