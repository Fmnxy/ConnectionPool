package me.jume.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

/**
 * 数据库连接池的管理类
 *    数据库连接池的初始化、销毁、获取连接、释放连接
 */
public class ConnectionPool {

    // 连接池配置对象，产生此连接池的时候，需要知道各项配置信息
     private PoolConfig poolConfig;
    /*记录当前连接池的所有连接对象*/
     private int count;
    // 空闲连接的集合和正在使用的连接集合，是否需要考虑线程安全
     private Vector<Connection> freeConn = new Vector<>();
     private Vector<Connection> useConn = new Vector<>();


    /** 为了保证连接资源不浪费，同一个线程不管取多少次，都提供一个连接
     * 使用本地线程变量，给每个线程提供一个空间，可以存储一个对象*/
     private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

     public ConnectionPool(PoolConfig poolConfig){
          this.poolConfig=poolConfig;
          init();
     }


    /**
     * 初始化数据库连接池
     */
     public void init(){
          for (int i=0;i<poolConfig.getInitConn();i++){
              Connection conn;
              try {
                 conn=getNewConnetion();
                 freeConn.add(conn);
                 count++;
              } catch (SQLException e) {
                  e.printStackTrace();
              }
          }
     }

    /**
     * 销毁数据库连接池
     *     将空闲连接和在使用的连接全部关闭，将连接池中连接的数量改为0
     */
    public void destroy(){
          for (Connection conn :freeConn){
              try {
                  conn.close();
              } catch (SQLException e) {
                  e.printStackTrace();
              }
          }
          for (Connection conn :useConn){
              try {
                  conn.close();
              } catch (SQLException e) {
                  e.printStackTrace();
              }
          }
          count=0;
     }

    /**
     * 从连接池中获取连接对象 采用线程安全
     * @return
     */
     public synchronized Connection getConnetcion(){
           Connection conn =null;
           try{
               // 数据库连接池中当前的存在的连接总数小于配置的最大连接数
                if (count<poolConfig.getMaxActiveConn()){
                    if (freeConn.size()>0){
                        conn=freeConn.remove(0);
                    }else {
                         conn = getNewConnetion();
                         count++;
                    }
                    // 判断拿到的这个连接是否可用
                    if (isEnable(conn)){
                          useConn.add(conn);
                    }else {
                        count--;
                        conn=getConnetcion(); //递归调用
                    }
                }else { // 当前连接池中连接数量已经达到最大连接数，先等待
                     wait(poolConfig.getWaitTime()); // 线程等待一定时间
                     conn = getConnetcion();
                }
           }catch (Exception e){
              e.printStackTrace();
           }
           threadLocal.set(conn);
           return conn;
     }

    /**判断连接对象是否可用*/
    private boolean isEnable(Connection conn){
         if (conn==null){
             return false;
         }
        try {
            if (conn.isClosed()){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * 获取新的连接、创建新的连接
     * @return
     * @throws SQLException
     */
     private Connection getNewConnetion() throws SQLException {
         Connection conn=null;
         conn=DriverManager.getConnection(poolConfig.getUrl(),poolConfig.getUserName(),poolConfig.getPassword());
         return conn;
     }

    /**
     * 释放连接，将连接放回连接池
     */
    public synchronized void releaseConnection(Connection conn){
         if (isEnable(conn)){
             // 当空闲连接数没有达到最大数量的时候，将当前连接放回连接池
             if (freeConn.size()<poolConfig.getMaxConn()){
                 freeConn.add(conn);
             }else {
                 // 已经达到最大连接数量，关闭当前连接
                 try {
                     conn.close();
                 } catch (SQLException e) {
                     e.printStackTrace();
                 }
             }
         }
         useConn.remove(conn); //将要释放的连接从在使用的连接集合中移除
         count--;
         threadLocal.remove();// 将当前线程使用的连接从本地线程变量中移除
         notifyAll();       // 通知其他所有等待的线程可以来获取连接
     }




}
