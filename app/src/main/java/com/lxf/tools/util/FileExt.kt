package com.lxf.tools.util

import android.os.Environment
import okio.BufferedSource
import okio.Okio
import java.io.File
import java.io.IOException


/**
 * 写入文件
 */
@Throws(IOException::class)
fun writeFile(source: BufferedSource, file: File, append:Boolean = false) {
    if (!file.parentFile.exists())
        file.parentFile.mkdirs()

    if (file.exists())
        file.delete()
    val sink = if (append) Okio.appendingSink(file) else Okio.sink(file)
    val bufferedSink = Okio.buffer(sink)
    bufferedSink.writeAll(source)

    bufferedSink.close()
    source.close()
}

fun getSDPath() = Environment.getExternalStorageDirectory().absolutePath