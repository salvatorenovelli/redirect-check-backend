#!/bin/sh


export VERSION=`jq -r '.version' package.json`
export APP_TAG=github.com/salvatorenovelli/redirect-check-frontend:${VERSION}

yarn build
docker build -t ${APP_TAG} .


#To run it locally you might want to use this :)
# $ docker run -it --rm -p 5000:80 ${APP_TAG}