#!/bin/bash

export AWS_CLI_PROFILE=devops
export AWS_REGION=us-east-1
export STACK_NAME=custom-runtime

aws cloudformation delete-stack \
  --stack-name $STACK_NAME \
  --region $AWS_REGION \
  --profile $AWS_CLI_PROFILE