package ru.stivosha.finalwork.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class FragmentFactoryImpl : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            PostListFragment::class.java.name -> PostListFragment.createInstance()
            FavoritePostsListFragment::class.java.name -> FavoritePostsListFragment.createInstance()
            ProfileFragment::class.java.name -> ProfileFragment.createInstance()
            else -> super.instantiate(classLoader, className)
        }
    }
}