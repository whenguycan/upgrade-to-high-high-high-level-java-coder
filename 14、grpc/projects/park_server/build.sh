#! /bin/bash

all_modules_arr=(parking-charge parking-device parking-lot parking-notification parking-order parking-payment parking-member-merchant)

ls -la ./

#如果文件夹不存在，创建文件夹
if [ ! -d "./artifact" ]; then
  mkdir ./myfolder
else
  rm -rf ./artifact
  mkdir ./artifact
fi

for every_module in ${all_modules_arr[*]}; do
  pwd
  cd  ${every_module}
  pwd
  ls
  mkdir ../artifact/${every_module}
  mvn clean package -Dmaven.test.skip=true -gs ./mvn-setting.xml  -Dmaven.repo.local=/home/cache/maven
  cp ./target/*.jar ../artifact/${every_module}/${every_module}.jar
  cp ./Dockerfile ../artifact/${every_module}/
  cp ./deployment.yaml ../artifact/${every_module}/
  ls
  cd ../
  cp ./buildimage.sh ./artifact
  cp ./deploy.sh ./artifact
  cp ./dev-config ./artifact
done
