package com.zgs.library.base.livedata

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*

/**
 * Description: 自动在UI销毁时移除msg和任务的Handler，不会有内存泄露
 * Create by dance, at 2019/5/21
 */
open class LifecycleHandler(private val lifecycleOwner: LifecycleOwner?)
    : Handler(Looper.getMainLooper()), DefaultLifecycleObserver {
    private val HANDLER = Handler(Looper.getMainLooper())
    init {
        runOnUiThread { lifecycleOwner?.lifecycle?.addObserver(this) }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        removeCallbacksAndMessages(null)
        lifecycleOwner?.lifecycle?.removeObserver(this)
    }
    open fun runOnUiThread(runnable: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
        } else {
            HANDLER.post(runnable)
        }
    }
}