# CI/CD — Jenkins Job DSL and Pipelines

## Responsibility
This repository owns Jenkins automation for application CI, Kubernetes CD, and platform preparation. It references the other repositories; it does not own their source.

## Layout
```text
jenkins/Jenkinsfile.ci
jenkins/Jenkinsfile.cd
jenkins/Jenkinsfile.platform
jenkins/job-dsl/seed.groovy
```

## CI flow
```text
main push -> checkout application repo -> Gradle build
          -> unit tests -> component tests
          -> SonarQube
          -> Snyk SAST/SCA
          -> Docker build
          -> Snyk container scan
          -> JFrog push
          -> trigger CD with immutable image tag
```
JFrog target: `app-k8-deploymentJrog/app-k8-deployment-health:<version>`. Requested Snyk tenancy/organisation name is `app-k8-deploymentSynkTenancy`. The Snyk token must be retrieved from Vault rather than committed to Git.

## CD flow
```text
image tag + target environment
 -> checkout platform repo
 -> authenticate/read Vault
 -> produce environment Helm overrides
 -> Helm upgrade --install --atomic
 -> kubectl rollout status
 -> GET /health
 -> verify deployed version
```
This final version check ensures the externally reachable endpoint reflects the image just released.

## Job DSL
The seed DSL creates CI, CD and platform-preparation jobs. Configure the application/platform repository URLs in the DSL/Jenkins parameters for your Git organisation. Main-branch webhook triggering is preferred; polling can remain as fallback.

## Jenkins prerequisites
Configure Java/Gradle or wrapper execution, Docker access, SonarQube, Snyk CLI, Vault CLI/API access, Helm, kubectl, AWS CLI and JFrog connectivity.

Required credentials normally include Git, Vault AppRole, JFrog, SonarQube and an approved AWS deployment identity. Do not put secret values in Jenkinsfiles.

## Rollback
Helm deployments use `--atomic`; operational rollback can also use `helm rollback` or `kubectl rollout undo` according to the release procedure. PDB is availability protection, not rollback.
