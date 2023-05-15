package com.zgs.library.base.ext

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.zgs.library.base.AppUtils

/**
 * 作者　: zgs
 * 时间　: 2021/3/17
 * 描述　:
 */
private val mHandler = Handler(Looper.getMainLooper())
private var mToast: Toast? = null


fun String.toast(timeLong: Boolean = false) {
    show(
        this,
        timeLong
    )
}

private fun Any.show(msg: CharSequence?, timeLong: Boolean = false) {
    runOnUiThread(Runnable {
        if (mToast != null) {
            mToast!!.cancel()
        }
        val duration = if (timeLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        mToast =
            Toast.makeText(AppUtils.getApplication(), msg, duration)
        mToast?.show()
    })
}

private fun runOnUiThread(runnable: Runnable) {
    if (Looper.getMainLooper() == Looper.myLooper()) {
        runnable.run()
    } else {
        mHandler.post(runnable)
    }
}