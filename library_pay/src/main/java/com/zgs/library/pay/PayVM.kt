package com.zgs.library.pay

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alipay.sdk.app.PayTask
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlin.concurrent.thread

/**
 * Description:
 * Create by dance, at 2019/6/28
 */
object PayVM : ViewModel() {

    var wxAppId = ""
    val aliPayData = MutableLiveData<AliPayResult?>()
    val wxPayData = MutableLiveData<WxPayResult>()
    fun aliPay(orderParam: String, activity: Activity) {
        thread(start = true) {
            val result = PayTask(activity).payV2(orderParam, true)
            aliPayData.postValue(AliPayResult.fromMap(result))
        }
    }

    fun wxPay(
        context: Context, appId: String, partnerId: String, prepayId: String,
        nonceStr: String, timeStamp: String, packageValue: String = "Sign=WXPay",
        sign: String, extData: String = ""
    ) {
        wxAppId = appId
        val wxapi = WXAPIFactory.createWXAPI(context, appId)
        wxapi.registerApp(appId)
        if (!wxapi.isWXAppInstalled) {
            Toast.makeText(context, "未安装微信，无法支付", Toast.LENGTH_SHORT).show()
            return
        }
        val req = PayReq()
        req.appId = appId
        req.partnerId = partnerId
        req.prepayId = prepayId
        req.nonceStr = nonceStr
        req.timeStamp = timeStamp
        req.packageValue = packageValue
        req.sign = sign
        if (!extData.isNullOrEmpty()) req.extData = extData // optional
        wxapi.sendReq(req)
    }

    fun wxPay2(
        context: Context, param: WxPayParam
    ) {
        wxAppId = param.appId
        val wxapi = WXAPIFactory.createWXAPI(context, param.appId)
        wxapi.registerApp(param.appId)
        if (!wxapi.isWXAppInstalled) {
            Toast.makeText(context, "未安装微信，无法支付", Toast.LENGTH_SHORT).show()
            return
        }
        val req = PayReq()
        req.appId = param.appId
        req.partnerId = param.partnerId
        req.prepayId = param.prepayId
        req.nonceStr = param.nonceStr
        req.timeStamp = param.timeStamp
        req.packageValue = param.packageValue
        req.sign = param.sign
        if (!param.extData.isNullOrEmpty()) req.extData = param.extData // optional
        wxapi.sendReq(req)
    }
}