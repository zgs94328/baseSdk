package com.zgs.library.base.ext

import android.net.Uri
import java.net.URLDecoder


/**
 * 解析url的查询参数
 */
fun String.parseQueryParams(): Map<String, String> {
    var index = lastIndexOf("?") + 1
    var queryParam = hashMapOf<String, String>()
    if (index > 0) {
        this.split("&").forEach {
            if (it.contains("=")) {
                val arr = it.split("=", limit = 2)
                if (arr.size > 1) {
                    val key = arr[0]
                    val value = arr[1]
                    queryParam[decode(key)] = decode(value)
                }
            }
        }
    }

    return queryParam
}

/**
 * 解析url 中path部分
 */
fun String.parseQueryPath(): String? {
    val array = this.split('?', limit = 2)
    return array.getOrNull(0)
}

/**
 * URLDecode解码
 */
private fun decode(content: String): String {
    return try {
        URLDecoder.decode(content, "utf-8")
    } catch (e: Exception) {
        content
    }
}
