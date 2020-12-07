package ru.stivosha.finalwork.data.repositories

import android.util.Log
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import ru.stivosha.finalwork.VkClientApplication
import ru.stivosha.finalwork.data.db.PostDataBase
import ru.stivosha.finalwork.data.db.dbentity.PostDbEntity
import ru.stivosha.finalwork.data.entity.Comment
import ru.stivosha.finalwork.data.entity.Post
import ru.stivosha.finalwork.data.entity.PostDetailObject
import ru.stivosha.finalwork.data.entity.User
import ru.stivosha.finalwork.data.responseentity.comments.CommentMapper
import ru.stivosha.finalwork.data.responseentity.posts.PostMapper
import ru.stivosha.finalwork.data.services.VkService
import ru.stivosha.finalwork.di.SimpleDi
import java.util.*
import java.util.concurrent.TimeUnit

class RepositoryImpl : Repository {
    private val service: VkService by lazy { SimpleDi.getVkService() }
    private val dataBase: PostDataBase by lazy { VkClientApplication.db }
    private var postsLoaded: Boolean = false
    private val timeOutForRequestInSeconds = 2L

    override fun loadPosts(isFavoritePosts: Boolean): Single<List<Post>> {
        val postSingle = if (postsLoaded) loadPostsFromLocal() else loadPostsFromRemote()
        return postSingle
            .map {
                if (!postsLoaded) {
                    savePostsInLocal(it)
                    postsLoaded = true
                }
                val filteredPosts = if (isFavoritePosts) it.filter { post -> post.isLiked } else it
                filteredPosts.sortedBy { post -> post.dateCreation }
            }
            .subscribeOn(Schedulers.computation())
    }

    override fun sendComment(post: Post, message: String): Single<ResponseBody> {
        return SimpleDi.getVkService().createComment(postId = post.id, ownerId = post.authorId, message = message)
            .subscribeOn(Schedulers.io())
    }

    override fun loadComments(post: Post): Single<List<PostDetailObject>> {
        return service.getComments(postId = post.id, ownerId = post.authorId)
            .timeout(timeOutForRequestInSeconds, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { response ->
                if (response.commentBody != null) {
                    val groups = response.commentBody.groups.map { group -> group.groupId to Pair(group.name, group.photoUrl) }.toMap()
                    val profiles = response.commentBody.profiles.map { profile ->
                        profile.profileId to Pair("${profile.firstName} ${profile.lastName}", profile.photoUrl)
                    }.toMap()
                    response.commentBody.comments.map { commentEntity ->
                        CommentMapper(commentEntity, groups, profiles).comment as PostDetailObject
                    }
                } else {
                    listOf<PostDetailObject>()
                }
            }
    }

    override fun loadPostsFrom(profileId: Int): Single<List<Post>> {
        return service.getPostsFrom(ownerId = profileId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { it.response }
            .map { response ->
                val groups = response.groups.map { group -> group.groupId to Pair(group.name, group.photoUrl) }.toMap()
                val profiles = response.profiles.map { profile ->
                    profile.profileId to Pair("${profile.firstName} ${profile.lastName}", profile.photoUrl)
                }.toMap()
                response.posts
                    .filter { it.likeEntity != null && it.commentCountEntity != null && it.repostEntity != null }
                    .map { post ->
                        PostMapper(post, groups, profiles).post
                    }.toList()
            }
    }

    override fun likePost(post: Post, position: Int): Single<ResponseBody> {
        return service
            .likePost(postId = post.id, ownerId = post.authorId)
            .timeout(timeOutForRequestInSeconds, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                updatePostInLocal(post)
            }
    }

    override fun dislikePost(post: Post, position: Int): Single<ResponseBody> {
        return service
            .dislikePost(postId = post.id, ownerId = post.authorId)
            .timeout(timeOutForRequestInSeconds, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                updatePostInLocal(post)
            }
    }

    override fun hidePost(post: Post): Single<ResponseBody> {
        deletePostFromLocal(post)
        return service
            .hidePost(postId = post.id, ownerId = post.authorId)
            .timeout(timeOutForRequestInSeconds, TimeUnit.SECONDS)
            .doOnError {
                dataBase.postDao().insertAll(PostDbEntity.create(post))
            }.subscribeOn(Schedulers.io())
    }

    override fun loadProfile(profileId: Int): Single<User> {
        return service.getUser(userId = profileId)
            .timeout(timeOutForRequestInSeconds, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .map { it.users[0] }
            .map { userEntity ->
                User(
                    id = userEntity.profileId,
                    first_last_name = "${userEntity.firstName} ${userEntity.lastName}",
                    photo_url = userEntity.photoUrl,
                    followersCount = userEntity.followersCount,
                    status = userEntity.status,
                    educationPlaceName = userEntity.universityName,
                    cityName = userEntity.city?.title
                )
            }
    }

    override fun createPost(profileId: Int, text: String): Single<ResponseBody> {
        return service.createPost(ownerId = profileId, message = text)
            .timeout(timeOutForRequestInSeconds, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
    }

    private fun savePostsInLocal(posts: List<Post>) {
        val postDbEntities = posts.map { PostDbEntity.create(it) }
        dataBase.clearAllTables()
        dataBase.postDao().insertAll(*postDbEntities.toTypedArray())
    }

    private fun updatePostInLocal(post: Post): Disposable {
        return dataBase.postDao().update(PostDbEntity.create(post))
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = { Log.d(this::class.java.simpleName, "post ${post.id} updated") },
                onError = { Log.d(this::class.java.simpleName, "post ${post.id} didn't update") }
            )
    }

    private fun deletePostFromLocal(post: Post): Disposable {
        return dataBase.postDao().delete(PostDbEntity.create(post))
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = { Log.d(this::class.java.simpleName, "post ${post.id} deleted") },
                onError = { Log.d(this::class.java.simpleName, "post ${post.id} didn't delete") }
            )
    }

    private fun loadPostsFromLocal(): Single<List<Post>> {
        return dataBase.postDao().getAll()
            .subscribeOn(Schedulers.io())
            .map { postDbEntities ->
                postDbEntities.map { postDbEntity ->
                    Post(
                        id = postDbEntity.id,
                        author = postDbEntity.authorName,
                        textContent = postDbEntity.textContent,
                        dateCreation = Date(postDbEntity.dateCreation),
                        authorImageUrl = postDbEntity.authorImageUrl,
                        imageContentUrl = postDbEntity.imageContentUrl,
                        isLiked = postDbEntity.isLiked,
                        likesCount = postDbEntity.likesCount,
                        commentsCount = postDbEntity.commentsCount,
                        repostsCount = postDbEntity.repostsCount,
                        authorId = postDbEntity.authorId
                    )
                }
            }
    }

    private fun loadPostsFromRemote(): Single<List<Post>> {
        return service.getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { it.response }
            .map { response ->
                val groups = response.groups.map { group -> group.groupId to Pair(group.name, group.photoUrl) }.toMap()
                val profiles = response.profiles.map { profile ->
                    profile.profileId to Pair("${profile.firstName} ${profile.lastName}", profile.photoUrl)
                }.toMap()
                response.posts
                    .filter { it.likeEntity != null && it.commentCountEntity != null && it.repostEntity != null }
                    .map { post ->
                        PostMapper(post, groups, profiles).post
                    }.toList()
            }
    }
}