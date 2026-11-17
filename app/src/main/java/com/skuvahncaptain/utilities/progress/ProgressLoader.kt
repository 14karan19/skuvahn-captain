package com.skuvahncaptain.utilities.progress

import android.app.Dialog
import com.skuvahncaptain.R
import com.skuvahncaptain.databinding.LoaderBinding

class ProgressLoader : ProgressBarDialog<LoaderBinding>(){

    override fun getLayoutResourceId(): Int {
        return R.layout.loader
    }

    override fun onDialogCreated(dialog: Dialog) {

    }
}