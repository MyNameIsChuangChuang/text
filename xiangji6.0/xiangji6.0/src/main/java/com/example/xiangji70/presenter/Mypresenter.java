package com.example.xiangji70.presenter;

import com.example.xiangji70.bean.Uptouxiang;
import com.example.xiangji70.mode.Mymode;
import com.example.xiangji70.view.Iview;

import java.io.File;

/**
 * Created by Administrator on 2018/3/24.
 */

public class Mypresenter {
    private Iview iview;
    private Mymode mymode;

    public Mypresenter(Iview iview) {
        this.iview = iview;
        mymode=new Mymode();
    }

    public void uptouxiang(String uid, File file){
        mymode.uptouxiang(uid, file, new Mymode.Uptou() {
            @Override
            public void upTouxiang(Uptouxiang uptouxiang) {
                if (uptouxiang.getCode().equals("0")){
                    iview.chenggong("上传成功");
                }else {
                    iview.shibai(uptouxiang.getMsg());
                }
            }
        });
    }

}
