package com.zgs.library_umeng

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.umeng.socialize.Config
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMMin
import com.umeng.socialize.media.UMWeb
import com.zgs.library_umeng.util.UmengUtil
import com.zgs.library_umeng.util.UmengUtil.convertPlatform


/**
 * 文件名：UmengLoginUtil <br/>
 * 描述：友盟第三方分享
 *
 * @author zgs
 * @since 2023/05/11 17:23
 */
object UmengShareUtil {




    /**
     * 分享网页
     */
    fun shareWeb(activity: Activity, platform: Platform, url: String, title: String,
                 thumbUrl: String? = null, thumbRes: Int? = null,
                 desc: String? = "", cb: UmengUtil.Callback? = null){
        val innerPlatform = convertPlatform(platform)
        if(!UmengUtil.checkInstall(activity, innerPlatform)) return
        val web = UMWeb(url)
        web.title = title
        if(thumbUrl!=null) web.setThumb(UMImage(activity, thumbUrl))
        if(thumbRes!=null) web.setThumb(UMImage(activity, thumbRes))
        web.description = desc
        ShareAction(activity)
                .setPlatform(innerPlatform)
                .withMedia(web)
                .setCallback(InnerShareCallback(cb = cb))
                .share()
    }

    /**
     * 分享图片
     *  imgUrl，bitmap，imgRes传一个即可
     */
    fun shareImage(activity: Activity, platform: Platform, imgUrl: String? = null,
                   bitmap: Bitmap? = null, imgRes: Int? = null, cb: UmengUtil.Callback? = null){
        val innerPlatform = convertPlatform(platform)
        if(!UmengUtil.checkInstall(activity, innerPlatform)) return
        var image:UMImage? = null
        if(imgUrl!=null)image = UMImage(activity, imgUrl)
        if(bitmap!=null)image = UMImage(activity, bitmap)
        if(imgRes!=null)image = UMImage(activity, imgRes)
        if(image==null)return
        ShareAction(activity)
                .setPlatform(innerPlatform)
                .withMedia(image)
                .setCallback(InnerShareCallback(cb = cb))
                .share()
    }
    /**
     * 分享文本
     */
    fun shareText(activity: Activity, platform: Platform, text: String, cb: UmengUtil.Callback? = null){
        val innerPlatform = convertPlatform(platform)
        if(!UmengUtil.checkInstall(activity, innerPlatform)) return
        ShareAction(activity)
                .setPlatform(innerPlatform)
                .withText(text)
                .setCallback(InnerShareCallback(cb = cb))
                .share()
    }


    /**
     * 分享微信小程序
     * @param miniAppId 不是小程序的id，是小程序的原始id，在小程序设置界面
     */
    fun shareMiniProgram(activity: Activity, url: String, bitmap: Bitmap? = null, imgRes: Int? = null, title: String, desc: String, path: String,
                         miniAppId: String, forTestVersion: Boolean = false,
                         forPreviewVersion: Boolean = false, cb: UmengUtil.Callback? = null) {
        if(!UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.WEIXIN)){
            Toast.makeText(activity,"未安装微信",Toast.LENGTH_SHORT).show()
            return
        }
        if (forTestVersion) {
            Config.setMiniTest()
        }
        if (forPreviewVersion) {
            Config.setMiniPreView()
        }
        val umMin = UMMin(url) //兼容低版本的网页链接
        val img = if (bitmap == null) UMImage(activity, imgRes!!) else UMImage(activity, bitmap)
        umMin.setThumb(img) // 小程序消息封面图片
        umMin.title = title // 小程序消息title
        umMin.description = desc // 小程序消息描述
        umMin.path = path //小程序页面路径
        umMin.userName = miniAppId // 小程序原始id,在微信平台查询
        ShareAction(activity)
                .withMedia(umMin)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(InnerShareCallback(cb = cb))
                .share()
    }


    /**
     * 直接打开小程序
     * @param miniAppId 不是小程序的id，是小程序的原始id，在小程序设置界面
     */
    fun openMiniProgram(activity: Activity, appId: String, miniAppId: String,
                        path: String, forTestVersion: Boolean = false,
                        forPreviewVersion: Boolean = false) {
        if(!UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.WEIXIN)){
            Toast.makeText(activity,"未安装微信",Toast.LENGTH_SHORT).show()
            return
        }
        val api = WXAPIFactory.createWXAPI(activity, appId)
        val req: WXLaunchMiniProgram.Req = WXLaunchMiniProgram.Req()
        req.userName = miniAppId // 填小程序原始id
        req.path = path ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        if (forTestVersion) {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_TEST // 可选打开 开发版，体验版和正式版
        } else if (forPreviewVersion) {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW // 可选打开 开发版，体验版和正式版
        } else {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE // 可选打开 开发版，体验版和正式版
        }
        api.sendReq(req)
    }



    class InnerShareCallback(var cb: UmengUtil.Callback?) : UMShareListener{
        override fun onResult(p: SHARE_MEDIA) {
            Log.e("share","share->onResult $p")
            cb?.onComplete()
        }
        override fun onCancel(p: SHARE_MEDIA) {
            Log.e("share","share->onResult $p")
            cb?.onCancel()
        }
        override fun onError(p: SHARE_MEDIA, t: Throwable) {
            Log.e("share","share->onError $p  ${t.message}")
            cb?.onError(t)
        }
        override fun onStart(p: SHARE_MEDIA) {
            Log.e("share","share->onStart $p")
            cb?.onStart()
        }
    }



    /**
     * 调用确保分享能正确回调
     */
    fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?){
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data)
    }
}