package com.example.study.test;

import com.example.study.jdbc.JDBCUtils;
import com.example.study.jdbc.dao.BaseDaoFactory;
import com.example.study.jdbc.dao.impl.BaseDao;
import com.example.study.jdbc.dao.impl.UserDao;
import com.example.study.jdbc.domain.User;

import java.sql.Connection;

/**
 * Created by lilinjie on 2019/12/2 3:07 下午
 */
public class JDBCTest {
    public static void main(String[] args) {
        Connection connection = JDBCUtils.getConnection();
        System.out.println(connection);
        BaseDao userDao = (BaseDao) BaseDaoFactory.getInstance().getDao(UserDao.class, User.class);

    }
}
