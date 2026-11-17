package com.skuvahncaptain.base.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.skuvahncaptain.base.listeners.BaseListener
import com.skuvahncaptain.database.SkuVahnDatabase
import java.util.concurrent.locks.ReentrantLock
import kotlin.reflect.KClass


abstract class BaseFragment<V : ViewBinding>: Fragment() {

    lateinit var baselistener: BaseListener
    private val lock = ReentrantLock()
    private lateinit var viewBinding: V
    val binding get() = viewBinding

    var skuVahnDatabase: SkuVahnDatabase? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

//        requireActivity().window.setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE
//        )

        if (context is BaseListener) {
            baselistener = context
        } else {
            throw IllegalStateException("You Must have to extends your activity with KotlinBaseActivity")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        skuVahnDatabase = SkuVahnDatabase.getInstance(requireActivity())
//        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        return init(container)
    }

    private fun init(viewGroup: ViewGroup?): View {
        viewBinding =
            DataBindingUtil.inflate(layoutInflater, getLayoutResourceId(), viewGroup, false)
        initView()
        return viewBinding.root

    }
    open fun initView() {

    }

    abstract fun getLayoutResourceId(): Int

    fun <T : ViewBinding> showDialog(
        clazz: KClass<out BaseDialogFragment<T>>,
        bundle: Bundle? = Bundle()
    ): BaseDialogFragment<T> {
        try {

            val tag = clazz.simpleName

            var fragment = childFragmentManager.findFragmentByTag(tag)
            if (fragment != null) {
                val ft = childFragmentManager.beginTransaction()
                ft.remove(fragment).commit()
            }
            val ft = childFragmentManager.beginTransaction()
            ft.addToBackStack(tag)

            // Create and show the dialog.
            fragment = clazz.java.newInstance()
            fragment.arguments = bundle
            fragment.show(ft, tag)

            return fragment
        } finally {

        }
    }

    @DrawableRes
    open fun setBreadCrumbsImage(): Int? {
        return null
    }

    open fun setBreadCrumbsTitle(): String {
        return ""
    }

    inline fun <reified T : AppCompatActivity> openA(bundle: Bundle = Bundle()) {
        `access$baselistener`.openA(T::class, bundle)
    }


    @PublishedApi
    internal var `access$baselistener`: BaseListener
    get() = baselistener
    set(value) {
        baselistener = value
    }

    override fun onPause() {
        super.onPause()
//        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

}