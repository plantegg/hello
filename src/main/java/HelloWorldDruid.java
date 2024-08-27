import com.alibaba.druid.pool.DruidDataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloWorldDruid {
    private static DruidDataSource dataSource;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java DruidThreadTest <ip> <port>");
            System.exit(1);
        }

        String ip = args[0];
        String port = args[1];

        initDataSource(ip, port);

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        for (int i = 0; i < 100; i++) {
            executorService.submit(new DatabaseTask());
        }

        executorService.shutdown();
    }

    private static void initDataSource(String ip, String port) {
        dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://" + ip + ":" + port + "/test?useSSL=false&connectTimeout=1500&socketTimeout=1700");
        dataSource.setUsername("root");
        dataSource.setPassword("123");
        dataSource.setInitialSize(0);
        dataSource.setMinIdle(0);
        dataSource.setMaxActive(1);
    }

    static class DatabaseTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                try (Connection conn = dataSource.getConnection()) {
                    Statement stmt = conn.createStatement();
                     //PreparedStatement stmt = conn.prepareStatement("SELECT 1")) {

                    stmt.execute("SELECT sleep(0.005), id from sbtest1 limit 1;");
                    Thread.sleep(1);

                    //System.out.println("id:"+Thread.currentThread().toString());
                    //System.out.println("dataSource:"+dataSource.getActiveCount());
                } catch (SQLException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
