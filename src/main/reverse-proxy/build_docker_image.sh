#!/bin/sh


export VERSION=`jq -r '.version' package.json`
export APP_NAME=`jq -r '.name' package.json`
export IMAGE_TAG=github.com/salvatorenovelli/${APP_NAME}:${VERSION}


if [ -z "$1" ]
  then
    echo "No argument supplied"
    exit
fi


case $1 in
    "build" )
        npm i
        echo "Building ${IMAGE_TAG}"
        docker build -t ${IMAGE_TAG} .
    ;;
    "run" )
        docker run -it --rm -p 5000:80 ${IMAGE_TAG}
    ;;
    "deploy" )
        kubectl delete -f k8s/production.yaml
        sed -i.bak "s#<IMAGE_TAG_DO_NOT_EDIT>#${IMAGE_TAG}#" k8s/production.yaml
        kubectl apply -f k8s/production.yaml
        mv k8s/production.yaml.bak k8s/production.yaml
    ;;
esac





