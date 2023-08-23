package com.example.base_project.Utils.Views

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.base_project.Utils.Extensions.cleaned
import com.example.base_project.Utils.Mask
import java.util.concurrent.Executors

fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

//fun ImageView.load(image: Any?, isCircular: Boolean = false, centerCrop: Boolean = false) {
//    var requestOptions = RequestOptions()
//        .error(R.drawable.ic_placeholder_error)
//        .placeholder(R.drawable.ic_placeholder_loading)
//
//    if (centerCrop)
//        requestOptions = requestOptions.centerCrop()
//
//    if (isCircular)
//        requestOptions = requestOptions.circleCrop()
//
//    Glide.with(this).load(image).apply(requestOptions).into(this)
//}

fun ImageView.loadFromurl(url: String){

    var image: Bitmap? = null
    val executor = Executors.newSingleThreadExecutor()
    val handler = Handler(Looper.getMainLooper())

    executor.execute {

        val imageURL = url

        try {
            val `in` = java.net.URL(imageURL).openStream()
            image = BitmapFactory.decodeStream(`in`)

            handler.post {
                this.setImageBitmap(image)
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun RecyclerView.setup(adapter: RecyclerView.Adapter<in RecyclerView.ViewHolder>,
                       layoutManager: RecyclerView.LayoutManager? = LinearLayoutManager(this.context),
                       decoration: RecyclerView.ItemDecoration? = null,
                       hasFixedSize: Boolean = true) {

    this.adapter = adapter
    this.layoutManager = layoutManager
    this.setHasFixedSize(hasFixedSize)
    decoration?.let { this.addItemDecoration(it) }
}

fun EditText.insertMask(mask: String) {
    addTextChangedListener(object : TextWatcher {
        var isUpdating: Boolean = false
        var old = ""
        var maskAux = mask

        override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                   count: Int) {
            if (mask == Mask.PHONE) {
                if (s.toString().cleaned().length > 10) {
                    maskAux = Mask.CEL_PHONE
                } else {
                    maskAux = Mask.PHONE
                }
            }
            val str = s.toString().cleaned()
            var mascara = ""
            if (isUpdating) {
                old = str
                isUpdating = false
                return
            }
            var i = 0
            for (char in maskAux.toCharArray()) {
                try {
                    val cha = str[i]
                    if (char == '#') {
                        mascara += cha
                        i++
                    } else {
                        mascara += char
                    }
                } catch (e: Exception) {
                    break
                }
            }
            isUpdating = true
            setText(mascara)
            setSelection(mascara.length)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {}
    })
}