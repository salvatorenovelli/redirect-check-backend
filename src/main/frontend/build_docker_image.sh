#!/bin/sh

yarn build
docker build -t redirect-check-frontend:v1 .


#To run it locally you might want to use this :)
# $ docker run -it --rm -p 5000:5000 redirect-check-frontend:v1