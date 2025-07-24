//参数说明：第三个参数 true 表示启用公平锁，1000 表示 1000 个并发，100 表示 Druid 数据库连接数 100
taskset -a -c 0-15,32-47 java -jar /root/HelloDruid/hellodruid-1.0.jar 127.1 3306 true 1000 100

java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005  -classpath ./helloworld-1.0.jar HelloDruid 127.0.0.1 3306

java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005  -classpath ./helloworld-1.0.jar JedisTest 127.0.0.1 6379

java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005  -classpath ./helloworld-1.0.jar SocketClient 1.2.3.4 4321
