#! /bin/bash

VERSION=$1

echo $VERSION

cd ./artifact

# shellcheck disable=SC2045
for every_file in `ls ./`; do
  echo '--------------------分隔线------------------'
  echo $every_file
  if [ -d $every_file ]; then
    pwd
    cd $every_file
    pwd
    ls
    sed -i "s#__image__#reg.czsdc.com:5000/cicd/${every_file}:${VERSION}#g" deployment.yaml
    cat deployment.yaml
    kubectl --kubeconfig=../dev-config delete -f deployment.yaml
    kubectl --kubeconfig=../dev-config apply -f deployment.yaml
    cd ../
  fi
done
