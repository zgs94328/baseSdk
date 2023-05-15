package com.zgs.library.base

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.tencent.mmkv.MMKV
import me.jessyan.autosize.utils.AutoSizeLog.isDebug

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
        initMMKV(app)
        initArouter(app)
    }

    /**
     * 初始化arouter
     *
     * @param application
     */
    private fun initArouter(application: Application) {
        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application); // 尽可能早，推荐在Application中初始化
    }

    /**
     * 初始化MMKV
     *
     * @param application
     */
    private fun initMMKV(application: Application) {
        MMKV.initialize(application)
    }
}