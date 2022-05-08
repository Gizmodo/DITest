package ru.gizmodo.ditest

import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        TestModule::class
    ]
)
interface AppGraph {
    //    fun embed(vm: MainViewModel)
    fun embed(fragment: FirstFragment)
    fun embed(vm: FirstViewModel)
}