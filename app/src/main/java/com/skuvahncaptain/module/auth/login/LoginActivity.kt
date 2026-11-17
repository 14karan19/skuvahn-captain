package com.skuvahncaptain.module.auth.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.hes.vayu.utilities.*
import com.hes.vayu.utilities.CommonUtils.clearError
import com.hes.vayu.utilities.CommonUtils.onChange
import com.hes.vayu.utilities.CommonUtils.showError
import com.skuvahncaptain.BuildConfig
import com.skuvahncaptain.R
import com.skuvahncaptain.base.view.BaseActivity
import com.skuvahncaptain.databinding.ActivityLoginBinding
import com.skuvahncaptain.module.auth.otp.OTPActivity
import com.skuvahncaptain.utilities.CustomToast
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_login
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        super.initView()

        binding.version.text = "Version: ${BuildConfig.VERSION_NAME}"

        CommonUtils.disableTextView(binding.cvSend, binding.btnSend, resources)

        attachListeners()
        observeLoginState()
    }

    private fun attachListeners() {

        binding.btnSend.setOnClickListener {
            if (!validateForm()) return@setOnClickListener

            sendOtp()
        }

        attachChangeListeners()
        attachTouchListener(binding.root)
    }

    private fun attachChangeListeners() {

        binding.etMobile.onChange { validateForm() }
        binding.etEmail.onChange { validateForm() }
        binding.etPassword.onChange { validateForm() }
    }

    private fun validateForm(): Boolean {

        var isValid = false

        val mobile = binding.etMobile.text.toString().trim()

        when {
            mobile.isEmpty() -> {
                binding.etMobile.showError(
                    binding.tvErrorMobile,
                    getString(R.string.enter_mobile),
                    resources
                )
            }

            !CommonUtils.isValidIndianMobile(binding.etMobile) -> {
                binding.etMobile.showError(
                    binding.tvErrorMobile,
                    getString(R.string.enter_valid_mobile),
                    resources
                )
            }

            else -> {
                binding.etMobile.clearError(binding.tvErrorMobile, resources)
                isValid = true
            }
        }

        if (isValid) {
            CommonUtils.enableTextView(binding.cvSend, binding.btnSend, resources)
        } else {
            CommonUtils.disableTextView(binding.cvSend, binding.btnSend, resources)
        }

        return isValid
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun attachTouchListener(root: View) {
        root.setOnTouchListener { v, event ->
            if (event?.action == MotionEvent.ACTION_DOWN) {
                CommonUtils.hideKeyboard(this, binding.root)
            }
            v?.onTouchEvent(event) ?: true
        }
    }

    private fun clearMobile() {
        binding.etMobile.text?.clear()
        binding.etMobile.clearError(binding.tvErrorMobile, resources)
    }

    private fun clearEmailPassword() {
        binding.etEmail.text?.clear()
        binding.etPassword.text?.clear()
        binding.etEmail.clearError(binding.tvErrorEmail, resources)
        binding.etPassword.clearError(binding.tvErrorPassword, resources)
    }

    private fun sendOtp() {

        val mobile = binding.etMobile.text.toString().trim()

        binding.btnSend.isEnabled = false
        CommonUtils.showProgress(this)

        viewModel.sendOtp(mobile)
    }

    private fun observeLoginState() {

        viewModel.state.observe(this) { state ->

            when (state) {

                AuthState.Loading -> Unit

                is AuthState.OtpSent -> {

                    CommonUtils.hideProgress()
                    binding.btnSend.isEnabled = true

                    // SAVE USER ID
                    pref.userId = state.userId

                    val intent = Intent(this, OTPActivity::class.java).apply {
                        putExtra("PHONE", binding.etMobile.text.toString().trim())
                    }
                    startActivity(intent)
                }

                is AuthState.Error -> {
                    CommonUtils.hideProgress()
                    binding.btnSend.isEnabled = true
                    CustomToast.showMessage(this, state.message)
                }

                else -> Unit
            }
        }
    }
}