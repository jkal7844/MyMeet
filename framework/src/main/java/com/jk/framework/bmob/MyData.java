package com.jk.framework.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created By Admin on 2020/3/29
 * Describe:
 */
public class MyData extends BmobObject {

    private String name;

    private int sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "MyData{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                '}';
    }
}
