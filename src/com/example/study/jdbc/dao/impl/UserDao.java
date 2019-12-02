package com.example.study.jdbc.dao.impl;

import com.example.study.jdbc.domain.User;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by lilinjie on 2019/12/2 11:13 上午
 */
public class UserDao extends BaseDao<User> {
    @Override
    public List<User> handleResultSet(ResultSet resultSet) {
        return null;
    }
}
