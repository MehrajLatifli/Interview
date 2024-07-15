package com.example.interview.utilities

import okhttp3.OkHttpClient
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate

fun createUnsafeOkHttpClient(): OkHttpClient {
    // Create a trust manager that trusts all certificates
    val trustAllCertificates = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate>? = emptyArray()
    })

    // Initialize SSLContext with the above trust manager
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, trustAllCertificates, java.security.SecureRandom())

    // Obtain the SSLSocketFactory from the SSLContext
    val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

    return OkHttpClient.Builder()
        .sslSocketFactory(sslSocketFactory, trustAllCertificates[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true } // Bypass hostname verification
        .build()
}
