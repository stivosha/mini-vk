package ru.stivosha.finalwork.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_main.*
import ru.stivosha.finalwork.R
import ru.stivosha.finalwork.data.entity.Post
import ru.stivosha.finalwork.di.SimpleDi
import ru.stivosha.finalwork.ui.activities.activityForResult.NewPostActivity
import ru.stivosha.finalwork.ui.fragments.*


class MainActivity : AppCompatActivity(), FragmentChecker, PostItemClickListener {

    private var postListFragment: Fragment? = null
    private var favoritePostListFragment: Fragment? = null
    private var profileFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = FragmentFactoryImpl()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        if (SimpleDi.token == null)
            VK.login(this, arrayListOf(VKScope.WALL, VKScope.FRIENDS))
    }

    override fun updateFavoritePageState(isLikedItemExist: Boolean) {
        val visible = bottomNavigationPosts.menu.findItem(R.id.bottomNavigationElementFavorite).isVisible
        if (bottomNavigationPosts.selectedItemId != R.id.bottomNavigationElementPosts && visible && !isLikedItemExist) {
            bottomNavigationPosts.selectedItemId = R.id.bottomNavigationElementPosts
        }
        setVisibilityFavoritePostsElement(isLikedItemExist)
    }

    override fun onPostItemClick(pos: Int, postItem: Post, sharedImageView: ImageView?) {
        if (sharedImageView != null) {
            val intent = PostDetailActivity.createIntent(this, postItem)
            startActivity(intent, ViewCompat.getTransitionName(sharedImageView)?.let {
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedImageView, it).toBundle()
            })
        } else {
            val intent = PostDetailActivity.createIntent(this, postItem)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                Log.d("USER_ID", token.userId.toString())
                Log.d("VK_SDK", token.accessToken)
                SimpleDi.token = token.accessToken
                SimpleDi.userId = token.userId.toString()
                openFragmentByName(PostListFragment::class.java.name)
            }

            override fun onLoginFailed(errorCode: Int) {
                Log.d("VK_SDK", errorCode.toString())
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun init() {
        bottomNavigationPosts.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottomNavigationElementPosts -> openFragmentByName(PostListFragment::class.java.name)
                R.id.bottomNavigationElementFavorite -> openFragmentByName(FavoritePostsListFragment::class.java.name)
                R.id.bottomNavigationElementProfile -> openFragmentByName(ProfileFragment::class.java.name)
            }
            true
        }
        setVisibilityFavoritePostsElement(false)
    }

    private fun setVisibilityFavoritePostsElement(visibility: Boolean) {
        bottomNavigationPosts.menu.findItem(R.id.bottomNavigationElementFavorite).isVisible = visibility
    }

    private fun openFragmentByName(name: String) {
        when (name) {
            PostListFragment::class.java.name -> {
                if (postListFragment == null)
                    postListFragment = supportFragmentManager.fragmentFactory.instantiate(classLoader, PostListFragment::class.java.name)
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainerView, postListFragment!!).commitAllowingStateLoss()
                }
            }
            FavoritePostsListFragment::class.java.name -> {
                if (favoritePostListFragment == null)
                    favoritePostListFragment = supportFragmentManager.fragmentFactory.instantiate(classLoader, FavoritePostsListFragment::class.java.name)
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainerView, favoritePostListFragment!!).commitAllowingStateLoss()
                }
            }
            ProfileFragment::class.java.name -> {
                if (profileFragment == null)
                    profileFragment = supportFragmentManager.fragmentFactory.instantiate(classLoader, ProfileFragment::class.java.name)
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainerView, profileFragment!!).commitAllowingStateLoss()
                }
            }
        }
    }
}
