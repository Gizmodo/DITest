package ru.gizmodo.ditest

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
    ]
)
interface ViewModelInjector {
    fun inject(viewmodel: MainActivity)
}