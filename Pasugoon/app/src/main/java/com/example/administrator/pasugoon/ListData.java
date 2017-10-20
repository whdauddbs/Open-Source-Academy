package com.example.administrator.pasugoon;

/**
 * Created by Administrator on 2017-10-20.
 */

public class ListData {
    private String text;
    private String imgName;

    ListData(String text, String imgName) {
        this.text = text;
        this.imgName = imgName;
    }


    public String getText() {
        return text;
    }

    public String getImgName() {
        return imgName;
    }
}
