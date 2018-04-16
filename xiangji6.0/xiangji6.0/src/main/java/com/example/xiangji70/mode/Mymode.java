package com.example.xiangji70.mode;

import android.util.Log;

import com.example.xiangji70.bean.Uptouxiang;
import com.example.xiangji70.util.Getnet;
import com.example.xiangji70.util.Util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/3/24.
 */

public class Mymode {

    public void uptouxiang(String uid, File file, final Uptou uptou){
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file",file.getName(), RequestBody.create(
                MediaType.parse("application/octet-stream"),file));
        Map<String,String> map=new HashMap<>();
        map.put("uid",uid);
        Observable<Uptouxiang> uptouxiangObservable = Util.getmInstance().getnetjson(Getnet.net).upload2(map, filePart);
        uptouxiangObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Uptouxiang>() {
                    @Override
                    public void accept(Uptouxiang uptouxiang) throws Exception {
                        Log.d("走没走", "accept: "+uptouxiang);
                     uptou.upTouxiang(uptouxiang);
                    }
                });
    }

    public interface Uptou{
        void upTouxiang(Uptouxiang uptouxiang);
    }

}
