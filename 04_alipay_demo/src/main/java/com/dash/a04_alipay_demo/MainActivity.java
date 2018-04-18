package com.dash.a04_alipay_demo;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dash.a04_alipay_demo.alipay.AlipayUtil;
import com.dash.a04_alipay_demo.alipay.PayResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    //使用返回的支付结果字符串构建一个支付结果对象
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&docType=1) 建议商户依赖异步通知
                     */
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    //----------app端拿到支付宝同步返回的结果,需要发送给服务器端,服务器端经过验证签名和解析支付结果,然后给app端返回最终支付的结果

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {

                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。



                        Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(MainActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }

                    //---------->需要根据是否支付成功跳转对应的页面

                    /**
                     * 不管订单支付成功或者失败 订单是已经生成了 如果成功 跳转订单列表的已支付,,,如果失败跳转的是订单列表的待支付
                     */

                    break;
                }
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 真是开发过程中 这些参数是都不能出现在代码中的 尤其是私钥 放在服务器端
     */
    private final String PARTNER = "2088221871911835"; //Pid
    private final String SELLER = "zhuangshiyigou@163.com";// 商户收款账号
    // 商户私钥，pkcs8格式
    private final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALmhW0c+ZO7sB3utNmkQu5hkpqnw+nAPLCdBoPEw+E+7qcOZBdXxwZsG05yr2BYp5j0MTE0FpKRu+uNTVTAX/MzhcInAColtnWkF1R8rsIdlLdleRoMcM3Lo4XesctdsyxLeummrFLKQfASMqS64kkTUoEFGb3tlYZs7iSFgpM1pAgMBAAECgYAGm6zhK2JycuqNR4xBTzwuX57jO9XeeVvMBfURwPmF9RtFAESJ6jJHL4YG9MMbfuBYWgC5WTMUO3Mo9oV40dHI9dPwANL5aPeKEIUawoQyuCyY/js84fOY3+TbEnXym8G6+Zxm+bGKQn3ZZqlSgR3CHk5f/CceoXvPWDLwl//RtQJBANzDaQTeckTUEtxVyZ9vM26Sv+TKULvqf8OHdrGm7WOWY6/CbChrxMKHxkdrNwZPNIhozNfTaOmnJaPqeMKo8SMCQQDXQmEPHj0h4Qjn3D6n6WiNOvUsNbzVpLP/TgwHFYkRLcz+GPRkbXXdvkSUKxNo7vwZr8vwTIquYA+K3CFTpr4DAkEAu3Ox2NCJdqgc27p8WUSzB1DUYBDqPKYBlqWPw4laSRWJz9Pmwuu/Ru7DDiGbt1/J24ohZaG9k6i57VVK9P8+wQJBAIgGtFrfWvY7xGrwbM+i2aTVqvTDCI9hQzWEVmlrnHA0pyOzFU0ZNrBneeK/zcYzry90PcWeOMy0e13eeVjpN40CQQCMBjVBeTdQ9afgGnBR6glIWrCtqTBAsUIr3gvNZYWdaznr0FmG2pjLwDLUsx0SUCpcrTQxhWu16HDEyQuCm7Ar";

    public void sunbmitOrder(View view) {

        //只需要在客户端准备商品的名称/描述/价格 然后调用自己服务器的接口 获取签名后的订单信息即可

        //中间加密+签名+编码这样的过程是在服务器端进行的---->后台需要有Pid商户的id,SELLER商户的收款账号,RSA_PRIVATE商户的私钥

        //需要拿到签名和加密后的订单信息 然后异步调用支付的接口

        //这里需要自己根据实际去传值
        //这是一个还没有完全符合支付宝规范的订单信息
        String orderInfo = AlipayUtil.getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01",PARTNER,SELLER);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！...实际sign签名的字符串是通过与公司服务器签名得到的
         */
        String sign = AlipayUtil.sign(orderInfo,RSA_PRIVATE);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        /**
         * 这个就是符合支付宝规范的订单信息
         *
         * 1.签名操作
         */
        final String signOrderInfo = orderInfo + "&sign=\"" + sign + "\"&" + AlipayUtil.getSignType();

        /**
         * 由于没有后台 这个demo案例把上面获取订单信息的过程写在本地 ,
         * 此处需要把Pid商户的id,SELLER商户的收款账号,RSA_PRIVATE商户的私钥 暴露在本地代码中
         * 而实际不是暴露在本地的
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //开启异步调用支付宝支付接口
                //1.获取支付宝支付的任务对象
                PayTask payTask = new PayTask(MainActivity.this);
                //2.拿着任务对象 调用支付的接口...signOrderInfo经过签名的订单信息
                String result = payTask.pay(signOrderInfo, true);


                //3.把获取的支付结果信息发送到主线程进行操作
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }).start();


    }
}
