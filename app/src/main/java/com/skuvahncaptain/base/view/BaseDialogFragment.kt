package com.skuvahncaptain.base.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.skuvahncaptain.base.listeners.BaseListener

abstract class BaseDialogFragment<V : ViewBinding> : DialogFragment() {

    protected lateinit var baseListener: BaseListener

    private lateinit var viewBinding: V
    val binding get() = viewBinding

    protected val metrics by lazy {
        val display = activity?.windowManager?.defaultDisplay
        val outMetrics = DisplayMetrics()
        display?.getMetrics(outMetrics)
        outMetrics
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

//        requireActivity().window.setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE
//        )
        baseListener = context as BaseListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

        if (adjustdisplay()) setdialogwidth(dialog)

        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding =
            DataBindingUtil.inflate(layoutInflater, getLayoutResourceId(), container, false)
        initView()

        return viewBinding.root
    }

    abstract fun initView()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (adjustdisplay()) setdialogwidth(dialog!!)
    }

    abstract fun adjustdisplay(): Boolean

    private fun setdialogwidth(dialog: Dialog) {
        val adjustedwidth = adjustedWidth()
        setWidth(dialog, adjustedwidth)
    }

    protected open fun setWidth(dialog: Dialog, adjustedwidth: Int) {
        val attrs = dialog.window?.attributes
        attrs?.width = adjustedwidth
    }

    protected fun setHeight(dialog: Dialog, height: Int) {
        val attrs = dialog.window?.attributes
        attrs?.height = height
    }

    private fun setDisplay() {
        val display = activity?.windowManager?.defaultDisplay
        val outMetrics = DisplayMetrics()
        display?.getMetrics(outMetrics)
        //   dialog.window.setLayout((312 * outMetrics.density).toInt(), (436 * outMetrics.density).toInt())

        val attrs = dialog?.window?.attributes
        attrs?.width = adjustedWidth()
    }

    private fun adjustedWidth(): Int {
        activity?.run {
            return (metrics.widthPixels - convertDpToPx(16))
        }
        return metrics.widthPixels
    }


    fun Context.convertDpToPx(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics)
            .toInt()
    }
    abstract fun getLayoutResourceId(): Int

    override fun onPause() {
        super.onPause()
//        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }
}