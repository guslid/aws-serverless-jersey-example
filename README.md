# aws-serverless-jersey-example
A basic example AWS Serverless Java container application written with the [Jersey framework] (https://github.com/awslabs/aws-serverless-java-container). The `LambdaHandler` object is the main entry point for Lambda. The application can be deployed in an AWS account using the [Serverless Application Model](https://github.com/awslabs/serverless-application-model). The `app-sam.yaml` file in the root folder contains the application definition.

The application is meant to be deployed as part of an automated CI/CD pipeline inspired from [this example] (https://aws.amazon.com/blogs/compute/continuous-deployment-for-serverless-applications/). This allows for a fully automated pipeline with multiple stages including build and integration tests as well as support for environment variables for full flexibility.

![overview](images/overview.png)

## Installation

1. Fork your own copy of this repository to your Github account
2. Generate a personal access token for your Github account
3. Deploy the sample Pipeline by launching the CloudFormation stack below in eu-west-1

[![cloudformation-launch-stack](images/cloudformation-launch-stack.png)](https://console.aws.amazon.com/cloudformation/home?region=eu-west-1#/stacks/new?stackName=PetStoreDemo&templateURL=https://s3.amazonaws.com/borjeson-public/serverless-pipeline.yaml)
