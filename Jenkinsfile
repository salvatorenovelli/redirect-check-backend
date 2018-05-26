node {
    def project = 'redirect-check-180020'
    def appName = 'redirect-check-backend'

    def svcName = "${appName}-service"

    def imageTag = "gcr.io/${project}/${appName}:${env.BRANCH_NAME}.${env.BUILD_NUMBER}"

    checkout scm

    stage 'Build project'
    sh("./mvnw package")

    stage 'Build image'
    sh("docker build docker -t ${imageTag}")

}
