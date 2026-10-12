def cicdRepo = 'https://github.com/ravishaggarwal/app-k8-deployment-applicationPlaformCICD.git'
def branch = 'main'

pipelineJob('app-k8-deployment-health-ci') {
    description('CI: Build/test/scan app-k8-deployment Health and publish image to JFrog.')
    properties {
        disableConcurrentBuilds()
    }
    triggers {
        githubPush()
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(cicdRepo)
                    }
                    branch("${branch}")
                }
            }
            scriptPath('jenkins/Jenkinsfile.ci')
            lightweight(true)
        }
    }
}

pipelineJob('app-k8-deployment-health-cd') {
    description('CD: Deploy app-k8-deployment Health to EKS using Helm.')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(cicdRepo)
                    }
                    branch("${branch}")
                }
            }
            scriptPath('jenkins/Jenkinsfile.cd')
            lightweight(true)
        }
    }
}

pipelineJob('app-k8-deployment-platform-prepare') {
    description('Platform: Terraform AWS infrastructure and bootstrap EKS platform components.')
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(cicdRepo)
                    }
                    branch("${branch}")
                }
            }
           scriptPath('jenkins/Jenkinsfile.platform')
            lightweight(true)
        }
    }
}