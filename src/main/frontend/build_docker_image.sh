#!/bin/sh


export VERSION=`jq -r '.version' package.json`
export imageTag=github.com/salvatorenovelli/redirect-check-frontend:${VERSION}

yarn build

echo "Building ${imageTag}"
docker build -t ${imageTag} .


#To run it locally you might want to use this :)
# $ docker run -it --rm -p 5000:80 ${imageTag}


sed -i.bak "s#<IMAGE_TAG_DO_NOT_EDIT>#${imageTag}#" k8s/production.yaml

kubectl apply -f k8s/

mv k8s/production.yaml.bak k8s/production.yaml


