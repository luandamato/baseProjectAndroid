package com.example.base_project.Utils.Views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDialog
import com.example.base_project.R

class CustomDialog(context: Context) : AppCompatDialog(context) {

    private var title: String? = null
    private var body: String? = null
    private var ok: String? = null
    private var okAction: (() -> Unit)? = null
    private var cancel: String? = null
    private var cancelAction: (() -> Unit)? = null
    private var dismissAction: (() -> Unit)? = null

    private lateinit var dialogTitle: TextView
    private lateinit var dialogBody: TextView
    private lateinit var dialogOk: CustomButton
    private lateinit var dialogCancel: CustomButton

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_default)
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        dialogTitle = findViewById(R.id.dialogTitle)!!
        dialogBody = findViewById(R.id.dialogBody)!!
        dialogOk = findViewById(R.id.dialogOk)!!
        dialogCancel = findViewById(R.id.dialogCancel)!!

        setView()
    }

    override fun dismiss() {
        dismissAction?.invoke()
        super.dismiss()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setView() {
        title?.let { dialogTitle.text = title }
        body?.let { dialogBody.text = body }
        ok?.let { dialogOk.setButtonText(it) }
        cancel?.let { dialogCancel.setButtonText(it) }
        dialogOk.setOnClickListener {
            okAction?.invoke()
            dismiss()
        }
        dialogCancel.setVisible(cancel != null || cancelAction != null || okAction != null)
        dialogCancel.setOnClickListener {
            cancelAction?.invoke()
            dismiss()
        }
    }

    fun setTitle(title: String): CustomDialog {
        this.title = title
        return this
    }

    fun setBody(body: String): CustomDialog {
        this.body = body
        return this
    }

    fun setOkText(ok: String): CustomDialog {
        this.ok = ok
        return this
    }

    fun setOkAction(action: () -> Unit): CustomDialog {
        okAction = action
        return this
    }

    fun setCancelText(cancel: String): CustomDialog {
        this.cancel = cancel
        return this
    }

    fun setCancelAction(action: () -> Unit): CustomDialog {
        cancelAction = action
        return this
    }

    fun setDismissAction(action: () -> Unit): CustomDialog {
        dismissAction = action
        return this
    }
}