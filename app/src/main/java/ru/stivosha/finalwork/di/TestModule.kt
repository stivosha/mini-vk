package ru.stivosha.finalwork.di

import dagger.Module
import dagger.Provides
import ru.stivosha.finalwork.data.entity.TestObject
import javax.inject.Singleton

@Module
class TestModule {
    @Provides
    @Singleton
    fun provideTestObject() = TestObject()
}