def testProjectBranch='master'
def testProjectUrl='http://github.com/smatytsin/sber-test-cucumber.git'
def eMailList='susclick@gmail.com'

pipeline {
    agent any
    // требуется предварительно настроить дженкинс для этого блока
    tools {
       jdk 'jdk1.8.0_201'
       maven 'apache-maven-3.6.3'
    }

    parameters{
        string defaultValue: '', description: 'remote web driver url (оставить пустым, если не требуется грид)', name: 'remoteWevDriverUrl', trim: true
        string defaultValue: '', description: 'parallel threads count per cpu for parallel run (оставить пустым, если не требуется параллельный запуск)', name: 'parallelThreads', trim: true
    }
    stages {
       stage('Checkout project') {
           steps{
              git branch: "${testProjectBranch}",
                  url: "${testProjectUrl}"
           }
       }
       stage('Test') {
           steps{
              script {
                 def threadsOptions=''
                 if (params.parallelThreads!='') {
                    threadsOptions="-Dthreads.per.cpu.count=${params.parallelThreads}"
                 }
                 bat "mvn -B -ntp clean test ${threadsOptions} ${remoteWevDriverUrl}"
              }
           }
       }
    }
    post {
        always {
            // нужно предварительно плагин аллюра в дженкинс добавить
            allure results: [[path: 'target/allure-results']]
            // нужно поставить junit jenkins plugin, чтобы считать junit репорты для дальнейшего использования результатов внутри шаблона нотификаций
            junit 'target/surefire-reports/TEST*.xml'
            // предварительно положить шаблон нотификации в JenkinsHome/email-templates
            emailext body: '${SCRIPT, template="at-cucumber-eml-tmpl.groovy"}',
                    subject: "${env.JOB_NAME} - Запуск автотестов # ${env.BUILD_NUMBER}",
                    mimeType: 'text/html', 
                    to: "${eMailList}",
                    attachLog: true
            deleteDir()
        }
    }
}