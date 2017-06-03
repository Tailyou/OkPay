package com.hengda.pay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hengda.zwf.androidpay.Alipay;
import com.hengda.zwf.androidpay.WechatPay;
import com.smart.pang.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

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

    /**
     * 支付宝支付
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/3/20 10:13
     */
    public void doAlipay(View view) {
        //pay param form server
        String payParam = "app_id=2017010904947813&biz_content=%7B%22out_trade_no%22%3A%221706263126%22%2C%22" +
                "subject%22%3A%22%5Cu5468%5Cu5e84%5Cu68a6%5Cu8776+-+%5Cu60a6%5Cu8bfb%5Cu4e0a%5Cu6d77%22%2C%22" +
                "total_amount%22%3A%220.01%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=utf-8" +
                "&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F211.144.104.253%3A65529%2Falinotify" +
                "&sign=JU9UqRAsrmY%2FT5JSpCUqtXbZr%2Bck0BMI1yWgE5oiPSs0o%2B7DnGonnMDfuPDvGbTMOZz9uJzWk1hEQOB" +
                "hv01DzCwGVs%2BWgcgmgWFqXqE7X6vra%2FJb1qAWll1DCgFyTY9Ea5%2BGrp%2BAv7U54FOOxz068YqFVpQlyvUis7" +
                "CyQ2Bpkm5lWGZME9Q45InsS0gD82FA1gpngUNfndZ3%2FmoBebd0mRoilFSRMUHkLHmjQEVz8biMpr1%2FjJOHodbjJ" +
                "ipLQ1%2BxAwuCf1z17Nb0R7vBcvDSmcFqbweyY3jyom1cpwZeMuXFjDx%2FRFVuS28dtzpu63UxM9Sav%2B0jk84pw" +
                "QvlSetiLw%3D%3D&sign_type=RSA2&timestamp=2017-03-04+17%3A02%3A47&version=1.0";
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

}
