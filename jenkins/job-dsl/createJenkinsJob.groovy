def cicdRepo = 'https://github.com/ravishaggarwal/AusPost-applicationPlaformCICD.git'
def branch = 'main'

pipelineJob('auspost-health-ci') {
    description('CI: Build/test/scan AusPost Health and publish image to JFrog.')

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


pipelineJob('auspost-health-cd') {
    description('CD: Deploy AusPost Health to EKS using Helm.')

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


pipelineJob('auspost-platform-prepare') {
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