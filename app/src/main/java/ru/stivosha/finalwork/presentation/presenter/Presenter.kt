package ru.stivosha.finalwork.presentation.presenter

interface Presenter<View> {

    fun attachView(view: View)

    fun detachView(isFinishing: Boolean)
}