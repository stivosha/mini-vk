package ru.stivosha.finalwork.di

import androidx.room.Room
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.stivosha.finalwork.VkClientApplication
import ru.stivosha.finalwork.data.consts.Config.BASE_URL
import ru.stivosha.finalwork.data.interceptors.VkInterceptor
import ru.stivosha.finalwork.data.repositories.RepositoryImpl
import ru.stivosha.finalwork.data.services.VkService


object SimpleDi {

    private var vkService: VkService? = null
    private var oldToken: String? = null
    var dataLoaded = false
    var token: String? = null
    var userId: String? = null
    val repository = RepositoryImpl()

    fun getVkService(): VkService {
        if (vkService == null || token != oldToken) {
            oldToken = token
            val client = OkHttpClient.Builder()
                .addNetworkInterceptor(VkInterceptor(token))
                .build()
            vkService = Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(VkService::class.java)
        }
        return vkService!!
    }
}