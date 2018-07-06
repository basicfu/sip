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
      sh 'curl https://cs.console.aliyun.com/hook/trigger?triggerUrl=Y2ViNDdiMjYwNjlkMDQ3Y2U5YzcyNzQ1MTk3NzZjZTUzfHNpcC1kZXZ8cmVkZXBsb3l8MWFmNGlyMHR0cGtxZHw=&secret=4232685778416d346743677755445168e059741cb712b406bafc7ddf83b7c7c9'
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
    stage('aliyun login') {
      steps {
        sh 'docker login -u ${ALIYUN_DOCKER_REPO_USR} -p ${ALIYUN_DOCKER_REPO_PSW} registry-vpc.cn-beijing.aliyuncs.com'
      }
    }
    stage('develop') {
      when {
        branch 'master'
      }
      parallel {
        stage('sip-docs') {
          steps {
            sh 'docker build -t registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-docs sip-docs/docs'
            sh 'docker push registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-docs'
          }
        }
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
        stage('sip-base') {
          steps {
            sh './gradlew :sip-base:build -x test'
            sh 'docker build -t registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-base sip-base'
            sh 'docker push registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-base'
          }
        }
        stage('sip-dict') {
          steps {
            sh './gradlew :sip-dict:build -x test'
            sh 'docker build -t registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-dict sip-dict'
            sh 'docker push registry-vpc.cn-beijing.aliyuncs.com/basicfu/sip-dict'
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
    ALIYUN_DOCKER_REPO = credentials('aliyun-docker-repo')
  }
}
