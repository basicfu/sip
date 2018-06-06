pipeline {
  agent any
  stages {
    stage('gradle build') {
      steps {
        sh './gradlew :sip-eureka:build'
      }
    }
    stage('docker build') {
      steps {
        sh 'docker build -t registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-eureka sip-eureka/'
      }
    }
    stage('docker push') {
      steps {
        sh '''docker login -u ${ALIYUN_DOCKER_REPO_USR} -p ${ALIYUN_DOCKER_REPO_PSW} registry-vpc.cn-beijing.aliyuncs.com
docker push registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-eureka'''
      }
    }
  }
  environment {
    ALIYUN_DOCKER_REPO = credentials('aliyun-docker-repo')
  }
}