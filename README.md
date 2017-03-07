# aws-serverless-jersey-example
A basic example AWS Serverless Java container application written with the [Jersey framework] (https://github.com/awslabs/aws-serverless-java-container). The `LambdaHandler` object is the main entry point for Lambda. The application can be deployed in an AWS account using the [Serverless Application Model](https://github.com/awslabs/serverless-application-model). The `app-sam.yaml` file in the root folder contains the application definition.

The application is meant to be deployed as part of an automated CI/CD pipeline inspired from [this example] (https://aws.amazon.com/blogs/compute/continuous-deployment-for-serverless-applications/). This allows for a fully automated pipeline with multiple stages including build and integration tests as well as support for environment variables for full flexibility.

There are two separate parts to this application: the api and the pipeline which detects, builds, and deploys changes.

## Installation

#### 1. Fork the GitHub repository

[Fork](https://help.github.com/articles/fork-a-repo/) this [Sample Application](https://github.com/sctobbor/aws-serverless-jersey-example) GitHub repository into your GitHub account.

From your terminal application, execute the following command (make sure to replace `<your_github_username>` with your actual GitHub username):

```console
git clone https://github.com/<your_github_username>/aws-serverless-jersey-example
```

This creates a directory named `aws-serverless-jersey-example` in your current directory, which contains the code for the sample app.

#### 2. Create the CloudFormation stack

Choose **Launch Stack** to launch the template in the eu-west-1 (Dublin) Region in your account:

[![cloudformation-launch-stack](images/cloudformation-launch-stack.png)](https://console.aws.amazon.com/cloudformation/home?region=eu-west-1#/stacks/new?stackName=PetStoreDemo&templateURL=https://s3.amazonaws.com/borjeson-public/serverless-pipeline.yaml)

The CloudFormation template requires the following parameters:

- GitHub configuration
  - **ServiceName**: The name of the sample service.
  - **GitHubRepo**: The name of the repo to deploy continuously.
  - **GitHubOwner**: Your username on GitHub.
  - **GitHubOAuthToken**: Token for the user specified above. ([https://github.com/settings/tokens](https://github.com/settings/tokens))

The deployed CloudFormation stacks provides the following output:

- **JerseyPetStoreApi-${Stage}**: The service API endpoint for the specific stage (beta/gamma/prod).
- **TestFunction**: The name of the deployed Lambda function for integration tests.

### Testing the example

After the CloudFormation stack is created, the latest commit to the GitHub repository is run through the pipeline and deployed to each stage environment (beta/gamma/prod) if integration tests executes successfully, the pipeline requires manual approval before deploying to the last production stage. Open the deployed CodePipeline to watch the first revision run through the pipeline. After the deploy step for a specific stage turns green, use the URL from **JerseyPetStoreApi-${Stage}** as the API endpoint with your client of choice.

To test continuous deployment, make a change in the aws-serverless-jersey-example repository and push it to GitHub. CodePipeline detects the change, builds the new application, runs integration tests and deploys it automatically to each stage in the pipeline.

### Cleaning up the example resources

To remove all resources created by this example, do the following:

1. Delete the deployed CloudFormation stacks for each stage ${ServiceName}-Stack-${Stage} as well as the main CloudFormation stack which deletes the resources created by the exercises.
1. Manually delete resources which may contain files:
  - S3 bucket: `ArtifactBucket`
