package com.example.study.jdbc.dao;

import java.util.List;

/**
 * Created by lilinjie on 2019/12/2 11:09 上午
 */
public interface IBaseDao<T> {

    long insert(T t);

    int delete(int id);

    int update(int id, T t);

    List<T> query(int id);

    List<T> queryAll();
}
