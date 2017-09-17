#!/bin/sh


export VERSION=`jq -r '.version' package.json`
export APP_NAME=`jq -r '.name' package.json`
export IMAGE_TAG=github.com/salvatorenovelli/${APP_NAME}:${VERSION}

yarn install

echo "Building ${IMAGE_TAG}"
docker build -t ${IMAGE_TAG} .


#To run it locally you might want to use this :)
# $ docker run -it --rm -p 5000:80 ${imageTag}


sed -i.bak "s#<IMAGE_TAG_DO_NOT_EDIT>#${IMAGE_TAG}#" k8s/production.yaml

kubectl apply -f k8s/production.yaml

mv k8s/production.yaml.bak k8s/production.yaml


