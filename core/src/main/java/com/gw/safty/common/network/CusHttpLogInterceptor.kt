package com.gw.safty.common.network

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.http.HttpHeaders
import okhttp3.internal.platform.Platform
import okio.Buffer
import timber.log.Timber
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * [application interceptor][OkHttpClient.interceptors] or as a [ ][OkHttpClient.networkInterceptors].
 *
 * The format of the logs created by
 * this class should not be considered stable and may change slightly between releases. If you need
 * a stable logging format, use your own interceptor.
 */
class CusHttpLogInterceptor @JvmOverloads constructor(
    private val logger: Logger = Logger.DEFAULT
) : Interceptor {

    @Volatile
    var level = Level.NONE
        private set

    /** Change the level at which this interceptor logs.  */
    fun setLevel(level: Level?): CusHttpLogInterceptor {
        if (level == null) throw NullPointerException("level == null. Use Level.NONE instead.")
        this.level = level
        return this
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level = level
        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }
        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS
        val requestBody = request.body()
        val hasRequestBody = requestBody != null
        val connection = chain.connection()
        var requestStartMessage = ("--> "
                + request.method()
                + ' ' + request.url()
                + if (connection != null) " " + connection.protocol() else "")
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody!!.contentLength() + "-byte body)"
        }
        logger.log(requestStartMessage)
        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody!!.contentType() != null) {
                    logger.log("Content-Type: " + requestBody.contentType())
                }
                if (requestBody.contentLength() != -1L) {
                    logger.log("Content-Length: " + requestBody.contentLength())
                }
            }
            val headers = request.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                val name = headers.name(i)
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equals(
                        name,
                        ignoreCase = true
                    ) && !"Content-Length".equals(name, ignoreCase = true)
                ) {
                    logger.log(name + ": " + headers.value(i))
                }
                i++
            }
            if (!logBody || !hasRequestBody) {
                logger.log("--> END " + request.method())
            } else if (bodyEncoded(request.headers())) {
                logger.log("--> END " + request.method() + " (encoded body omitted)")
            } else {
                val buffer = Buffer()
                requestBody?.writeTo(buffer)
                var charset: Charset = UTF8
                val contentType = requestBody?.contentType()
                contentType?.charset(UTF8)?.let { charset = it }
                logger.log("")
                if (isPlaintext(buffer)) {
                    if (contentType?.toString()?.contains("multipart/form-data") == false)
                        logger.log(buffer.readString(charset))
                    logger.log("--> END " + request.method() + " (" + requestBody?.contentLength() + "-byte body)")
                } else {
                    logger.log("--> END " + request.method() + " (binary " + requestBody?.contentLength() + "-byte body omitted)")
                }
            }
        }
        val startNs = System.nanoTime()
        val response: Response
        response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            logger.log("<-- HTTP FAILED: $e")
            throw e
        }
        val tookMs: Long = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        val responseBody = response.body()
        val contentLength = responseBody?.contentLength() ?: -1L
        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
        logger.log(
            "<-- "
                    + response.code()
                    + (if (response.message()
                    .isEmpty()
            ) "" else ' '.toString() + response.message())
                    + ' ' + response.request().url()
                    + " (" + tookMs + "ms" + (if (!logHeaders) ", $bodySize body" else "") + ')'
        )
        if (logHeaders) {
            val headers = response.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                logger.log(headers.name(i) + ": " + headers.value(i))
                i++
            }
            if (!logBody || !HttpHeaders.hasBody(response)) {
                logger.log("<-- END HTTP")
            } else if (bodyEncoded(response.headers())) {
                logger.log("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody?.source()
                source?.request(Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source?.buffer()
                var charset: Charset = UTF8
                val contentType = responseBody?.contentType()
                contentType?.charset(UTF8)?.let { charset = it }
                if (buffer != null && !isPlaintext(buffer)) {
                    logger.log("")
                    logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)")
                    return response
                }
                if (buffer != null && contentLength != 0L) {
                    logger.log("-----自定义Interceptor ${this.javaClass.simpleName}------")
                    val p = Pattern.compile("\\s*\t|\r|\n")
                    val message = buffer.clone().readString(charset)
                    val trim = p.matcher(message).replaceAll("")
                    logger.log(trim)
                }
                logger.log("<-- END HTTP (" + buffer?.size() + "-byte body)")
            }
        }
        return response
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"]
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }

    enum class Level {
        /** No logs.  */
        NONE,

        /**
         * Logs request and response lines.
         * Example:
         * <pre>`--> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
        `</pre> *
         */
        BASIC,

        /**
         * Logs request and response lines and their respective headers.
         *
         * Example:
         * <pre>`--> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
        `</pre> *
         */
        HEADERS,

        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         *
         *
         * Example:
         * <pre>`--> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
        `</pre> *
         */
        BODY
    }

    interface Logger {
        fun log(message: String?)
        companion object {
            private const val LOG_MAXLENGTH = 2000

            /** A [Logger] defaults output appropriate for the current platform.  */
            val DEFAULT = object : Logger {
                override fun log(message: String?) {
                    debugLarge(message)
                }
            }
            fun debugLarge(message: String?){
                if (message != null){
                    if (message.length > LOG_MAXLENGTH){
                        var part = message.substring(0, LOG_MAXLENGTH)
                        Timber.tag("CusHttpLogInterceptor").d(part)
                        part = message.substring(LOG_MAXLENGTH, message.length)
                        debugLarge(part)
                    } else{
                        Timber.tag("CusHttpLogInterceptor").d(message)
                    }
                }
            }
        }
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")


        /**
         * Returns true if the body in question probably contains human readable text. Uses a small sample
         * of code points to detect unicode control characters commonly used in binary file signatures.
         */
        fun isPlaintext(buffer: Buffer): Boolean {
            return try {
                val prefix = Buffer()
                val byteCount = if (buffer.size() < 64) buffer.size() else 64
                buffer.copyTo(prefix, 0, byteCount)
                for (i in 0..15) {
                    if (prefix.exhausted()) {
                        break
                    }
                    val codePoint = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                }
                true
            } catch (e: EOFException) {
                false // Truncated UTF-8 sequence.
            }
        }
    }

}