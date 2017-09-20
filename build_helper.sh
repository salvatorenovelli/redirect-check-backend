#!/bin/sh


export PROJECT_ID=redirect-check-180020/
export VERSION=`xmlstarlet sel -N x=http://maven.apache.org/POM/4.0.0 -t -m /x:project/x:version  -v . pom.xml`
export GROUP_ID=`xmlstarlet sel -N x=http://maven.apache.org/POM/4.0.0 -t -m /x:project/x:groupId  -v . pom.xml`
export ARTIFACT_ID=`xmlstarlet sel -N x=http://maven.apache.org/POM/4.0.0 -t -m /x:project/x:artifactId  -v . pom.xml`


export IMAGE_TAG=gcr.io/${PROJECT_ID}/${ARTIFACT_ID}:${VERSION}


if [ -z "$1" ]
  then
    echo "No argument supplied"
    exit
fi

echo "Going to $1 ${IMAGE_TAG}"

case $1 in
    "build" )
#        mvn clean package || exit 1
        echo "Building ${IMAGE_TAG}"
        docker build -t ${IMAGE_TAG} .
    ;;
    "run" )
        docker run --name ${ARTIFACT_ID} -it --rm -p 8080:8080 -p 5005:5005 ${IMAGE_TAG}
    ;;
    "push" )
        gcloud docker -- push ${IMAGE_TAG}
    ;;
    "deploy" )
        kubectl delete -f k8s/production.yaml
        sed -i.bak "s#<IMAGE_TAG_DO_NOT_EDIT>#${IMAGE_TAG}#" k8s/production.yaml
        kubectl apply -f k8s/production.yaml
        mv k8s/production.yaml.bak k8s/production.yaml
    ;;
esac


