package com.skuvahncaptain.module.auth.otp

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.view.View
import androidx.activity.viewModels
import com.hes.vayu.utilities.CommonUtils
import com.skuvahncaptain.R
import com.skuvahncaptain.base.view.BaseActivity
import com.skuvahncaptain.databinding.ActivityOtpBinding
import com.skuvahncaptain.module.auth.login.AuthState
import com.skuvahncaptain.module.auth.login.AuthViewModel
import com.skuvahncaptain.module.home.activities.HomeActivity
import com.skuvahncaptain.module.profile.ProfileActivity
import com.skuvahncaptain.utilities.CustomToast
import dagger.hilt.android.AndroidEntryPoint
import `in`.aabhasjindal.otptextview.OTPListener

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class OTPActivity : BaseActivity<ActivityOtpBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    private var otp = ""
    private lateinit var phone: String

    override fun getLayoutResourceId() = R.layout.activity_otp

    override fun initView() {
        super.initView()

        phone = intent.getStringExtra("PHONE")!!

        binding.phoneNumber.text =
            "${getString(R.string.otp_heading)} ${phone.substring(0, 2)}XXXXXX${phone.takeLast(2)}"

        CommonUtils.disableTextView(binding.cvSend, binding.btnSend, resources)
        CommonUtils.disableEmptyButton(binding.btnResend, resources)

        initTimer()
        attachListeners()
        observe()
    }

    // ---------------- TIMER ----------------

    private fun initTimer() {

        binding.tvTime.visibility = View.VISIBLE
        CommonUtils.disableEmptyButton(binding.btnResend, resources)

        object : CountDownTimer(30000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding.tvTime.text =
                    "Resend code in ${millisUntilFinished / 1000} second(s)"
            }

            override fun onFinish() {
                binding.tvTime.visibility = View.GONE
                CommonUtils.enableEmptyButton(binding.btnResend, resources)
            }

        }.start()
    }

    // ---------------- LISTENERS ----------------

    private fun attachListeners() {

        binding.etOtp.otpListener = object : OTPListener {

            override fun onInteractionListener() {
                binding.tvError.visibility = View.GONE
                otp = binding.etOtp.otp

                if (otp.length == 6)
                    CommonUtils.enableTextView(binding.cvSend, binding.btnSend, resources)
                else
                    CommonUtils.disableTextView(binding.cvSend, binding.btnSend, resources)
            }

            override fun onOTPComplete(otp: String) {
                this@OTPActivity.otp = otp
                verifyOtp()
            }
        }

        binding.btnSend.setOnClickListener {
            verifyOtp()
        }

        binding.btnResend.setOnClickListener {
            CommonUtils.showProgress(this)
            viewModel.resendOtp(phone)
        }

        binding.tvBack.setOnClickListener {
            finish()
        }
    }

    // ---------------- VERIFY ----------------

    private fun verifyOtp() {
        binding.tvError.visibility = View.GONE
        CommonUtils.showProgress(this)
        viewModel.verifyOtp(phone, otp)
    }

    // ---------------- OBSERVER ----------------

    private fun observe() {

        viewModel.state.observe(this) {

            when (it) {

                AuthState.Loading -> Unit

                AuthState.CodeSent -> {
                    CommonUtils.hideProgress()
                    CustomToast.showMessage(this, "OTP Sent Again")
                    initTimer()
                }

                is AuthState.Verified -> {

                    CommonUtils.hideProgress()

                    // SAVE TOKEN
                    pref.token = "Bearer ${it.accessToken}"
                    pref.phoneNumber = phone

                    if (it.isProfileComplete) {
                        pref.isLoggedIn = true
                        openA<HomeActivity>()
                    } else {
                        openA<ProfileActivity>()
                    }

                    finishAffinity()
                }

                is AuthState.Error -> {
                    CommonUtils.hideProgress()
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = it.message
                }

                else -> {}
            }
        }
    }
}