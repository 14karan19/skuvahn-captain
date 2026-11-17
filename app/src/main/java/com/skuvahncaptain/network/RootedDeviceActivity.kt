package com.skuvahncaptain.network

import com.skuvahncaptain.base.view.BaseActivity
import com.skuvahncaptain.R
import com.skuvahncaptain.databinding.ActivityRootedDeviceErrorBinding

class RootedDeviceActivity : BaseActivity<ActivityRootedDeviceErrorBinding>() {

    override fun initView() {
        super.initView()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_rooted_device_error
    }

    override fun onBackPressed() {
        //do nothing
    }
}