package com.zgs.library_qrcode

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.zgs.library.base.util.ActivityResultFragment
import kotlinx.coroutines.*

/**
 * 二维码/条形码工具类
 */
object QrCodeUtil {
    var requestCode = 1

    /**
     * 开启二维码扫描界面
     */
    fun scan(context: Context, reqCode: Int = 1, success: (String) -> Unit) {
        requestCode = reqCode
        XXPermissions.with(context).permission(Permission.Group.STORAGE)
            .permission(Permission.CAMERA).request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (!allGranted) {
                        Toast.makeText(context, "获取部分权限成功，但部分权限未正常授予", Toast.LENGTH_SHORT).show()
                        return
                    }
                    val intent = Intent(context, QrCodeActivity::class.java)
                    (context as? FragmentActivity)?.apply {
                        ActivityResultFragment.launchActivityResult(
                            supportFragmentManager,
                            intent
                        ) {
                            //此处进行数据接收（接收回调）
                            if (it.resultCode == Activity.RESULT_OK) {
                                //成功数据
                                success.invoke(fetchResult(it.data))
                            }

                        }
                    }

                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    if (doNotAskAgain) {
                        Toast.makeText(context, "被永久拒绝授权，请手动授予录音和日历权限", Toast.LENGTH_SHORT).show()
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(context, permissions)
                    } else {
                        Toast.makeText(context, "权限拒绝，无法使用扫描功能", Toast.LENGTH_SHORT).show()
                    }
                }

            })

    }

    /**
     * 生成二维码
     */
    fun genQrCode(
        context: Context,
        content: String,
        size: Int,
        logo: Bitmap? = null,
        onFinish: ((Bitmap?) -> Unit)
    ) {
        (context as? FragmentActivity)?.let {
            it.lifecycleScope.launch {
                var bitmap: Bitmap?
                withContext(Dispatchers.IO) {
                    bitmap = if (logo != null) {
                        QRCodeEncoder.syncEncodeQRCode(content, size, Color.BLACK, logo)
                    } else {
                        QRCodeEncoder.syncEncodeQRCode(content, size)
                    }
                }
                onFinish.invoke(bitmap)
            }
        }

    }

    /**
     * 从Bitmap图片解析出二维吗
     */
    fun parseQrCode(context: Context, bitmap: Bitmap, onFinish: (String?) -> Unit) {

        (context as? FragmentActivity)?.let {
            it.lifecycleScope.launch {
                var str: String?
                withContext(Dispatchers.IO) {
                    str = QRCodeDecoder.syncDecodeQRCode(bitmap)
                }
                onFinish.invoke(str)
            }
        }
    }

    /**
     * 从图片路径解析出二维吗
     */
    fun parseQrCode(context: Context,imgPath: String, onFinish: (String?) -> Unit) {
        (context as? FragmentActivity)?.let {
            it.lifecycleScope.launch {
                var str: String?
                withContext(Dispatchers.IO) {
                    str = QRCodeDecoder.syncDecodeQRCode(imgPath)
                }
                onFinish.invoke(str)
            }
        }
    }

    /**
     * 生成条形码
     */
    fun genBarCode(
        context: Context,
        content: String,
        width: Int,
        height: Int,
        textSize: Int = 0,
        onFinish: ((Bitmap?) -> Unit)
    ) {
        (context as? FragmentActivity)?.let {
            it.lifecycleScope.launch {
                var bitmap: Bitmap?
                withContext(Dispatchers.IO) {
                    bitmap = QRCodeEncoder.syncEncodeBarcode(content, width, height, textSize)
                }
                onFinish.invoke(bitmap)
            }
        }

    }

    /**
     * 获取扫描结果
     */
    fun fetchResult(data: Intent?): String {
        if (data != null) {
            return data.getStringExtra("result") ?: ""
        }
        return ""
    }
}