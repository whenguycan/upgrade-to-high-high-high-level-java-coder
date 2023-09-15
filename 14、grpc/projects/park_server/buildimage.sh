#! /bin/bash

DOCKER_REGISRTY_PASSWD=$2
DOCKER_REGISTRY_USER=$1
VERSION=$3

echo $DOCKER_REGISRTY_PASSWD
echo $DOCKER_REGISTRY_USER
echo $VERSION

echo $DOCKER_REGISRTY_PASSWD | docker login '10.10.210.23:5000/cicd/' -u $DOCKER_REGISTRY_USER --password-stdin

cd ./artifact

# shellcheck disable=SC2045
for every_file in `ls ./`; do
  echo $every_file
  if [ -d $every_file ]; then
    pwd
    cd $every_file
    pwd
    ls
    echo "docker build -t ${every_file}:${VERSION} -f ./Dockerfile ."
    docker build -t ${every_file}:${VERSION} -f ./Dockerfile .
    docker tag ${every_file}:${VERSION} '10.10.210.23:5000/cicd/'${every_file}:${VERSION}
    docker push '10.10.210.23:5000/cicd/'${every_file}:${VERSION}
    docker rmi '10.10.210.23:5000/cicd/'${every_file}:${VERSION}
    docker rmi ${every_file}:${VERSION}
    cd ../
  fi
done

touch finish.txt
