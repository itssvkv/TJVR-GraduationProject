package com.itssvkv.todolist.utils

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import com.itssvkv.todolist.R

object CommonFunctions {
    fun buttonState(state: Boolean, view: View, context: Context) {
        view.isEnabled = state
        if (state) {
            view.background =
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.cv_button_shadow,
                    context.resources.newTheme()
                )
        } else {
            view.background =
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.disable_button,
                    context.resources.newTheme()
                )
        }
    }


    fun progressBarState(state: Boolean, progressBar: ProgressBar) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    // remove the et
    fun checkTheFormat(
        editText1: EditText? = null,
        editText2: EditText? = null,
        editText3: EditText? = null,
        editText4: EditText? = null,
        button: Button,
        context: Context
    ) {
        if (editText1?.text.toString().trim()
                .isEmpty() || editText2?.text.toString().trim()
                .isEmpty() || editText3?.text.toString().trim()
                .isEmpty() || editText4?.text.toString().trim().isEmpty()
        ) {
            buttonState(state = false, button, context)
        } else {
            buttonState(state = true, button, context)
        }
    }
}