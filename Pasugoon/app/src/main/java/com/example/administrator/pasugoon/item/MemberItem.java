package com.example.administrator.pasugoon.item;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017-10-18.
 */

public class MemberItem {
    public int seq;
    public String phone;
    public int state;
    @SerializedName("off_time") public String offTime;

    @Override
    public String toString() {
        return "MemberItem{" +
                "seq=" + seq +
                ", phone='" + phone + '\'' +
                ", state='" + state + '\'' +
                ", offTime='" + offTime + '\'' +
                '}';
    }
}
