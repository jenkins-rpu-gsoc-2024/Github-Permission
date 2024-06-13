pipeline {
    agent any

    environment {
        // Define whether this is a dry run or not as an environment variable
        DRY_RUN = 'true'
    }

    stages {
        stage('Update GitHub Permissions') {
            steps {
                script {
                    // Check for file changes from the last commit
                    def changedFiles = sh(script: "git diff --name-only HEAD~1", returnStdout: true).trim()
                    def hasChanges = changedFiles != ''

                    echo "Changed Files: ${changed)Files}"

                    // Decide on actions based on whether it is a dry run
                    if (env.DRY_RUN == 'true') {
                        echo "Dry run mode: Detected changes: ${changedFiles}"
                        if (!hasChanges) {
                            echo 'No changes detected during the dry run'
                        }
                    } else {
                        // Perform the actual actions
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Post-build actions can be specified here.'
        }
    }
}
