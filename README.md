java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005  -classpath ./helloworld-1.0.jar HelloWorldDruid 127.0.0.1 3306

java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005  -classpath ./helloworld-1.0.jar JedisTest 127.0.0.1 6379

java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005  -classpath ./helloworld-1.0.jar SocketClient 1.2.3.4 4321
