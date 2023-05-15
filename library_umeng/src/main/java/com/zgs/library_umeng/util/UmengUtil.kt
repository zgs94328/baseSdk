package com.zgs.library_umeng.util

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import com.zgs.library_umeng.Platform
import com.zgs.library_umeng.bean.LoginData

/**
 * 文件名：UmengUtil <br/>
 * 描述：
 *
 * @author zgs
 * @since 2023/05/11 17:26
 */
object UmengUtil {
    /**
     * 检查是否安装应用
     *
     * @param activity
     * @param platform
     * @return
     */
    fun checkInstall(activity: Activity, platform: SHARE_MEDIA): Boolean {
        if (!UMShareAPI.get(activity).isInstall(activity, platform)) {
            Toast.makeText(activity, "未安装${getAppNameByPlatform(platform)}", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }

    /**
     * 获取platform对应的应用名称
     *
     * @param platform
     */
    private fun getAppNameByPlatform(platform: SHARE_MEDIA) = when (platform) {
        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE -> "微信"
        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE -> "QQ"
        SHARE_MEDIA.SINA -> "新浪微博"
        SHARE_MEDIA.ALIPAY -> "支付宝"
        SHARE_MEDIA.DINGTALK -> "钉钉"
        SHARE_MEDIA.DOUBAN -> "豆瓣"
        SHARE_MEDIA.DROPBOX -> "DROPBOX"
        SHARE_MEDIA.FACEBOOK, SHARE_MEDIA.FACEBOOK_MESSAGER -> "Facebook"
        SHARE_MEDIA.TWITTER -> "Twitter"
        SHARE_MEDIA.WHATSAPP -> "WhatsApp"
        SHARE_MEDIA.EVERNOTE -> "Evernote"
        SHARE_MEDIA.INSTAGRAM -> "Instagram"
        SHARE_MEDIA.LINE -> "Line"
        SHARE_MEDIA.LINKEDIN -> "LinkedIn"
        else -> "目标App"
    }
     fun convertPlatform(platform: Platform) = when (platform) {
        Platform.WxFriend -> SHARE_MEDIA.WEIXIN
        Platform.WxCircle -> SHARE_MEDIA.WEIXIN_CIRCLE
        Platform.QQ -> SHARE_MEDIA.QQ
        Platform.QZone -> SHARE_MEDIA.QZONE
        Platform.Sina -> SHARE_MEDIA.SINA
    }

    /**
     * 认证回调
     *
     * @property cb
     */
    class OauthCallback(var cb: Callback?) : UMAuthListener {
        override fun onComplete(p: SHARE_MEDIA, p1: Int, map: MutableMap<String, String>?) {
            Log.e("share","UMAuthListener->onComplete：$p $map")
            cb?.onComplete(if (map == null) null else LoginData.fromMap(map))
        }
        override fun onError(p: SHARE_MEDIA, p1: Int, t: Throwable) {
            Log.e("share","UMAuthListener->onError：$p ${t.message}")
            cb?.onError(t)
        }
        override fun onStart(p: SHARE_MEDIA) {
            Log.e("share","UMAuthListener->onStart：$p")
            cb?.onStart()
        }
        override fun onCancel(p: SHARE_MEDIA, p1: Int) {
            Log.e("share","UMAuthListener->onCancel：$p")
            cb?.onCancel()
        }
    }
    fun deleteOauth(activity: Activity, platform: Platform, callback: Callback? = null) {
        UMShareAPI.get(activity).deleteOauth(activity, convertPlatform(platform),
            UmengUtil.OauthCallback(callback)
        )
    }
    interface Callback {
        fun onCancel() {}
        fun onStart() {}
        fun onError(t: Throwable) {}
        fun onComplete(loginData: LoginData? = null) {}
    }
}