package com.example.administrator.pasugoon.remote;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

import com.example.administrator.pasugoon.item.MemberItem;
import com.example.administrator.pasugoon.item.CheckItem;

/**
 * Created by Administrator on 2017-10-18.
 */

public interface RemoteService {
    String BASE_URL = "http://10.53.128.104:3000";

    @GET("/member/{phone}")
    Call<MemberItem> selectMember(@Path("phone") String phone);

    /*@POST("/member/info")
    Call<String> insertMemberInfo(@Body MemberItem memberInfoItem);*/

    @FormUrlEncoded
    @POST("/member/phone")
    Call<String> insertMemberPhone(@Field("phone") String phone);

    /*@Multipart
    @POST("/member/icon_upload")
    Call<ResponseBody> uploadMemberIcon(@Part("member_seq") RequestBody memberSeq,
                                        @Part MultipartBody.Part file);*/

}
