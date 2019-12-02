package com.example.study.jdbc;

import com.example.study.jdbc.dao.impl.BaseDao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

/**
 * Created by lilinjie on 2019/11/29 4:37 下午
 */
public class JDBCUtils {

    private static String driver;
    private static String url;
    private static String user;
    private static String password;

    static {
        initConnectionConfig();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private JDBCUtils() {
        throw new UnsupportedOperationException("u can't instantiate me !");
    }

    private static void initConnectionConfig() {
        Properties properties = new Properties();
        InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = properties.getProperty("jdbc.driver");
        url = properties.getProperty("jdbc.url");
        user = properties.getProperty("jdbc.user");
        password = properties.getProperty("jdbc.password");
    }

    /**
     * 获取连接
     *
     * @return
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static int delete(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = JDBCUtils.getConnection();
            pstmt = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, pstmt, connection);
        }
        return 0;
    }

    public static int update(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = JDBCUtils.getConnection();
            pstmt = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, pstmt, connection);
        }
        return 0;
    }

    public static long insert(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        long insertId = -1L;
        try {
            connection = JDBCUtils.getConnection();
            pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert user failed, no rows affected.");
            }
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                insertId = generatedKeys.getLong("id");
            } else {
                throw new SQLException("Insert user failed, no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null, pstmt, connection);
        }
        return insertId;
    }

    public static <T> List<T> query(String sql, BaseDao<T> baseDao, Object... args) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        List<T> dataList = null;
        try {
            connection = JDBCUtils.getConnection();
            pstmt = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
            resultSet = pstmt.executeQuery();
            dataList = baseDao.handleResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(resultSet, pstmt, connection);
        }
        return dataList;
    }

    /**
     * 关闭资源
     *
     * @param resultSet
     * @param statement
     * @param connection
     */
    public static void close(ResultSet resultSet, Statement statement, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
