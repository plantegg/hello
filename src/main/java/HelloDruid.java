import com.alibaba.druid.pool.DruidDataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloDruid {
    private static DruidDataSource dataSource;

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Usage: java DruidThreadTest <ip> <port> fairLock threadNum connNum");
            System.exit(1);
        }

        String ip = args[0];
        String port = args[1];
        int threadNum = Integer.parseInt(args[3]);
        int connNum = Integer.parseInt(args[4]);
        boolean fair = Boolean.parseBoolean(args[2]);

        initDataSource(ip, port, connNum, fair);

        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

        for (int i = 0; i < threadNum; i++) {
            executorService.submit(new DatabaseTask());
        }

        executorService.shutdown();
    }

    private static void initDataSource(String ip, String port, int connNum, boolean fair) {
        dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://" + ip + ":" + port + "/sbtest?useSSL=false&connectTimeout=1500&socketTimeout=1700");
        dataSource.setUsername("root");
        dataSource.setPassword("123");
        dataSource.setInitialSize(connNum);
        dataSource.setMinIdle(0);
        dataSource.setMaxActive(connNum);
        //dataSource.setMaxWait(60000);
        dataSource.setUseUnfairLock(!fair);
        if(fair){
            dataSource.setMaxWait(60000);
        }

    }

    static class DatabaseTask implements Runnable {
        @Override
        public void run() {

            while (true) {
                long iter=100000;
                try (Connection conn = dataSource.getConnection()) {
                    Statement stmt = conn.createStatement();
                     //PreparedStatement stmt = conn.prepareStatement("SELECT 1")) {

                    stmt.execute("SELECT sleep(0.001), id from sbtest1 limit 1;");
                    //Thread.sleep(1);
                    while(iter>0){
                        iter--;
                    }
                    stmt.close();
                    //System.out.println("id:"+Thread.currentThread().toString());
                    //System.out.println("dataSource:"+dataSource.getActiveCount());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
