package com.example.study.jdbc.pool;

import java.sql.Connection;

/**
 * Created by Administrator on 2019/12/2 21:19
 */
public interface IConnectionPool {

    Connection acquireConnection();

    void releaseConnection(Connection connection);

    Connection createNewConnection();

    void destroyConnectionPool();
}
