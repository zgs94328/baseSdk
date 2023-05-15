package com.zgs.library.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import android.util.Log

/**
 * 文件名：AppUtils <br/>
 * 描述：
 *
 * @author zgs
 * @since 2023/02/08 18:06
 */
object AppUtils {
    private lateinit var sApp: Application

    /**
     * Init utils.
     *
     * Init it in the class of UtilsFileProvider.
     *
     * @param app application
     */
    fun init(app: Application?) {
        if (app == null) {
            Log.e("Utils", "app is null.")
            return
        }
        if (::sApp.isInitialized) return
        sApp = app
    }

    /**
     * Return the Application object.
     *
     * Main process get app by UtilsFileProvider,
     * and other process get app by reflect.
     *
     * @return the Application object
     */
    fun getApplication(): Application {
        return sApp
    }

    /**
     * context转Activity
     *
     * @param context
     * @return
     */
    fun getActivityFromContext(context: Context?): Activity? {
        if (context == null) {
            return null
        }
        if (context is Activity) {
            return context
        }
        if (context is Application || context is Service) {
            return null
        }
        var c = context
        while (c != null) {
            if (c is ContextWrapper) {
                c = c.baseContext
                if (c is Activity) {
                    return c
                }
            } else {
                return null
            }
        }
        return null
    }
}