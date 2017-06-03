package com.hengda.zwf.androidpay;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * 支付宝支付
 *
 * @author 祝文飞（Tailyou）
 * @time 2017/3/20 8:51
 */
public class Alipay {

    private static final Object mLock = new Object();
    private static Alipay mAlipay;
    private PayTask mPayTask;
    private Handler handler;
    private AlipayResultCallBack mCallback;

    public static final int ERROR_RESULT = 1;   //支付结果解析错误
    public static final int ERROR_PAY = 2;      //支付失败
    public static final int ERROR_NETWORK = 3;  //网络连接错误

    public interface AlipayResultCallBack {
        void onSuccess();               //支付成功

        void onDealing();               //正在处理中

        void onError(int error_code);   //支付失败

        void onCancel();                //支付取消
    }

    /**
     * 私有构造方法
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/3/20 8:50
     */
    private Alipay(Activity context) {
        mPayTask = new PayTask(context);
        handler = new Handler();
    }

    /**
     * 单例
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/3/20 8:50
     */
    public static Alipay getInstance(Activity context) {
        if (mAlipay == null) {
            synchronized (mLock) {
                if (mAlipay == null) {
                    mAlipay = new Alipay(context);
                }
            }
        }
        return mAlipay;
    }

    //发起支付
    public void doPay(final String mParams, AlipayResultCallBack callback) {
        mCallback = callback;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Map<String, String> pay_result = mPayTask.payV2(mParams, true);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCallback == null) {
                            return;
                        }
                        if (pay_result == null) {
                            mCallback.onError(ERROR_RESULT);
                            return;
                        }
                        String resultStatus = pay_result.get("resultStatus");
                        if (TextUtils.equals(resultStatus, "9000")) {               //支付成功
                            mCallback.onSuccess();
                        } else if (TextUtils.equals(resultStatus, "8000")) {        //等待支付结果确认
                            mCallback.onDealing();
                        } else if (TextUtils.equals(resultStatus, "6001")) {        //支付取消
                            mCallback.onCancel();
                        } else if (TextUtils.equals(resultStatus, "6002")) {        //网络连接出错
                            mCallback.onError(ERROR_NETWORK);
                        } else if (TextUtils.equals(resultStatus, "4000")) {        //支付错误
                            mCallback.onError(ERROR_PAY);
                        }
                    }
                });
            }
        }).start();
    }
}
