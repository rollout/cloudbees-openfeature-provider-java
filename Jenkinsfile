pipeline {
  agent any
  tools {
      maven 'Maven 3.8.6'
      jdk 'jdk8'
  }

  options {
    timeout(time: 5, unit: 'MINUTES')
  }
  stages {
    stage ('Build') {
      steps {
        sh 'mvn -B -Dmaven.test.failure.ignore=true verify'
      }
      post {
        success {
          junit 'target/surefire-reports/**/*.xml'
        }
      }
    } // end stage
  } // end stages
} // end pipeline
