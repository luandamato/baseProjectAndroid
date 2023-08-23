package com.example.base_project.Utils.Views

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.example.base_project.R
import com.example.base_project.databinding.CustomTitleViewBinding


class CustomTitleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private var binding = CustomTitleViewBinding.inflate(LayoutInflater.from(context), this, true)

    init{
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.customTitleView, 0, 0)

            binding.txtTitle.text = typedArray.getString(R.styleable.customTitleView_titleText)
            binding.txtSubTitle.text = typedArray.getString(R.styleable.customTitleView_subtitleText)

            val centerd = typedArray.getBoolean(R.styleable.customTitleView_center, false)
            if (centerd) center()

            val secundary = typedArray.getBoolean(R.styleable.customTitleView_secondaryTheme, false)
            if (secundary) setSecundary()
        }

    }

    private fun center(){
        binding.txtTitle.setGravity(Gravity.CENTER)
        binding.txtSubTitle.setGravity(Gravity.CENTER)
    }

    private fun setSecundary(){
        binding.txtTitle.setTextColor(context.getColor(R.color.black))
        binding.txtSubTitle.setTextColor(context.getColor(R.color.purple_200))
    }

    public fun setTitle(text: String){
        binding.txtTitle.text = text
    }

    public fun setSubtitle(text: String){
        binding.txtSubTitle.text = text
    }

}