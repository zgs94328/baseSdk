package com.zgs.library.base.ext

import android.content.Intent
import android.net.Uri
import java.io.File
import java.io.IOException

/**
 * 作者　: zgs
 * 时间　: 2021/3/9
 * 描述　:
 */
/**
 * 判断文件夹是否存在
 *
 * @param filePath
 */
fun isDirExists(filePath: String): Boolean {
    val file = getFileByPath(filePath)
    return file?.isDirExists ?: false
}
fun isFileExists(file: File): Boolean {
    return file?.isDirExists == true
}
/**
 * 判断文件夹是否存在
 *
 */
val File.isDirExists: Boolean get() = exists() && isDirectory

/**
 * 通过文件路径获取File对象
 *
 * @param filePath
 * @return nullable
 */
fun getFileByPath(filePath: String): File? = if (filePath.isBlank()) null else File(filePath)


/**
 * 判断文件是否存在，不存在则判断是否创建成功
 *
 * @param filePath
 * @return true 文件存在或者创建成功  false 路径无效、文件不存在或者创建失败
 */
fun createOrExistsFile(filePath: String): Boolean {
    val file = getFileByPath(filePath)
    return file?.createOrExistsFile() ?: false
}
/**
 * 判断文件是否存在，不存在则判断是否创建成功
 *
 * @return true 文件存在或者创建成功  false 文件不存在或者创建失败
 */
fun File.createOrExistsFile(): Boolean {
    if (exists()) return isFile
    if (parentFile?.createOrExistsDir() != true) return false

    return try {
        createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}
/**
 * 判断目录是否存在，不存在则判断是否创建成功
 *
 * @return true 文件夹存在或者创建成功  false 文件夹不存在或者创建失败
 */
fun File.createOrExistsDir(): Boolean =
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        if (exists()) isDirectory else mkdirs()

