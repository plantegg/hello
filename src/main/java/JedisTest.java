import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class JedisTest {
    private static int localPort=0;
    
    public static void main(String[] args) {
        Jedis jedis = null;
        try {
            //Thread.sleep(10000);
            jedis = new Jedis(args[0], Integer.parseInt(args[1]));
            jedis.ping();
            localPort=jedis.getConnection().getSocket().getLocalPort();
            jedis.close();
        }catch (JedisConnectionException e) {
            
            String localIpAddress = getLocalIpAddress();
            localPort=jedis.getConnection().getSocket().getLocalPort();
            int localPort = getLocalPort();
            System.out.println("java.net.SocketTimeoutException occurred on " + localIpAddress + ":" + localPort);
            // 其他异常处理逻辑
            // ...
            e.printStackTrace();
        }
    }

    private static String getLocalIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getLocalPort() {
        // 如果您知道使用的端口号，可以直接返回该值
        // 否则，您可能需要从相关的Socket或ServerSocket对象获取端口号
        return localPort; // 示例中返回0，表示未知端口
    }
}
