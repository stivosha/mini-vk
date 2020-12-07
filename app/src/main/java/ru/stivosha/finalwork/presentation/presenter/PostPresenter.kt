package ru.stivosha.finalwork.presentation.presenter

import ru.stivosha.finalwork.data.entity.Post

interface PostPresenter {
    fun loadPosts(isFavoritePosts: Boolean = false)
    fun likePost(post: Post, position: Int)
    fun dislikePost(post: Post, position: Int)
    fun hidePost(post: Post)
}