package com.sokhorn.bgapplication.request

import android.os.Build
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.sokhorn.bgapplication.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

object ApiClient {
    private var retrofit: Retrofit? = null

    //    private const val TIME_OUT: Long = 120
    private const val TIME_OUT: Long = 30

    //    private const val TIME_OUT_CONNECT: Long = 60
    private const val TIME_OUT_CONNECT: Long = 30

    private var okHttpClient: OkHttpClient? = null

    //    private const val baseUrl = "http://phartepnimet.nexvissolution.com"
    private const val baseUrl = "http://dconetservice.nexvissolution.com"

    /**
     * @param baseUrl url
     */
    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = createRetrofit()
        } else {
            if (!baseUrl.equals(retrofit!!.baseUrl().host, ignoreCase = true)) {
                retrofit = createRetrofit()
            }
        }
        return retrofit!!
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient!!)
            .build()
    }

    //Initial when app open
    @JvmStatic
    fun initOkHttp(deviceId: String ) {
        val httpClient = OkHttpClient().newBuilder()
            .connectTimeout(TIME_OUT_CONNECT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)

        val context = SSLContext.getInstance("TLS")
        context.init(
            null,
            arrayOf<X509TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            }),
            SecureRandom()
        )
        val trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            ("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers))
        }
        val trustManager = trustManagers[0] as X509TrustManager

        val sslSocketFactory = context.socketFactory
        httpClient.sslSocketFactory(sslSocketFactory, trustManager)
        httpClient.hostnameVerifier(HostnameVerifier { hostname, session -> true })

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            httpClient.addInterceptor(interceptor)
        }


        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val connection = chain.connection()
            connection?.socket()?.keepAlive = true
            val requestBuilder = original.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Request-Type", "Android")
                .addHeader("Content-Type", "application/json")
                .addHeader("connection", "keep-alive")
                .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                .addHeader("x-app-version-number", BuildConfig.VERSION_CODE.toString())
                .addHeader("x-os-version", "${Build.VERSION.SDK_INT}")
                .addHeader("x-timezone", TimeZone.getDefault().id)
                .addHeader("x-platform", "ANDROID")
                .addHeader("x-udid", deviceId)

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        okHttpClient = httpClient.build()
    }

}