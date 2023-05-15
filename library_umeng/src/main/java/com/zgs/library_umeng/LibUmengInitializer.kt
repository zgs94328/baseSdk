package com.zgs.library_umeng
import android.content.Context
import androidx.startup.Initializer

/**
 * 文件名：LibBaseInitializer <br/>
 * 描述：
 *
 * @author zgs
 * @since 2023/02/01 11:47
 */
class LibUmengInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        LoadSdk.init(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}