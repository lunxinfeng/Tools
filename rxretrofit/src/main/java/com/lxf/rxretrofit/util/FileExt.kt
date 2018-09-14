package com.lxf.rxretrofit.util

import com.lxf.rxretrofit.callback.ProgressListener
import okhttp3.ResponseBody
import okio.BufferedSource
import okio.Okio
import java.io.File
import java.io.IOException


/**
 * 写入文件
 */
@Throws(IOException::class)
fun writeFile(response: ResponseBody, file: File) {
    writeFile(response.source(),file)
}

@Throws(IOException::class)
fun writeFile(source: BufferedSource, file: File) {
    if (!file.parentFile.exists())
        file.parentFile.mkdirs()

    if (file.exists())
        file.delete()

    val bufferedSink = Okio.buffer(Okio.sink(file))

    bufferedSink.writeAll(source)

    bufferedSink.close()
    source.close()
}