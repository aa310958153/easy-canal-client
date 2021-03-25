package com.easy.canal.test;

import java.io.Serializable;

/**
 * @Project mybatis-demo
 * @PackageName com.mybatis.demo
 * @ClassName Classes
 * @Author qiang.li
 * @Date 2021/3/24 4:57 下午
 * @Description TODO
 */
public class Classes implements Serializable {
    private int id;
    private String name;
    private Sex sex;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }
}