package com.example.study.jdbc.pool.impl;

import com.example.study.jdbc.JDBCUtils;
import com.example.study.jdbc.pool.ConnectionPoolConfig;
import com.example.study.jdbc.pool.IConnectionPool;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Administrator on 2019/12/2 21:20
 */
public class ConnectionPool implements IConnectionPool {
    private static ConnectionPool sInstance = new ConnectionPool();
    private final ThreadLocal<Connection> TL_CONNECTION_POOL = new ThreadLocal<>();
    private final Object lock = new Object();
    private List<Connection> freeConnections = new ArrayList<>();
    private List<Connection> activeConnections = new ArrayList<>();
    private boolean isActive = true;
    private int activeConnectionCount;
    private ConnectionPoolConfig connectionPoolConfig;

    private ConnectionPool() {
        initConnectionPoolConfig();
        initConnectionPool();
    }

    public static ConnectionPool getInstance() {
        return sInstance;
    }

    private void initConnectionPoolConfig() {
        Properties properties = new Properties();
        InputStream is = ConnectionPool.class.getClassLoader().getResourceAsStream("pool.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int maxConnectionCount = Integer.parseInt(properties.getProperty("pool.maxConnectionCount"));
        int initConnectionCount = Integer.parseInt(properties.getProperty("pool.initConnectionCount"));
        int connectionTimeout = Integer.parseInt(properties.getProperty("pool.connectionTimeout"));
        connectionPoolConfig = new ConnectionPoolConfig(maxConnectionCount, initConnectionCount, connectionTimeout);
    }

    private void initConnectionPool() {
        for (int i = 0; i < connectionPoolConfig.getInitConnectionCount(); i++) {
            Connection connection = createNewConnection();
            if (connection != null) {
                freeConnections.add(connection);
                activeConnectionCount++;
            }
            isActive = true;
        }
    }

    @Override
    public Connection acquireConnection() {
        Connection connection = TL_CONNECTION_POOL.get();
        if (isConnectionValid(connection)) {
            return connection;
        }
        synchronized (lock) {
            if (activeConnectionCount < connectionPoolConfig.getMaxConnectionCount()) {
                if (freeConnections.size() > 0) {
                    connection = freeConnections.get(0);
                    if (connection != null) {
                        TL_CONNECTION_POOL.set(connection);
                    }
                    freeConnections.remove(0);
                } else {
                    connection = createNewConnection();
                }
            } else {
                try {
                    wait(connectionPoolConfig.getConnectionTimeout());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                connection = acquireConnection();
            }
            if (isConnectionValid(connection)) {
                activeConnections.add(connection);
                activeConnectionCount++;
            }
        }
        return connection;

    }

    @Override
    public void releaseConnection(Connection connection) {
        synchronized (lock) {
            if (isConnectionValid(connection) && !(freeConnections.size() > connectionPoolConfig.getMaxConnectionCount())) {
                freeConnections.add(connection);
                activeConnections.remove(connection);
                activeConnectionCount--;
                TL_CONNECTION_POOL.remove();
                notifyAll();
            }
        }
    }

    @Override
    public Connection createNewConnection() {
        return JDBCUtils.getConnection();
    }

    @Override
    public void destroyConnectionPool() {
        synchronized (lock) {
            for (Connection connection : freeConnections) {
                if (isConnectionValid(connection)) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (Connection connection : activeConnections) {
                if (isConnectionValid(connection)) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            activeConnectionCount = 0;
            isActive = false;
        }
    }

    public boolean isActive() {
        synchronized (lock) {
            return isActive;
        }
    }

    private boolean isConnectionValid(Connection connection) {
        try {
            if (connection == null || connection.isClosed()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
