package ru.stivosha.finalwork.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.stivosha.finalwork.R
import ru.stivosha.finalwork.data.consts.Extras.ERROR_TEXT

class ErrorDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val text = requireArguments().getString(ERROR_TEXT)
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.apply {
            setTitle(R.string.error)
            setMessage(text)
            setNeutralButton(R.string.ok) { dialog, _ ->
                dialog?.dismiss()
            }
        }
        return alertDialogBuilder.create()
    }

    companion object {
        fun createInstance(errorText: String): ErrorDialogFragment {
            val frag = ErrorDialogFragment()
            frag.arguments = Bundle().apply { putString(ERROR_TEXT, errorText) }
            return frag
        }
    }
}