package com.zgs.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.zgs.library.base.ext.singleClick
import com.zgs.library_umeng.Platform
import com.zgs.library_umeng.UmengLoginUtil
import com.zgs.library_umeng.UmengShareUtil
import com.zgs.library_umeng.bean.LoginData
import com.zgs.library_umeng.util.UmengUtil
import com.zgs.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var mBing: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBing =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                Log.e("lifecycle", event.name)
            }

        })
        mBing.floatingButton.singleClick {
            UmengShareUtil.shareWeb(this@MainActivity, platform = Platform.WxFriend,
                url = "https://www.baidu.com", title = "三生三世",
                thumbRes = R.mipmap.ic_launcher, cb = object : UmengUtil.Callback {

                })

        }
        mBing.floatingButton2.singleClick {

            UmengLoginUtil.wxLogin(this, false, object : UmengUtil.Callback {
                override fun onComplete(loginData: LoginData?) {
                    super.onComplete(loginData)
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}