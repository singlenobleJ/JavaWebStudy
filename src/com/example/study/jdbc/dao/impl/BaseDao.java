package com.example.study.jdbc.dao.impl;

import com.example.study.jdbc.JDBCUtils;
import com.example.study.jdbc.annotation.ColumnInfo;
import com.example.study.jdbc.annotation.TableInfo;
import com.example.study.jdbc.dao.IBaseDao;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lilinjie on 2019/12/2 2:13 下午
 */
public abstract class BaseDao<T> implements IBaseDao<T> {
    private boolean isInitialized;
    protected String tableName;
    protected Map<String, Field> columnInfos;

    public synchronized void init(Class<T> clazz) {
        if (isInitialized) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        if (columnInfos == null) {
            columnInfos = new HashMap<>();
        }
        TableInfo tableInfo = clazz.getAnnotation(TableInfo.class);
        if (tableInfo != null) {
            tableName = tableInfo.value();
        } else {
            tableName = clazz.getSimpleName();
        }
        for (Field field : fields) {
            field.setAccessible(true);
            ColumnInfo columnInfo = field.getAnnotation(ColumnInfo.class);
            if (columnInfo == null) {
                continue;
            }
            String columnName = columnInfo.value().length() == 0 ? field.getName() : columnInfo.value();
            columnInfos.put(columnName, field);
        }
        isInitialized = true;
    }

    @Override
    public long insert(T t) {
        String columnNames = getInsertColumnNames();
        String columnPlaceHolders = getInsertColumnPlaceHolders();
        String sql = "INSERT INTO " + tableName + "(" + columnNames + ") VALUES (" + columnPlaceHolders + ")";
        return JDBCUtils.insert(sql, getColumnValues(t));
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        return JDBCUtils.delete(sql, id);
    }

    @Override
    public int update(int id, T t) {
        String columnNamePlaceHolders = getUpdateColumnNamePlaceHolders();
        String sql = "UPDATE " + tableName + " SET " + columnNamePlaceHolders + " where id = ?";
        return JDBCUtils.update(sql, getColumnValues(t));
    }

    @Override
    public List<T> query(int id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        return JDBCUtils.query(sql, this, id);
    }

    @Override
    public List<T> queryAll() {
        String sql = "SELECT * FROM " + tableName;
        return JDBCUtils.query(sql, this);
    }

    public abstract List<T> handleResultSet(ResultSet resultSet);

    protected String getInsertColumnNames() {
        StringBuilder columnNameBuilder = new StringBuilder();
        for (String columnName : columnInfos.keySet()) {
            columnNameBuilder.append(columnName).append(",");
        }
        return columnNameBuilder.substring(0, columnNameBuilder.length() - 1);
    }

    protected String getInsertColumnPlaceHolders() {
        StringBuilder columnPlaceHolderBuilder = new StringBuilder();
        for (int i = 0; i < columnInfos.size(); i++) {
            columnPlaceHolderBuilder.append("?").append(",");
        }
        return columnPlaceHolderBuilder.substring(0, columnPlaceHolderBuilder.length() - 1);
    }

    protected Object[] getColumnValues(T t) {
        Object[] columnValues = new Object[columnInfos.size()];
        int i = 0;
        for (Field field : columnInfos.values()) {
            field.setAccessible(true);
            try {
                columnValues[i] = field.get(t);
                i++;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return columnValues;
    }

    protected String getUpdateColumnNamePlaceHolders() {
        StringBuilder columnNamePlaceHolderBuilder = new StringBuilder();
        for (String columnName : columnInfos.keySet()) {
            columnNamePlaceHolderBuilder.append(columnName).append(" ").append("= ").append("?").append(",");
        }
        return columnNamePlaceHolderBuilder.substring(0, columnNamePlaceHolderBuilder.length() - 1);
    }
}
