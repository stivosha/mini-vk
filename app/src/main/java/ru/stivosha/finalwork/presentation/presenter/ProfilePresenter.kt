package ru.stivosha.finalwork.presentation.presenter

interface ProfilePresenter {
    fun loadProfileAndComments(profileId: Int)
    fun loadPosts(profileId: Int)
    fun createPost(profileId: Int, text: String)
}