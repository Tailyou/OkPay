## 一、概述
移动支付框架，封装了微信支付、支付宝支付，两种支付方式保持了统一的使用风格，使用方便。

## 二、版本
最新版本0.0.1，compile 'com.hengda.zwf:AndroidPay:0.0.1'

## 三、使用

### 1、修改AndroidManifest.xml

```
        <activity
            android:name="com.hengda.zwf.androidpay.WXPayCallbackActivity"
            android:configChanges="orientation|keyboardHidden|navigation|screenSize"
            android:launchMode="singleTop"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />
        <activity-alias
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:targetActivity="com.hengda.zwf.androidpay.WXPayCallbackActivity" />
```

### 2、微信支付

```java
/**
     * 微信支付
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/3/20 10:13
     */
    public void doWxpay(View view) {
        String payParam = "";//pay param form server
        WechatPay.getInstance(this, "wxff989c97f26783b1").doPay(payParam, new WechatPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplication(), "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WechatPay.NO_OR_LOW_WX:
                        Toast.makeText(getApplication(), "未安装微信或微信版本过低", Toast.LENGTH_SHORT).show();
                        break;
                    case WechatPay.ERROR_PAY_PARAM:
                        Toast.makeText(getApplication(), "参数错误", Toast.LENGTH_SHORT).show();
                        break;
                    case WechatPay.ERROR_PAY:
                        Toast.makeText(getApplication(), "支付失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplication(), "支付取消", Toast.LENGTH_SHORT).show();
            }
        });
    }
```

### 3、支付宝支付

```java
 /**
     * 支付宝支付
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/3/20 10:13
     */
    public void doAlipay(View view) {
        //pay param form server
        String payParam = "pay param form server";
        Alipay.getInstance(this).doPay(payParam, new Alipay.AlipayResultCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplication(), "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDealing() {
                Toast.makeText(getApplication(), "支付处理中...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case Alipay.ERROR_RESULT:
                        Toast.makeText(getApplication(), "支付失败:支付结果解析错误", Toast.LENGTH_SHORT).show();
                        break;
                    case Alipay.ERROR_NETWORK:
                        Toast.makeText(getApplication(), "支付失败:网络连接错误", Toast.LENGTH_SHORT).show();
                        break;
                    case Alipay.ERROR_PAY:
                        Toast.makeText(getApplication(), "支付错误:支付码支付失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplication(), "支付错误", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplication(), "支付取消", Toast.LENGTH_SHORT).show();
            }
        });
    }
```

## 四、使用
具体使用见demo。
项目地址：https://git.oschina.net/tailyou/HD_Frame_AndroidPay