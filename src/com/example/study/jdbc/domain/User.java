package com.example.study.jdbc.domain;

import com.example.study.jdbc.annotation.ColumnInfo;
import com.example.study.jdbc.annotation.PrimaryKey;
import com.example.study.jdbc.annotation.TableInfo;

/**
 * Created by lilinjie on 2019/12/2 11:17 上午
 */
@TableInfo
public class User {
    @PrimaryKey
    private long id;
    @ColumnInfo(value = "user_name")
    private String name;
    @ColumnInfo(value = "user_age")
    private int age;

    public User() {
    }

    public User(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
