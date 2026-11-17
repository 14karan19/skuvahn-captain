package com.skuvahncaptain.module.auth.splash

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.scottyab.rootbeer.RootBeer
import com.skuvahncaptain.BuildConfig
import com.skuvahncaptain.R
import com.skuvahncaptain.base.view.BaseActivity
import com.skuvahncaptain.databinding.ActivitySplashBinding
import com.skuvahncaptain.module.auth.login.LoginActivity
import com.skuvahncaptain.module.home.activities.HomeActivity
import com.skuvahncaptain.network.RootedDeviceActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootBeer = RootBeer(this)
        if (rootBeer.isRooted) {
            openA<RootedDeviceActivity>()
            finish()
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        super.initView()
        binding.version.text = "Version: ${BuildConfig.VERSION_NAME}"

        initListener()
        navigateToNext()
    }

    private fun initListener() {
        binding.btnClose.setOnClickListener {
            finishAffinity()
        }
    }

    private fun navigateToNext() {
        if (pref.isLoggedIn) {
            openA<HomeActivity>()
            finish()
        } else {
            openA<LoginActivity>()
            finish()
        }
    }
}










