## 定期备份docker内的mysql数据到宿主机



#### 1、准备shell脚本

```shell
#!/bin/bash
dateBackup=$(date +%Y-%m-%d_%H:%M:%S)
dir=/data/mysql/data/backup/${dateBackup}
# 宿主机新建目录，通过挂载会自动添加到容器
if [ ! -d "${dir}" ]
then
		mkdir -p ${dir}
		echo "创建文件夹 ${dir} 成功" >> ${dir}/backupLog.log
else
		echo "创建文件夹 ${dir} 失败，文件夹已存在" >> ${dir}/backupLog.log
fi
# 需要备份的数据库名		
dbNames=(fast-parking fast-parking-member-merchant fast-parking-order fast-parking-payment)
for dbName in ${dbNames[@]}
do
		echo "-----------------> 备份 ${dbName} 数据库 <-----------------" >> ${dir}/backupLog.log
        docker exec -i 9e9025aabf50 sh -c "mysqldump -uroot -pxsdfopNdfret -h127.0.0.1 -P3306 ${dbName} 1>> /var/lib/mysql/backup/${dateBackup}/${dbName}.sql 2>> /var/lib/mysql/backup/${dateBackup}/backupLog.log"
done

echo -e " \n ----------------> 删除过期文件 <---------------------------" >> ${dir}/backupLog.log
# 判断文件夹数量是否大于7，防止程序意外停止，删除所有备份
dirCount=`ls -l /data/mysql/data/backup/|grep "^d"|wc -l`
if [ ${dirCount} -gt 7 ]
then
	# 删除超过七天的带"_"的目录
	find /data/mysql/data/backup/ -mtime +6 -name "*_*" -exec rm -rf {} \;
	echo -e " 删除过期文件成功" >> ${dir}/backupLog.log
else 
	echo "删除过期文件失败，文件数量小于 7 " >> ${dir}/backupLog.log
fi


```



#### 2、编写crontab，定时执行脚本

```shell
1、添加定时任务
crontab -e

2、凌晨 01：00 分执行 上面的shell脚本 文件
0 */1 * * * bash /root/backup/backup.sh

3、查看定时任务
crontab -l

4、查看定时任务日志
tail -f /var/log/cron

5、crond 服务
开启、关闭、重启 可以使用 systemctl start、stop、restart crond
```

