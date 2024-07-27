package com.example.mystoryapp.customView

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener

class PasswordEditText(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {

    init {
        setOnTextChangedListener()
    }

    private fun setOnTextChangedListener() {
        addTextChangedListener { text ->
            val errorMessage = if ((text?.length ?: 0) < 8) {
                "Password tidak boleh kurang dari 8 karakter"
            } else {
                null
            }
            setError(errorMessage, null)
        }
    }
}