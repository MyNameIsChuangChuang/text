package com.example.xiangji70.util;

import com.example.xiangji70.bean.Uptouxiang;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2018/3/24.
 */

public interface Testserivce {
    //第二种方式上传头像
    @POST("file/upload")
    @Multipart
    Observable<Uptouxiang> upload2(@QueryMap Map<String,String> map, @Part MultipartBody.Part file);
}
