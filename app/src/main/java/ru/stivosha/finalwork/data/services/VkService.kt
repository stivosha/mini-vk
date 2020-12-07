package ru.stivosha.finalwork.data.services

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import ru.stivosha.finalwork.data.responseentity.posts.VkResponse
import ru.stivosha.finalwork.data.responseentity.comments.VkCommentsResponse
import ru.stivosha.finalwork.data.responseentity.users.FieldForParsing
import ru.stivosha.finalwork.data.responseentity.users.VkUserResponse

interface VkService {
    @GET("newsfeed.get")
    fun getPosts(): Single<VkResponse>

    @GET("wall.get?extended=1")
    fun getPostsFrom(@Query("owner_id") ownerId: Int): Single<VkResponse>

    @GET("wall.createComment")
    fun createComment(@Query("post_id") postId: Int, @Query("owner_id") ownerId: Int, @Query("message") message: String): Single<ResponseBody>

    @GET("wall.post")
    fun createPost(@Query("owner_id") ownerId: Int, @Query("message") message: String): Single<ResponseBody>

    @GET("newsfeed.ignoreItem?type=post")
    fun hidePost(@Query("item_id") postId: Int, @Query("owner_id") ownerId: Int): Single<ResponseBody>

    @GET("likes.add?type=post")
    fun likePost(@Query("item_id") postId: Int, @Query("owner_id") ownerId: Int): Single<ResponseBody>

    @GET("likes.delete?type=post")
    fun dislikePost(@Query("item_id") postId: Int, @Query("owner_id") ownerId: Int): Single<ResponseBody>

    @GET("wall.getComments?type=post&need_likes=0&extended=1")
    fun getComments(@Query("post_id") postId: Int, @Query("owner_id") ownerId: Int): Single<VkCommentsResponse>

    @GET("users.get")
    fun getUser(
        @Query("user_ids") userId: Int,
        @Query("fields") strField: String = FieldForParsing.fields.joinToString(separator = ",")
    ): Single<VkUserResponse>
}