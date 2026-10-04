# Jenkins setup

Install at minimum:

- Pipeline
- Job DSL
- Git
- GitHub Branch Source / your SCM webhook integration
- Credentials Binding
- Docker Pipeline
- SonarQube Scanner
- Kubernetes CLI
- AWS Credentials
- JUnit

Create credentials outside Git:

```text
vault-addr             Secret text
vault-approle-role-id  Secret text
vault-approle-secret-id Secret text
jfrog-credentials      Username/password
sonarqube-token        Secret text
aws-deploy-role        AWS credentials
acm-certificate-arn    Secret text
```

The requested Snyk tenancy is represented by:

```text
AusPostSynkTenancy
```

The requested JFrog project path is represented by:

```text
AusPostJrog/auspost-health
```

For production, prefer Vault-backed dynamic credentials or your organization's supported Jenkins/Vault credential provider rather than long-lived static credentials.

Create a seed job:

1. New Item -> Freestyle project -> `secreateJenkinsJob.groovy`.
2. Add the "Process Job DSLs" build step.
3. Load `jenkins/job-dsl/screateJenkinsJob.groovy` from SCM.
4. Replace the repository URL in `secreateJenkinsJob.groovy`.
5. Run the seed.
6. Configure the Git provider webhook to notify Jenkins on pushes to `main`.

The Job DSL generates the CI/CD/platform Pipeline jobs. Jenkins Job DSL is the job-generation mechanism; the actual build logic lives in Jenkinsfiles.
