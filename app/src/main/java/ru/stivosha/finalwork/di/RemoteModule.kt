package ru.stivosha.finalwork.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.stivosha.finalwork.data.consts.Config
import ru.stivosha.finalwork.data.interceptors.VkInterceptor
import ru.stivosha.finalwork.data.services.VkService
import javax.inject.Singleton

@Module
class RemoteModule {

    @Provides
    @Singleton
    fun provideService(okHttpClient: OkHttpClient): VkService =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Config.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VkService::class.java)

    @Provides
    @Singleton
    fun provideOkHttpClient(token: String?): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(VkInterceptor(token))
            .build()
}