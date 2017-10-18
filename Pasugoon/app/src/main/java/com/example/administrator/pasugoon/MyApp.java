package com.example.administrator.pasugoon;

import android.app.Application;
import android.os.StrictMode;

import com.example.administrator.pasugoon.item.CheckItem;
import com.example.administrator.pasugoon.item.MemberItem;
import com.example.administrator.pasugoon.lib.MyLog;

/**
 * Created by Administrator on 2017-10-18.
 */

public class MyApp extends Application {
    private MemberItem memberItem;
    private CheckItem checkItem;

    @Override
    public void onCreate() {
        super.onCreate();

        // FileUriExposedException 문제를 해결하기 위한 코드
        // 관련 설명은 책의 [참고] 페이지 참고
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public MemberItem getMemberItem() {
        if (memberItem == null) memberItem = new MemberItem();

        return memberItem;
    }

    public void setMemberItem(MemberItem item) {
        this.memberItem = item;
    }

    public int getMemberSeq() {
        return memberItem.seq;
    }

    public void setCheckItem(CheckItem checkItem) {
        this.checkItem = checkItem;
    }

    public CheckItem getCheckItem() {
        return checkItem;
    }
}
