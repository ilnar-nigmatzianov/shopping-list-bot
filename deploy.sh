#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/AWS/aws_personal.pem \
    target/shopping-list-bot-1.0-SNAPSHOT.jar \
    ubuntu@ec2-3-14-149-182.us-east-2.compute.amazonaws.com:/home/ubuntu/

ssh -i ~/AWS/aws_personal.pem ubuntu@ec2-3-14-149-182.us-east-2.compute.amazonaws.com << EOF
echo 'Restart server...'
pgrep java | xargs kill -9
nohup java -jar shopping-list-bot-1.0-SNAPSHOT.jar > log.txt &
EOF

echo 'Bye'