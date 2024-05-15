pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                bat 'mvn -B clean package'
            }
        }
        stage('Test') {
            steps {
                // 运行测试并生成 surefire 报告
                bat 'mvn test --fail-never'
                bat 'mvn surefire-report:report'
            }
            post {
                always {
                    // 把 surefire 报告归档
                    archiveArtifacts artifacts: '**/target/site/surefire-report.html', fingerprint: true
                }
            }
        }
        stage('Generate Javadoc') {
            steps {
                // 生成 javadoc
                bat 'mvn javadoc:javadoc --fail-never'
                
            }
            post {
                always {
                    // 把 javadoc JAR 文件归档
                    archiveArtifacts artifacts: '**/target/site/apidocs/index.html', fingerprint: true
                }
            }
        }
        stage('pmd') {
            steps {
                bat 'mvn pmd:pmd'
            }
        }
    }

    post {
        always {
            // 归档网站和 JAR/WAR 文件
            archiveArtifacts artifacts: '**/target/site/**', fingerprint: true
            archiveArtifacts artifacts: '**/target/**/*.jar', fingerprint: true
            archiveArtifacts artifacts: '**/target/**/*.war', fingerprint: true
        }
    }
}
