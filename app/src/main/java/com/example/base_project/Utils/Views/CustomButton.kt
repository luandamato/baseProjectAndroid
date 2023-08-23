package com.example.base_project.Utils.Views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.base_project.R
import com.example.base_project.databinding.CustomButtonBinding

class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private var binding = CustomButtonBinding.inflate(LayoutInflater.from(context), this, true)

    init{
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.customButton, 0, 0)

            binding.buttonText.text = typedArray.getString(R.styleable.customButton_btnText)

            val enabled = typedArray.getBoolean(R.styleable.customButton_btnEnabled, true)
            isEnabled = enabled


            val preenchido = typedArray.getBoolean(R.styleable.customButton_preenchido, true)
            if (!preenchido) setOnlyBorder()

        }

    }
    private fun setOnlyBorder(){
        binding.buttonBackground.setBackgroundResource(R.drawable.bg_custom_button_border)
        binding.buttonProgress.indeterminateDrawable.setTint(resources.getColor(R.color.color_primary))
        binding.buttonText.setTextColor(resources.getColor(R.color.color_primary))
    }
    fun setLoading(loading: Boolean) {
        this.isClickable = !loading
        binding.buttonProgress.setVisible(loading)
        binding.buttonText.setVisible(!loading)
    }

    fun setButtonText(btnText: String) {
        binding.buttonText.text = btnText
    }
}