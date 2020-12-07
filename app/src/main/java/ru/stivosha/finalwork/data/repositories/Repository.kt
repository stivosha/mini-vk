package ru.stivosha.finalwork.data.repositories

import io.reactivex.Single
import okhttp3.ResponseBody
import ru.stivosha.finalwork.data.entity.Post
import ru.stivosha.finalwork.data.entity.PostDetailObject
import ru.stivosha.finalwork.data.entity.User

interface Repository {
    fun loadPosts(isFavoritePosts: Boolean): Single<List<Post>>
    fun loadPostsFrom(profileId: Int): Single<List<Post>>
    fun likePost(post: Post, position: Int): Single<ResponseBody>
    fun dislikePost(post: Post, position: Int): Single<ResponseBody>
    fun hidePost(post: Post): Single<ResponseBody>
    fun loadProfile(profileId: Int): Single<User>
    fun createPost(profileId: Int, text: String): Single<ResponseBody>
    fun sendComment(post: Post, message: String): Single<ResponseBody>
    fun loadComments(post: Post): Single<List<PostDetailObject>>
}