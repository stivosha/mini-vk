package ru.stivosha.finalwork.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.stivosha.finalwork.data.db.PostDataBase
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    fun providePostDataBase(context: Context): PostDataBase {
        return Room.databaseBuilder(
            context,
            PostDataBase::class.java, READER_NAME
        ).build()
    }

    companion object {
        const val READER_NAME = "PostReader"
    }
}