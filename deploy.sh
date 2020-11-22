#!/bin/bash
set -e
export AWS_S3_BUCKET=aj-lambda-examples
export AWS_S3_PREFIX=custom-runtime
export AWS_CLI_PROFILE=devops
export AWS_REGION=us-east-1
export STACK_NAME=custom-runtime

sam build
sam deploy \
  --stack-name $STACK_NAME \
  --region $AWS_REGION \
  --s3-bucket $AWS_S3_BUCKET \
  --s3-prefix $AWS_S3_PREFIX \
  --capabilities CAPABILITY_IAM \
  --profile $AWS_CLI_PROFILE
