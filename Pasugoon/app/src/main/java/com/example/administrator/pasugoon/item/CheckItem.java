package com.example.administrator.pasugoon.item;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017-10-18.
 */

public class CheckItem {
    @SerializedName("check_seq") public String checkSeq;
    @SerializedName("check_member_seq") public String checkMemberSeq;
    @SerializedName("check_chk_member_seq") public String checkChkMemberSeq;

    @Override
    public String toString() {
        return "CheckItem{" +
                "checkSeq='" + checkSeq + '\'' +
                ", checkMemberSeq='" + checkMemberSeq + '\'' +
                ", checkChkMemberSeq='" + checkChkMemberSeq + '\'' +
                '}';
    }

}
