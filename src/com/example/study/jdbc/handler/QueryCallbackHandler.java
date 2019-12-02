package com.example.study.jdbc.handler;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by lilinjie on 2019/12/2 3:27 下午
 */
public interface QueryCallbackHandler<T> {
    void callback(ResultSet resultSet);
}
