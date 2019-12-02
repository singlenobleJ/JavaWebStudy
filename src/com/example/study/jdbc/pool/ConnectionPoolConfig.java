package com.example.study.jdbc.pool;

/**
 * Created by Administrator on 2019/12/2 22:05
 */
public class ConnectionPoolConfig {
    private int maxConnectionCount;
    private int initConnectionCount;
    private int connectionTimeout;

    public ConnectionPoolConfig(){

    }
    public ConnectionPoolConfig(int maxConnectionCount, int initConnectionCount, int connectionTimeout) {
        this.maxConnectionCount = maxConnectionCount;
        this.initConnectionCount = initConnectionCount;
        this.connectionTimeout = connectionTimeout;
    }

    public int getMaxConnectionCount() {
        return maxConnectionCount;
    }

    public void setMaxConnectionCount(int maxConnectionCount) {
        this.maxConnectionCount = maxConnectionCount;
    }

    public int getInitConnectionCount() {
        return initConnectionCount;
    }

    public void setInitConnectionCount(int initConnectionCount) {
        this.initConnectionCount = initConnectionCount;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
