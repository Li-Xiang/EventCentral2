## EventCentral2
EventCentral2(EC2)是EventCentral迁移到SpringBoot平台上的版本。
部署需求:
  - Latest Java 1.8
  - A MySQL (like) database

### 1. 下载/构建EC2
1. 直接下载发布版本，解压即可。

2. EC2可以通过源代码基于apache maven自己构建:
```
$ mvn clean
$ mvn package
```
####2. 准备数据库
EC2的数据库结构语EC相同，可以直接使用原来的数据库，新建数据库的脚本也与原来相同，可以参考create_db.sql创建。

###3. 配置EC2
配置application.properties(这个是SpringBoot的默认配置文件), 将数据源指向你的数据库。Web端口SpringBoot默认的是8080，可以根据你的需要之际配置。如果你希望通过smtp发送告警事件，可以配置对应的smtp服务器。

###4. 执行
```
$ java -jar EventCentral2-1.0.jar
```
通过浏览器打开访问对应端口: http://your_ip:port/ 即可打开监控面板。