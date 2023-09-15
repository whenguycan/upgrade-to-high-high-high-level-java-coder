## nexus自带的仓库



1. maven-central：maven中央库，默认从https://repo1.maven.org/maven2/拉取jar
2. maven-releases：对应的类型是hosted，是存储我们自己开发的release的jar包。
3. maven-snapshots：对应的类型是hosted，是存储我们自己开发的snapshots的jar包。
4. maven-public：仓库分组，把上面三个仓库组合在一起对外提供服务，在本地maven基础配置settings.xml或项目pom.xml中，如果需要以上3个仓库，就只要配置这一个地址就行，不用再配置3个地址了！