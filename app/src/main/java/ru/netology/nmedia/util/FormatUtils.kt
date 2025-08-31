package ru.netology.nmedia.util

import android.content.Context
import android.view.View
import android.view.ViewTreeObserver.OnWindowFocusChangeListener
import android.view.inputmethod.InputMethodManager

object AndroidUtils {
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // thanks to https://stackoverflow.com/a/68925063/1219012
    fun showKeyboard(view: View) {
        view.requestFocus()
        if (view.hasWindowFocus()) {
            showKeyboardNow(view)
        } else {
            view.viewTreeObserver.addOnWindowFocusChangeListener(object : OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    if (hasFocus) {
                        showKeyboardNow(view)
                        view.viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
        }
    }

    private fun showKeyboardNow(view: View) {
        if (!view.isFocused) return

        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun formatCount(count: Int): String {
    return when {
        count < 1_000 -> {
            count.toString()
        }

        count < 10_000 -> {
            val thousands = count / 1000f
            val truncated = (thousands * 10).toInt() / 10.0
            "${"%.1f".format(truncated)}K"
        }

        count < 1_000_000 -> {
            "${count / 1000}K"
        }

        else -> {
            val millions = count / 1_000_000f
            "${"%.1f".format(millions)}M"
        }
    }
}
