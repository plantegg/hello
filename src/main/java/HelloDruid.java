import com.alibaba.druid.pool.DruidDataSource;
import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
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
        dataSource.setUrl("jdbc:mysql://" + ip + ":" + port + "/sbtest?useSSL=false&connectTimeout=15000&socketTimeout=17000&useServerPrepStmts=true&cachePrepStmts=true");
        dataSource.setUsername("root");
        dataSource.setPassword("123");
        dataSource.setInitialSize(connNum);
        dataSource.setMinIdle(0);
        dataSource.setMaxActive(connNum);
        dataSource.setMinEvictableIdleTimeMillis(1);
        dataSource.setTimeBetweenEvictionRunsMillis(3000);
        //dataSource.setMaxWait(60000);
        dataSource.setMaxWait(60000000);
        dataSource.setUseUnfairLock(!fair);
        if(fair){
            dataSource.setMaxWait(60000000);
        }
        try {
            dataSource.init();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    static class DatabaseTask implements Runnable {
        @Override
        public void run() {

            PreparedStatement updateStmt = null;
            Statement stmt = null;
            while (true) {
                long iter=100000;
                try (Connection conn = dataSource.getConnection()) {
                    conn.setAutoCommit(false);
                    stmt = conn.createStatement();
                     //PreparedStatement stmt = conn.prepareStatement("SELECT 1")) {
                    //stmt.executeQuery()

                    ResultSet rs = stmt.executeQuery("select sleep(0.046) from sbtest1 limit 1");

                    /*
                    ResultSet rs = stmt.executeQuery("SELECT k from ren where id=1 for update;");
                    if(rs.next()){
                        long currentK = rs.getLong("k");
                        currentK++;
                        String updateSQL = "UPDATE ren SET k = ? WHERE id = 1";
                        updateStmt = conn.prepareStatement(updateSQL);
                        updateStmt.setLong(1, ++currentK);
                        boolean rs2 = updateStmt.execute();
                        //ResultSet rs2 = conn.createStatement().executeQuery(updateSQL);
                        //conn.pre
                        if (!rs2) {
                            System.out.println("更新成功！k 从 " + (currentK-1) + " 更新为 " + currentK);
                            // 提交事务
                            conn.commit();
                        } else {
                            System.out.println("更新失败！");
                            // 回滚事务
                            conn.rollback();
                        }
                        updateStmt.close();
                    } */
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
//                    while (iter > 0) {
//                        iter--;
//                    }
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
