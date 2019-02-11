pipeline {
  agent any
  post {
    failure {
      emailext (
        subject: "'${env.JOB_NAME}${env.BUILD_DISPLAY_NAME}'构建失败",
        body: """
        <!DOCTYPE html><html lang=en><head><meta charset=UTF-8><title>Title</title></head><body><div style=overflow-x:auto><table style=border-spacing:0 width="100%"><thead><tr style="background-color:#00cc00"><th style="padding: 8px;text-align: center;vertical-align: middle;border: 1px solid #ccc" colspan=2>项目${env.JOB_NAME}构建结果</th><tbody><tr style="background-color: #ffaabb"><td style="padding: 8px;border: 1px solid #ccc">构建编号<td style="padding: 8px;border: 1px solid #ccc">${env.BUILD_DISPLAY_NAME}<tr><td style="padding: 8px;border: 1px solid #ccc">构建结果<td style="padding: 8px;border: 1px solid #ccc">FAIL<tr style="background-color: #ffaabb"><td style="padding: 8px;border: 1px solid #ccc">构建分支<td style="padding: 8px;border: 1px solid #ccc">${env.BRANCH_NAME}<tr><td style="padding: 8px;border: 1px solid #ccc">构建详情<td style="padding: 8px;border: 1px solid #ccc">${env.JENKINS_URL}blue/organizations/jenkins/sip/detail/${env.BRANCH_NAME}/${env.BUILD_NUMBER}/pipeline<tr style="background-color: #ffaabb"><td style="padding: 8px;border: 1px solid #ccc">Console<td style="padding: 8px;border: 1px solid #ccc">${env.BUILD_URL}console<tr><td style="padding: 8px;border: 1px solid #ccc">执行节点<td style="padding: 8px;border: 1px solid #ccc">${env.NODE_NAME}<tr style="background-color: #ffaabb"><td style="padding: 8px;border: 1px solid #ccc">提交HASH<td style="padding: 8px;border: 1px solid #ccc">${env.GIT_COMMIT}<tr><td style="padding: 8px;border: 1px solid #ccc">仓库地址<td style="padding: 8px;border: 1px solid #ccc">${env.GIT_URL}</table></div>
        """,
        to: "basicfu@gmail.com",
        recipientProviders: [[$class: 'DevelopersRecipientProvider']]
      )
    }
    success {
      emailext (
        subject: "'${env.JOB_NAME}${env.BUILD_DISPLAY_NAME}'构建成功",
        body: """
        <!DOCTYPE html><html lang=en><head><meta charset=UTF-8><title>Title</title></head><body><div style=overflow-x:auto><table style=border-spacing:0 width="100%"><thead><tr style="background-color:#00cc00"><th style="padding: 8px;text-align: center;vertical-align: middle;border: 1px solid #ccc" colspan=2>项目${env.JOB_NAME}构建结果</th><tbody><tr style="background-color: #ffaabb"><td style="padding: 8px;border: 1px solid #ccc">构建编号<td style="padding: 8px;border: 1px solid #ccc">${env.BUILD_DISPLAY_NAME}<tr><td style="padding: 8px;border: 1px solid #ccc">构建结果<td style="padding: 8px;border: 1px solid #ccc">SUCCESS<tr style="background-color: #ffaabb"><td style="padding: 8px;border: 1px solid #ccc">构建分支<td style="padding: 8px;border: 1px solid #ccc">${env.BRANCH_NAME}<tr><td style="padding: 8px;border: 1px solid #ccc">构建详情<td style="padding: 8px;border: 1px solid #ccc">${env.JENKINS_URL}blue/organizations/jenkins/sip/detail/${env.BRANCH_NAME}/${env.BUILD_NUMBER}/pipeline<tr style="background-color: #ffaabb"><td style="padding: 8px;border: 1px solid #ccc">Console<td style="padding: 8px;border: 1px solid #ccc">${env.BUILD_URL}console<tr><td style="padding: 8px;border: 1px solid #ccc">执行节点<td style="padding: 8px;border: 1px solid #ccc">${env.NODE_NAME}<tr style="background-color: #ffaabb"><td style="padding: 8px;border: 1px solid #ccc">提交HASH<td style="padding: 8px;border: 1px solid #ccc">${env.GIT_COMMIT}<tr><td style="padding: 8px;border: 1px solid #ccc">仓库地址<td style="padding: 8px;border: 1px solid #ccc">${env.GIT_URL}</table></div>
        """,
        to: "basicfu@gmail.com",
        recipientProviders: [[$class: 'DevelopersRecipientProvider']]
      )
    }
  }
  stages {
    stage('aliyun login'){
      steps{
        sh 'docker login -u ${ALIYUN_DOCKER_REPO_USR} -p ${ALIYUN_DOCKER_REPO_PSW} registry-vpc.cn-beijing.aliyuncs.com'
      }
    }
    stage('dev') {
      when {
        branch 'master'
      }
      steps {
        sh './gradlew -x test :sip-eureka:bootJar :sip-getway:bootJar :sip-base:bootJar :sip-notify:bootJar :sip-api:bootJar'
        sh 'docker build -t registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-docs:${IMAGE_VERSION} sip-docs/docs'
        sh 'docker build -t registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-eureka:${IMAGE_VERSION} sip-eureka'
        sh 'docker build -t registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-getway:${IMAGE_VERSION} sip-getway'
        sh 'docker build -t registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-base:${IMAGE_VERSION} sip-base'
        sh 'docker build -t registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-notify:${IMAGE_VERSION} sip-notify'
        sh 'docker build -t registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-api:${IMAGE_VERSION} sip-api'
        sh 'docker push registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-docs:${IMAGE_VERSION}'
        sh 'docker push registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-eureka:${IMAGE_VERSION}'
        sh 'docker push registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-getway:${IMAGE_VERSION}'
        sh 'docker push registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-base:${IMAGE_VERSION}'
        sh 'docker push registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-notify:${IMAGE_VERSION}'
        sh 'docker push registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-api:${IMAGE_VERSION}'
	      sh 'curl -d "name=sip-dev&set=docs.tag=${IMAGE_VERSION}&set=eureka.tag=${IMAGE_VERSION}&set=getway.tag=${IMAGE_VERSION}&set=base.tag=${IMAGE_VERSION}&set=notify.tag=${IMAGE_VERSION}&set=api.tag=${IMAGE_VERSION}" ${KUBE_TRIGGER_URL}'
      }
    }
    stage('test') {
	    when {
        not { branch 'master' }
      }
	    parallel {
	      stage('all build') {
	        steps {
	          sh './gradlew -x test :sip-eureka:build :sip-getway:build :sip-base:build :sip-dict:build :sip-permission:build :sip-notify:build'
	        }
	      }
	    }
	  }
    stage('production') {
      when {
        branch 'production'
      }
      parallel {
        stage('sip-eureka') {
          steps {
            sh './gradlew :sip-eureka:build -x test'
            sh 'docker build -t registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-eureka sip-eureka'
            sh 'docker push registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-eureka'
          }
        }
        stage('sip-getway') {
          steps {
            sh './gradlew :sip-getway:build -x test'
            sh 'docker build -t registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-getway sip-getway'
            sh 'docker push registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-getway'
          }
        }
      }
    }
  }
  environment {
    IMAGE_VERSION="${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
    ALIYUN_DOCKER_REPO = credentials('aliyun-docker-repo')
    KUBE_TRIGGER_URL = credentials('kube-trigger-url')
  }
}
