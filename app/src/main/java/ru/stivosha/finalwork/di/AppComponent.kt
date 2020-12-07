package ru.stivosha.finalwork.di

import androidx.fragment.app.Fragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, DbModule::class, RemoteModule::class, TestModule::class])
@Singleton
interface AppComponent {
    fun inject(fragment: Fragment)
}