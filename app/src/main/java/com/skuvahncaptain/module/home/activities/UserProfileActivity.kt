package com.skuvahncaptain.module.home.activities

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.hes.vayu.utilities.*
import com.skuvahncaptain.BuildConfig
import com.skuvahncaptain.R
import com.skuvahncaptain.base.view.BaseActivity
import com.skuvahncaptain.databinding.ActivityUserProfileBinding
import com.skuvahncaptain.module.profile.ProfileActivity
import com.skuvahncaptain.utilities.ApiConstants
import com.skuvahncaptain.utilities.CustomToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileActivity : BaseActivity<ActivityUserProfileBinding>() {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun initView() {
        super.initView()

        window.statusBarColor = getColor(R.color.light_blue)

        binding.tvName.text = pref.name
        binding.tvEmail.text = pref.email
        binding.tvPhone.text = "+91 ${pref.phoneNumber}"

        Glide.with(this)
            .load(pref.profilePic)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.imgProfile)

        binding.profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java).apply {
                putExtra("MYPROFILE", true)
            }
            startActivity(intent)
        }

        binding.contactSupport.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                val recipients = arrayOf("karan.lamba.1419@gmail.com")
                intent.putExtra(Intent.EXTRA_EMAIL, recipients)
                intent.putExtra(Intent.EXTRA_SUBJECT, "Skuavahn App Support")
                intent.putExtra(
                    Intent.EXTRA_TEXT, "\n" +
                            "\n" +
                            "\n" +
                            "${Build.DEVICE}, ${Build.MODEL}, ${Build.BRAND}\n" +
                            "Operating System : ${Build.VERSION()}\n" +
                            "Version : ${BuildConfig.VERSION_NAME}\n"
                )

                intent.type = "text/html"
                intent.setPackage("com.google.android.gm")
                startActivity(Intent.createChooser(intent, "Send mail"))
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                CustomToast.showMessage(this, ApiConstants.something_wrong)
            }
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.logout.setOnClickListener {
            CommonUtils.logout(pref, skuVahnDatabase!!, this)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_user_profile
    }

}