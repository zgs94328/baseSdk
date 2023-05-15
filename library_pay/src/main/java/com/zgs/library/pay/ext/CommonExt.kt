package com.zgs.library.pay.ext

import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import com.google.gson.reflect.TypeToken

/**
 * 文件名：CommonExt <br/>
 * 描述：
 *
 * @author zgs
 * @since 2023/05/12 14:19
 */
inline fun <reified T> String.toBean(dateFormat: String = "yyyy-MM-dd HH:mm:ss", lenient: Boolean = false)
        = GsonBuilder().setDateFormat(dateFormat)
    .setObjectToNumberStrategy(ToNumberPolicy.BIG_DECIMAL)
    .apply {
        if(lenient) setLenient()
    }.create()
    .fromJson<T>(this, object : TypeToken<T>() {}.type)