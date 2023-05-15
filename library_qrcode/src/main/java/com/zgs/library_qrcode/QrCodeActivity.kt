package com.zgs.library_qrcode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.bingoogolapple.qrcode.zxing.ZXingView

class QrCodeActivity : AppCompatActivity(), QRCodeView.Delegate {
    var openFlash = false
    private lateinit var zxingView: ZXingView
    private lateinit var btnBack: ImageView
    private lateinit var btnFlash: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._ktx_activity_qr_code)
        zxingView = findViewById(R.id.zxingView)
        btnBack = findViewById(R.id.btnBack)
        btnFlash = findViewById(R.id.btnFlash)
        zxingView.setDelegate(this)
        btnBack.setOnClickListener {
            finish()
        }
        btnFlash.setOnClickListener {
            if (openFlash) {
                zxingView.closeFlashlight()
            } else {
                zxingView.openFlashlight()
            }
            openFlash = !openFlash
            btnFlash.setImageResource(if (openFlash) R.mipmap._ktx_flash_open else R.mipmap._ktx_flash_close)
        }
    }

    override fun onStart() {
        super.onStart()
        zxingView.startSpotAndShowRect()
    }

    override fun onStop() {
        zxingView.stopCamera()
        super.onStop()
    }

    override fun onDestroy() {
        zxingView.onDestroy()
        super.onDestroy()
    }

    override fun onScanQRCodeSuccess(result: String?) {
        val intent = Intent()
        intent.putExtra("result", result)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
    }

    override fun onScanQRCodeOpenCameraError() {
        Toast.makeText(this, "打开相机出错", Toast.LENGTH_SHORT).show()
    }
}