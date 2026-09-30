def repo = 'https://github.com/ravishaggarwal/auspost-devsecops-reference.git'

pipelineJob('auspost-health-ci') {
  description('CI: build, test, SonarQube, Snyk SAST/SCA/container scan, Docker and JFrog push.')
  properties {
    disableConcurrentBuilds()
  }
  triggers {
    githubPush()
    pollSCM('H/5 * * * *')
  }
  definition {
    cpsScm {
      scm {
        git {
          remote { url(repo) }
          branch('*/main')
        }
      }
      scriptPath('jenkins/Jenkinsfile.ci')
      lightweight(true)
    }
  }
}

pipelineJob('auspost-health-cd') {
  description('CD: Helm deployment to EKS with atomic rollout and health validation.')
  definition {
    cpsScm {
      scm {
        git {
          remote { url(repo) }
          branch('*/main')
        }
      }
      scriptPath('jenkins/Jenkinsfile.cd')
      lightweight(true)
    }
  }
}

pipelineJob('auspost-platform-prepare') {
  description('Platform: Terraform AWS networking/EKS and cluster access validation.')
  definition {
    cpsScm {
      scm {
        git {
          remote { url(repo) }
          branch('*/main')
        }
      }
      scriptPath('jenkins/Jenkinsfile.platform')
      lightweight(true)
    }
  }
}
