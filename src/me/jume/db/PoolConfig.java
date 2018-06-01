package me.jume.db;

/**
 * 数据库连接池配置信息
 */
public class PoolConfig{

  //JDBC基础知识，数据库驱动类、连接地址、用户名、密码
    private String driverName;
    private String url;
    private String userName;
    private String password;


    /**常用数据库连接池的配置项 c3p0 dbcp durid*/
    /** 空闲的最少连接数、空闲的最大连接数、初始化连接数*/
    private int minConn=1;
    private int maxConn=5;
    private int initConn=5;

    // 连接池允许的最大连接数，一般是数据库允许的最大连接数
    private int maxActiveConn = 10;
    // 连接不够时，线程去获取连接时等待的时间，单位毫秒
    private int waitTime = 1000;
    // 数据库连接池时候开启自检机制
    private boolean isCheck = false;
    private long checkPeriod = 1000*30;


    public String getDriverName() {
        return driverName;
    }
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getMinConn() {
        return minConn;
    }
    public void setMinConn(int minConn) {
        this.minConn = minConn;
    }
    public int getMaxConn() {
        return maxConn;
    }
    public void setMaxConn(int maxConn) {
        this.maxConn = maxConn;
    }
    public int getInitConn() {
        return initConn;
    }
    public void setInitConn(int initConn) {
        this.initConn = initConn;
    }
    public int getMaxActiveConn() {
        return maxActiveConn;
    }
    public void setMaxActiveConn(int maxActiveConn) {
        this.maxActiveConn = maxActiveConn;
    }
    public int getWaitTime() {
        return waitTime;
    }
    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
    public boolean isCheck() {
        return isCheck;
    }
    public void setCheck(boolean check) {
        isCheck = check;
    }
    public long getCheckPeriod() {
        return checkPeriod;
    }
    public void setCheckPeriod(long checkPeriod) {
        this.checkPeriod = checkPeriod;
    }
}
