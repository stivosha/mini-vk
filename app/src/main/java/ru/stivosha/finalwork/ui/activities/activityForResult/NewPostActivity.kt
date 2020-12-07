package ru.stivosha.finalwork.ui.activities.activityForResult

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_new_post.*
import ru.stivosha.finalwork.R
import ru.stivosha.finalwork.data.entity.Post
import ru.stivosha.finalwork.ui.activities.PostDetailActivity
import ru.stivosha.finalwork.ui.fragments.ProfileFragment

class NewPostActivity : AppCompatActivity() {

    private var buttonSendActive: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
        initNewPostItemsBlock()
    }

    private fun initNewPostItemsBlock() {
        newPostItemsAttach.setOnClickListener { showToast(getString(R.string.attachIconClicked)) }
        newPostItemsSmile.setOnClickListener { showToast(getString(R.string.smileIconClicked)) }
        newPostEditText.addTextChangedListener {
            if (buttonSendActive && (it.isNullOrEmpty())) {
                newPostItemsSend.setImageResource(R.drawable.ic_send_36)
                buttonSendActive = false
            } else if (!buttonSendActive && !(it.isNullOrEmpty())) {
                newPostItemsSend.setImageResource(R.drawable.ic_send_active_36)
                buttonSendActive = true
            }
        }
        newPostItemsSend.setOnClickListener {
            if (!buttonSendActive) {
                showToast(getString(R.string.textFieldCantBeEmpty))
                return@setOnClickListener
            }
            setResult(ProfileFragment.NEW_POST_CREATED_OK, Intent().putExtra(ProfileFragment.POST_TEXT, newPostEditText.text.toString()))
            finish()
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, NewPostActivity::class.java)
    }
}