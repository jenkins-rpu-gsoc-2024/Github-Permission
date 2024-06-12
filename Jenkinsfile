pipeline {
    agent any
    stages {
        stage('Fetch Changes') {
            steps {
                script {

                    // Fetch differences between the last commit and the current commit
                    def changes = sh(script: "git diff HEAD HEAD~ --name-status", returnStdout: true).trim()
                    println("Changes: \n${changes}")

                    // Extract lines that were added in the current commit
                    def addedLines = sh(script: "git diff HEAD HEAD~ | grep '^+'", returnStdout: true).trim()
                    
                    // Extract lines that were removed in the current commit
                    def removedLines = sh(script: "git diff HEAD HEAD~ | grep '^-'", returnStdout: true).trim()
                    
                    echo "Added lines: \n${addedLines}"
                    echo "Removed lines: \n${removedLines}"
                }
            }
        }
    }
}