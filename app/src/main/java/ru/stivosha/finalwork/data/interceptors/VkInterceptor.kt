package ru.stivosha.finalwork.data.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.stivosha.finalwork.di.SimpleDi
import java.lang.NullPointerException

class VkInterceptor(private val token: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (token == null)
            throw NullPointerException("Token can't be null")
        val httpUrl = chain.request()
            .url().newBuilder()
            .addQueryParameter("v", "5.124")
            .addQueryParameter("access_token", token)
            .build()
        return chain.proceed(
            chain.request()
                .newBuilder()
                .url(httpUrl)
                .build()
        )
    }
}