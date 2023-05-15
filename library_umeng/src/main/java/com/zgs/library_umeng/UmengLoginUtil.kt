package com.zgs.library_umeng

import android.app.Activity
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareConfig
import com.zgs.library_umeng.util.UmengUtil

/**
 * 文件名：UmengLoginUtil <br/>
 * 描述：友盟第三方登录
 *
 * @author zgs
 * @since 2023/05/11 17:23
 */
object UmengLoginUtil {


    fun wxLogin(
        activity: Activity,
        alwaysNeedConfirm: Boolean = false,
        callback: UmengUtil.Callback? = null
    ) {
        innerLogin(
            activity,
            Platform.WxFriend,
            alwaysNeedConfirm = alwaysNeedConfirm,
            callback = callback
        )
    }

    fun qqLogin(
        activity: Activity,
        alwaysNeedConfirm: Boolean = false,
        callback: UmengUtil.Callback? = null
    ) {
        innerLogin(
            activity,
            Platform.QQ,
            alwaysNeedConfirm = alwaysNeedConfirm,
            callback = callback
        )
    }

    fun sinaLogin(
        activity: Activity,
        alwaysNeedConfirm: Boolean = false,
        callback: UmengUtil.Callback? = null
    ) {
        innerLogin(
            activity,
            Platform.Sina,
            alwaysNeedConfirm = alwaysNeedConfirm,
            callback = callback
        )
    }

    private fun innerLogin(
        activity: Activity,
        platform: Platform,
        alwaysNeedConfirm: Boolean = false,
        callback: UmengUtil.Callback? = null
    ) {
        val innerPlatform = UmengUtil.convertPlatform(platform)
        if (!UmengUtil.checkInstall(activity, innerPlatform)) return
        if (alwaysNeedConfirm) {
            val config = UMShareConfig()
            config.isNeedAuthOnGetUserInfo(true)
            UMShareAPI.get(activity).setShareConfig(config)
        }
        UMShareAPI.get(activity).getPlatformInfo(
            activity, innerPlatform,
            UmengUtil.OauthCallback(callback)
        )
    }
}