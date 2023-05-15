package com.zgs.library_umeng

import android.app.Application
import android.content.Context
import com.tencent.tauth.Tencent
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import com.zgs.library_umeng.SdkKeyConfig.umAppkey
import com.zgs.library_umeng.SdkKeyConfig.umChannel
import com.zgs.library_umeng.SdkKeyConfig.umengMessageSecret

/**
 * 文件名：LoadSdk <br/>
 * 描述：
 *
 * @author zgs
 * @since 2023/01/31 18:14
 */
object LoadSdk {

    fun init(context: Context) {
        val app = context as? Application ?: return
        umPreInit(app)
        umInit(app)
    }


    /**
     * 预加载友盟sdk
     *
     * @param application
     */
    private fun umPreInit(application: Application) {
        UMConfigure.preInit(application, umAppkey, umChannel)
    }

    /**
     * 初始化友盟sdk
     *
     * @param application
     */
    fun umInit(application: Application) {
        UMConfigure.init(
            application,
            umAppkey,
            "Umeng",
            UMConfigure.DEVICE_TYPE_PHONE,
            umengMessageSecret
        )
        // 微信设置
        if (SdkKeyConfig.wxAppKey.isNotEmpty()) {
            PlatformConfig.setWeixin(SdkKeyConfig.wxAppKey, SdkKeyConfig.wxAppSecret);
            PlatformConfig.setWXFileProvider("${application.packageName}.fileprovider")
        }

        // QQ设置
        if (SdkKeyConfig.qqAppId.isNotEmpty()) {
            Tencent.setIsPermissionGranted(true)        //QQ官方sdk授权
            PlatformConfig.setQQZone(SdkKeyConfig.qqAppId, SdkKeyConfig.qqKey);
            PlatformConfig.setQQFileProvider("${application.packageName}.fileprovider")
        }

        // 新浪微博设置
        if (SdkKeyConfig.weiboAppId.isNotEmpty()) {
            PlatformConfig.setSinaWeibo(
                SdkKeyConfig.weiboAppId, SdkKeyConfig.weiboKey,
                "http://sns.whalecloud.com"
            )
            PlatformConfig.setSinaFileProvider("${application.packageName}.fileprovider")

        }

        //钉钉设置
        if (SdkKeyConfig.ddAppId.isNotEmpty()) {
            PlatformConfig.setDing(SdkKeyConfig.ddAppId);
            PlatformConfig.setDingFileProvider("${application.packageName}.fileprovider")
        }

        //抖音设置
        if (SdkKeyConfig.douyinAppId.isNotEmpty()) {
            PlatformConfig.setBytedance(
                SdkKeyConfig.douyinAppId,
                SdkKeyConfig.douyinKey,
                SdkKeyConfig.douyinSecret,
                "${application.packageName}.fileprovider"
            )
        }


    }

}