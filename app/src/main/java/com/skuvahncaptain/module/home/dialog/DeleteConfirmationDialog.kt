package com.skuvahncaptain.module.home.dialog

import android.os.Build
import androidx.annotation.RequiresApi
import com.skuvahncaptain.R
import com.skuvahncaptain.base.view.BaseDialogFragment
import com.skuvahncaptain.databinding.DialogDeleteConfirmationBinding
import com.skuvahncaptain.module.home.models.RouteResponseModel
import com.skuvahncaptain.utilities.PreferenceConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteConfirmationDialog : BaseDialogFragment<DialogDeleteConfirmationBinding>() {

    interface OnDeleteConfirmListener {
        fun onDeleteConfirmed(address: RouteResponseModel.Route?, deleteUserId: String?)
    }

    var listener: OnDeleteConfirmListener? = null

    private val address by lazy {
        arguments?.getSerializable(PreferenceConstants.address) as? RouteResponseModel.Route
    }

    private val deleteUserId by lazy {
        arguments?.getString(PreferenceConstants.deleteUser)
    }

    override fun getLayoutResourceId() = R.layout.dialog_delete_confirmation

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {

        binding.no.setOnClickListener {
            dismissAllowingStateLoss()
        }

        binding.yes.setOnClickListener {

            listener?.onDeleteConfirmed(address, deleteUserId)

            dismissAllowingStateLoss()
        }
    }

    override fun adjustdisplay() = true
}