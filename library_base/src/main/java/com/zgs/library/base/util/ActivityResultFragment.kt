package com.zgs.library.base.util

import android.content.Intent

import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


/**
 * 文件名：ActivityResultFragment <br/>
 * 描述：在非 onCreate 、onStart、onRestart 的生命周期方法回调中调用 registerForActivityResult
 *
 * @author zgs
 * @since 2023/04/14 14:34
 */
class ActivityResultFragment : Fragment() {
    private var mCallback: ActivityResultCallback<ActivityResult>? = null
    private var mIntent: Intent? = null
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityResultLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (mCallback != null) {
                    mCallback?.onActivityResult(result)
                }
                parentFragmentManager.beginTransaction()
                    .remove(this@ActivityResultFragment)
                    .commitAllowingStateLoss()
            }
        if (mIntent != null) {
            activityResultLauncher.launch(mIntent)
        }
    }

    private fun setIntent(intent: Intent) {
        mIntent = intent
    }

    private fun setCallback(callback: ActivityResultCallback<ActivityResult>) {
        mCallback = callback
    }

    override fun onDestroy() {
        mCallback = null
        super.onDestroy()
    }

    companion object {
        fun launchActivityResult(
            fragmentManager: FragmentManager,
            intent: Intent,
            callback: ActivityResultCallback<ActivityResult>
        ) {
            val fragment = ActivityResultFragment()
            fragment.setCallback(callback)
            fragment.setIntent(intent)
            fragmentManager.beginTransaction()
                .add(fragment, "ActivityResultFragment")
                .commitAllowingStateLoss()
        }
    }
}
