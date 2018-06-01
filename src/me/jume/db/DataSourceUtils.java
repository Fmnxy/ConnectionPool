package me.jume.db;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

/**
 * 操作数据库连接池的工具类
 */
public class DataSourceUtils {
    private static PoolConfig poolConfig = new PoolConfig();

    static{
        Properties prop = new Properties();
        try {
            prop.load(DataSourceUtils.class.getClassLoader()
                    .getResourceAsStream("db.properties"));
            poolConfig.setDriverName(prop.getProperty("jdbc.driverName"));
            poolConfig.setUrl(prop.getProperty("jdbc.url"));
            poolConfig.setUserName(prop.getProperty("jdbc.userName"));
            poolConfig.setPassword(prop.getProperty("jdbc.password"));
            // 将驱动类加载到JVM虚拟机
            Class.forName(poolConfig.getDriverName());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static ConnectionPool connPool = new ConnectionPool(poolConfig);

    /**
     * 从连接池中获取连接对象
     * @return
     */
    public static Connection getConnection(){
        return connPool.getConnetcion();
    }

    /**
     * 释放连接
     * @param conn
     */
    public static void closeConnection(Connection conn){
        connPool.releaseConnection(conn);
    }
}
