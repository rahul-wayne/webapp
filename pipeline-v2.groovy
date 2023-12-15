pipeline {
    
    agent none

    // Variables declared here
    environment {
        dockerImageAmd = ''
        registry = 'blackpearl001/webapp:v1.2.4'
        registryCredential = 'docker_hub' //credential id
    }
    
    stages {
        
        // Git checkout
        stage('Git checkout') {
            agent {
                label 'master'
            }
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'git_pat', url: 'https://github.com/rahul-wayne/webapp.git']])
            }
        }

        // Building Image
        stage('Build Docker Image') {
            agent {
                label 'master'
            }
            steps {
                script {
                    dockerImageAmd = docker.build registry
                }
            }
        }
        
        // Scan Docker Image using trivy
        stage('Scanning Docker Image using Trivy'){
            agent {
                label 'master'
            }
            steps {
                script{
                    sh "trivy image ${registry} --scanners vuln"
                }
            }
        }
        
        // Upload docker image to Docker Hub
        stage ('Uploading docker image') {
            agent {
                label 'master'
            }
            steps {
                script {
                    docker.withRegistry('', registryCredential ){
                        dockerImageAmd.push()
                    }
                }
            }
        }

        // Deploying Docker Image to K8s
        stage ('Deploying to OKI cluster') {
            agent {
                label 'master'
            }
            steps {
                script {
                    sh "chmod +x helm_upgrade.sh"
                    sh "sh helm_upgrade.sh"

                }
            }
        }

    }

    post {
    always {
         //Clean up Git cloned directory on master
        node('master') {
            script {
                deleteDir()
                }
            }
        }
    }
}