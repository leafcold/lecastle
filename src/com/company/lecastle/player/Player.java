package com.company.lecastle.player;

//玩家信息类 不要太大 一般数据量为 uid +name + level 8+4+10= 22字节加上 * 10W  一共是220w字节 约为 2M 数据
public class Player {
    long uid;
    String name;
    int level;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }



}
