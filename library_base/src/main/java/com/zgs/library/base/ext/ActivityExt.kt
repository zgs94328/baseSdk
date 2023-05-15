package com.zgs.library.base.ext

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zgs.library.base.AppUtils
import com.zgs.library.base.livedata.LifecycleHandler

/**
 * 文件名：ActivityExt <br/>
 * 描述：
 *
 * @author zgs
 * @since 2023/02/13 16:45
 */
//post, postDelay
fun FragmentActivity.post(action: () -> Unit) {
    LifecycleHandler(this).post { action() }
}

fun FragmentActivity.postDelay(delay: Long = 0, action: () -> Unit) {
    LifecycleHandler(this).postDelayed({ action() }, delay)
}

/**
 * saved state view model，要求ViewModel的构造必须接受SavedStateHandle类型的参数，比如：
 * ```
 * class DemoVM( handler: SavedStateHandle): ViewModel()
 * ```
 */
fun <T : ViewModel> FragmentActivity.getSavedStateVM(clazz: Class<T>) = ViewModelProvider(
    this, SavedStateViewModelFactory(
        AppUtils.getApplication(), this
    )
).get(clazz)