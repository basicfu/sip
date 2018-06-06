pipeline {
  agent any
  stages {
    stage('gradle build') {
      parallel {
        stage('sip-eureka') {
          steps {
            sh './gradlew :sip-eureka:build'
          }
        }
        stage('sip-getway') {
          steps {
            sh './gradlew :wutong-getway:build'
          }
        }
      }
    }
    stage('docker build') {
      parallel {
        stage('sip-eureka') {
          steps {
            sh 'docker build -t registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-eureka sip-eureka'
          }
        }
        stage('sip-getway') {
          steps {
            sh 'docker build -t registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-getway sip-getway'
          }
        }
      }
    }
    stage('docker push') {
      parallel {
        stage('sip-eureka') {
          steps {
            sh '''docker login -u ${ALIYUN_DOCKER_REPO_USR} -p ${ALIYUN_DOCKER_REPO_PSW} registry-vpc.cn-beijing.aliyuncs.com
            docker push registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-eureka'''
          }
        }
        stage('sip-getway') {
          steps {
            sh '''docker login -u ${ALIYUN_DOCKER_REPO_USR} -p ${ALIYUN_DOCKER_REPO_PSW} registry-vpc.cn-beijing.aliyuncs.com
            docker push registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-getway'''
          }
        }
      }
    }
  }
  environment {
    ALIYUN_DOCKER_REPO = credentials('aliyun-docker-repo')
  }
}