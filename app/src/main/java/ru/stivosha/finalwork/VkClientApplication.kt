package ru.stivosha.finalwork

import android.app.Application
import android.content.Context
import androidx.room.Room
import ru.stivosha.finalwork.data.db.PostDataBase
import ru.stivosha.finalwork.di.AppComponent
import ru.stivosha.finalwork.di.AppModule
import ru.stivosha.finalwork.di.DaggerAppComponent
import ru.stivosha.finalwork.di.TestModule

class VkClientApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //component = DaggerAppComponent.builder()
        //    .appModule(AppModule(this)).build()
        db = createDataBase(this)
    }

    companion object {
        //lateinit var component: AppComponent
        lateinit var db: PostDataBase
        const val READER_NAME = "PostReader"
    }

    private fun createDataBase(context: Context): PostDataBase = Room.databaseBuilder(
        context,
        PostDataBase::class.java, READER_NAME
    ).build()
}