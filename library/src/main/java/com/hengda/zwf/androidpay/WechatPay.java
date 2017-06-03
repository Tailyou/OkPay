package com.hengda.zwf.androidpay;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 微信支付
 *
 * @author 祝文飞（Tailyou）
 * @time 2017/3/20 8:50
 */
public class WechatPay {

    private static final Object mLock = new Object();
    private static WechatPay mWechatPay;
    private IWXAPI mWXApi;
    private WXPayResultCallBack mCallback;

    public static final int NO_OR_LOW_WX = 1;       //未安装微信或微信版本过低
    public static final int ERROR_PAY_PARAM = 2;    //支付参数错误
    public static final int ERROR_PAY = 3;          //支付失败

    public interface WXPayResultCallBack {
        void onSuccess();               //支付成功

        void onError(int error_code);   //支付失败

        void onCancel();                //支付取消
    }

    /**
     * 私有构造方法
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/3/20 8:50
     */
    private WechatPay(Activity context, String wx_appid) {
        mWXApi = WXAPIFactory.createWXAPI(context, wx_appid);
        mWXApi.registerApp(wx_appid);
    }

    /**
     * 单例
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/3/20 8:50
     */
    public static WechatPay getInstance(Activity context, String wx_appid) {
        if (mWechatPay == null) {
            synchronized (mLock) {
                if (mWechatPay == null) {
                    mWechatPay = new WechatPay(context, wx_appid);
                }
            }
        }
        return mWechatPay;
    }

    //发起支付
    public void doPay(PayReq req, WXPayResultCallBack callback) {
        mCallback = callback;
        if (!check()) {
            if (mCallback != null) {
                mCallback.onError(NO_OR_LOW_WX);
            }
            return;
        }
        if (req == null) return;
        //mWXApi.openWXApp();
        mWXApi.sendReq(req);
    }

    //发起支付
    public void doPay(String payParam, WXPayResultCallBack callback) {
        //PayReq req = genPayReq(payParam);
        PayReq req = new PayReq();
        req.appId = "wxff989c97f26783b1";
        req.partnerId = "1433741002";
        req.prepayId = "wx20170307112556bd9969dbfd0294057248";
        req.packageValue = "Sign=WXPay";
        req.nonceStr = "evvVLW";
        req.timeStamp = "1488857156";
        req.sign = "7A5D7756CB59DDADB445F6FFC921EAF8";
        doPay(req, callback);
    }

    @Nullable
    private PayReq genPayReq(String payParam) {
        JSONObject param;
        try {
            param = new JSONObject(payParam);
        } catch (JSONException e) {
            e.printStackTrace();
            if (mCallback != null) {
                mCallback.onError(ERROR_PAY_PARAM);
            }
            return null;
        }
        if (TextUtils.isEmpty(param.optString("appid")) || TextUtils.isEmpty(param.optString("partnerid"))
                || TextUtils.isEmpty(param.optString("prepayid")) || TextUtils.isEmpty(param.optString("package")) ||
                TextUtils.isEmpty(param.optString("noncestr")) || TextUtils.isEmpty(param.optString("timestamp")) ||
                TextUtils.isEmpty(param.optString("sign"))) {
            if (mCallback != null) {
                mCallback.onError(ERROR_PAY_PARAM);
            }
            return null;
        }
        PayReq req = new PayReq();
        req.appId = param.optString("appid");
        req.partnerId = param.optString("partnerid");
        req.prepayId = param.optString("prepayid");
        req.packageValue = param.optString("package");
        req.nonceStr = param.optString("noncestr");
        req.timeStamp = param.optString("timestamp");
        req.sign = param.optString("sign");
        return req;
    }

    //检测是否支持微信支付
    private boolean check() {
        return mWXApi.isWXAppInstalled() && mWXApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }

    protected static WechatPay getInstance() {
        return mWechatPay;
    }

    protected IWXAPI getWXApi() {
        return mWXApi;
    }

    //支付回调响应
    public void onResp(int error_code) {
        if (mCallback == null) {
            return;
        }
        if (error_code == 0) {   //成功
            mCallback.onSuccess();
        } else if (error_code == -1) {   //错误
            mCallback.onError(ERROR_PAY);
        } else if (error_code == -2) {   //取消
            mCallback.onCancel();
        }
        mCallback = null;
    }

}
