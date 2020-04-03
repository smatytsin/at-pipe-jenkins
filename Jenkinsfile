def testProjectBranch='master'
def testProjectUrl='http://github.com/smatytsin/sber-test-cucumber.git'
def eMailList='susclick@gmail.com'

pipeline {
    agent any
    // ��������� �������������� ��������� �������� ��� ����� �����
    tools {
       jdk 'jdk1.8.0_201'
       maven 'apache-maven-3.6.3'
    }

    parameters{
        string defaultValue: '', description: 'remote web driver url (�������� ������, ���� �� ��������� ����)', name: 'remoteWevDriverUrl', trim: true
        string defaultValue: '', description: 'parallel threads count per cpu for parallel run (�������� ������, ���� �� ��������� ������������ ������)', name: 'parallelThreads', trim: true
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
            // ����� �������������� ������ ������ � �������� ��������
            allure results: [[path: 'target/allure-results']]
            // ����� ��������� junit jenkins plugin, ����� ������� junit ������� ��� ����������� ������������� ����������� ������ ������� �����������
            junit 'target/surefire-reports/TEST*.xml'
            // �������������� �������� ������ ����������� � JenkinsHome/email-templates
            emailext body: '${SCRIPT, template="at-cucumber-eml-tmpl.groovy"}',
                    subject: "${env.JOB_NAME} - ������ ���������� # ${env.BUILD_NUMBER}",
                    mimeType: 'text/html', 
                    to: "${eMailList}",
                    attachLog: true
            deleteDir()
        }
    }
}