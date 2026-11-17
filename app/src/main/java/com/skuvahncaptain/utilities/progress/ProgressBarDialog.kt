package com.skuvahncaptain.utilities.progress

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding

abstract class ProgressBarDialog<V : ViewBinding> {

    private var dialog: Dialog? = null
    private var attachedWith: Activity? = null
    private lateinit var viewBinding: V
    val binding get() = viewBinding

    private fun createDialog(context: Context): Dialog {
        return Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)

            viewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                getLayoutResourceId(),
                null,
                false
            );
            setContentView(viewBinding.root)
            setCancelable(false)
            setCanceledOnTouchOutside(false)

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val lp = WindowManager.LayoutParams()
                    .apply {
                        copyFrom(attributes)
                        width = WindowManager.LayoutParams.MATCH_PARENT
                        height = WindowManager.LayoutParams.WRAP_CONTENT
                    }
                attributes = lp
            }
        }
    }
    abstract fun getLayoutResourceId(): Int

    @SuppressLint("SuspiciousIndentation")
    fun initlize(context: Activity) {
        if (attachedWith?.isDestroyed == false) dialog?.dismiss()
        dialog = createDialog(context)
        onDialogCreated(dialog!!)
        attachedWith = context
    }

    abstract fun onDialogCreated(dialog: Dialog)

    fun toogle(show: Boolean) {
        if (show) show() else hide()
    }

    fun show() {
        if (attachedWith?.isDestroyed == false) dialog?.show()
    }

    fun hide() {
        if (attachedWith?.isDestroyed == false) dialog?.dismiss()
    }

}