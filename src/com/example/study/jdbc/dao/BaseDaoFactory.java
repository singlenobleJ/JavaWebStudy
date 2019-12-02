package com.example.study.jdbc.dao;

import com.example.study.jdbc.dao.impl.BaseDao;

/**
 * Created by lilinjie on 2019/12/2 2:18 下午
 */
public class BaseDaoFactory {
    private static BaseDaoFactory sInstance = new BaseDaoFactory();

    private BaseDaoFactory() {

    }

    public static BaseDaoFactory getInstance() {
        return sInstance;
    }

    public <T> IBaseDao<T> getDao(Class daoCls, Class<T> entityCls) {
        BaseDao<T> baseDao = null;
        try {
            baseDao = (BaseDao<T>) daoCls.newInstance();
            baseDao.init(entityCls);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return baseDao;
    }
}
