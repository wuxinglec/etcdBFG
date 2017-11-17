# highavailabilitybyetcd
# etcdBFG

# 使用方法
# step_1: 部署etcd
# step_2: 配置etcd.conf 文件 && 相配套的 lua 文件  lua文件的地址在etcd.conf中定义
# step_3: 部署实例






# 通过jar包方式供其他程序使用（仅限公司，线程间通信，正式版本不会使用）pom文件写法 maven
<!-- smartbi sdk -->
		<dependency>
			<groupId>etcdgroup</groupId>
			<artifactId>etcdartifactid</artifactId>
			<version>1.0</version>
			<scope>system</scope>
			<!-- <systemPath>${project.basedir}/libs/smartbi-SDK-6.0.jar</systemPath> -->
			<systemPath>${project.basedir}/src/main/java/com/lenovo/libappend/etcdartifactid-1.0-SNAPSHOT-jar-with-dependencies.jar</systemPath>
		</dependency>


