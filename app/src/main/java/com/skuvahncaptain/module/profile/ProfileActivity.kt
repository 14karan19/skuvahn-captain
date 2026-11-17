package com.skuvahncaptain.module.profile

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.hes.vayu.utilities.CommonUtils
import com.hes.vayu.utilities.CommonUtils.onChange
import com.skuvahncaptain.R
import com.skuvahncaptain.base.view.BaseActivity
import com.skuvahncaptain.databinding.ActivityProfileBinding
import com.skuvahncaptain.module.auth.login.LoginActivity
import com.skuvahncaptain.module.home.activities.HomeActivity
import com.skuvahncaptain.module.home.dialog.DeleteConfirmationDialog
import com.skuvahncaptain.module.home.models.RouteResponseModel
import com.skuvahncaptain.utilities.CustomToast
import com.skuvahncaptain.utilities.PreferenceConstants
import com.skuvahncaptain.utilities.image.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()

    private val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private val apiFormatter =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    private var insurance: Calendar? = null
    private var pollution: Calendar? = null

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraUri: Uri
    private var profileUri: Uri? = null
    private val documents = HashMap<String, Uri>()
    private var currentType = ""
    private var isMyProfile = false

    override fun getLayoutResourceId() = R.layout.activity_profile

    override fun initView() {
        super.initView()

        if (intent.hasExtra("MYPROFILE")){
            isMyProfile = true
        }

        binding.etMobile.setText(pref.phoneNumber)

        if (isMyProfile){
            Glide.with(this)
                .load(pref.profilePic)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.imgDriverProfile)

            binding.etFullName.isEnabled = false
            binding.etEmail.isEnabled = false
            binding.etFullName.isClickable = false
            binding.etEmail.isClickable = false

            binding.tvDeleteAccount.visibility = View.VISIBLE

            binding.etFullName.setText(pref.name)
            binding.etEmail.setText(pref.email)
            binding.etMobile.setText(pref.phoneNumber)

            binding.etVehicleNumber.visibility = View.GONE
            binding.etModel.visibility = View.GONE
            binding.etColor.visibility = View.GONE
            binding.etSeat.visibility = View.GONE
            binding.etInsuranceDate.visibility = View.GONE
            binding.etPollutionDate.visibility = View.GONE

            binding.hideLayout.visibility = View.GONE
            binding.btnPickProfileImage.visibility = View.GONE
        }

        setupImagePicker()
        setupClicks()
        observe()
    }

    // ---------------- IMAGE PICKER ----------------

    private fun setupImagePicker() {

        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                if (result.resultCode == RESULT_OK) {

                    val uri = ImagePicker.extractResult(this, result.data, cameraUri)

                    uri?.let {

                        if (currentType == "PROFILE") {
                            profileUri = it
                            binding.imgDriverProfile.setImageURI(it)
                        } else {
                            documents[currentType] = it
                            updateDocUI(currentType)
                        }

                        validate()
                    }
                }
            }
    }

    private fun pickImage(type: String) {
        currentType = type
        cameraUri = ImagePicker.createCameraUri(this)
        val chooser = ImagePicker.createChooser(this, cameraUri)
        imagePickerLauncher.launch(chooser)
    }

    // ---------------- CLICKS ----------------

    private fun setupClicks() {

        binding.tvDeleteAccount.setOnClickListener {
            val dialog = DeleteConfirmationDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(PreferenceConstants.deleteUser, pref.userId)
                }

                listener = object : DeleteConfirmationDialog.OnDeleteConfirmListener {
                    override fun onDeleteConfirmed(
                        address: RouteResponseModel.Route?,
                        deleteUserId: String?
                    ) {
                        deleteUserId?.let {
                            viewModel.deleteAccount(pref)
                        }
                    }
                }
            }

            dialog.show(supportFragmentManager, "DeleteDialog")
        }

        binding.etInsuranceDate.setOnClickListener {
            pickDate {
                insurance = it
                binding.etInsuranceDate.text = dateFormatter.format(it.time)
            }
        }

        binding.etPollutionDate.setOnClickListener {
            pickDate {
                pollution = it
                binding.etPollutionDate.text = dateFormatter.format(it.time)
            }
        }

        binding.btnPickProfileImage.setOnClickListener { pickImage("PROFILE") }

        binding.btnDl.setOnClickListener { pickImage("DL") }
        binding.btnAadhaarFront.setOnClickListener { pickImage("AADHAAR_FRONT") }
        binding.btnAadhaarBack.setOnClickListener { pickImage("AADHAAR_BACK") }
        binding.btnRc.setOnClickListener { pickImage("RC") }
        binding.btnInsurance.setOnClickListener { pickImage("INSURANCE") }
        binding.btnPollution.setOnClickListener { pickImage("POLLUTION") }

        binding.btnSubmit.setOnClickListener {
            if (!validate()) return@setOnClickListener
            submit()
        }

        binding.ivBack.setOnClickListener { finish() }
    }

    // ---------------- VALIDATION ----------------

    private fun validate(): Boolean {

        val isValid =
            binding.etFullName.text.isNotEmpty() &&
                    binding.etVehicleNumber.text.isNotEmpty() &&
                    binding.etModel.text.isNotEmpty() &&
                    binding.etColor.text.isNotEmpty() &&
                    binding.etSeat.text.isNotEmpty() &&
                    pollution != null &&
                    insurance != null &&
                    profileUri != null &&
                    documents.size >= 5

        if (isValid)
            CommonUtils.enableTextView(binding.cvSubmit, binding.btnSubmit, resources)
        else
            CommonUtils.disableTextView(binding.cvSubmit, binding.btnSubmit, resources)

        return isValid
    }

    // ---------------- SUBMIT ----------------
    private fun submit() {
        var finalInsurance = ""
        var finalPollution = ""

        finalInsurance = apiFormatter.format(insurance!!.time)
        finalPollution = apiFormatter.format(pollution!!.time)

        val request = SaveCaptainRequest(
            userId = pref.userId,
            fullName = binding.etFullName.text.toString(),
            phone = binding.etMobile.text.toString(),
            email = binding.etEmail.text.toString().ifBlank { null },
            imageUrl = "", // will be replaced
            registrationNumber = binding.etVehicleNumber.text.toString(),
            model = binding.etModel.text.toString(),
            color = binding.etColor.text.toString(),
            seatCapacity = binding.etSeat.text.toString().toInt(),
            insuranceExpiry = finalInsurance,
            fitnessExpiry = finalPollution,
            pollutionExpiry = finalPollution,
            documents = emptyList()
        )

        viewModel.saveCaptain(
            pref = pref,
            context = this,
            request = request,
            profileUri = profileUri!!,
            documents = documents
        )
    }

    // ---------------- OBSERVE ----------------

    private fun observe() {

        viewModel.state.observe(this) {

            when (it) {

                ProfileState.Loading -> {
                    CommonUtils.showProgress(this)
                }

                is ProfileState.Success -> {
                    CommonUtils.hideProgress()

                    pref.isLoggedIn = true

                    openA<HomeActivity>()
                    finishAffinity()
                }

                is ProfileState.Error -> {
                    CommonUtils.hideProgress()
                    CustomToast.showMessage(this, it.message)
                }

                is ProfileState.DeleteSuccess -> {

                    CommonUtils.hideProgress()

                    CommonUtils.logout(pref,skuVahnDatabase!!,this@ProfileActivity)
                }
            }
        }
    }

    private fun pickDate(onPicked: (Calendar) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, y, m, d ->
                calendar.set(y, m, d)
                onPicked(calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // ---------------- UI ----------------
    private fun updateDocUI(type: String) {

        when (type) {
            "DL" -> binding.tvDlStatus.text = "✔ Uploaded"
            "AADHAAR_FRONT" -> binding.tvAadhaarStatusFront.text = "✔ Uploaded"
            "AADHAAR_BACK" -> binding.tvAadhaarStatusBack.text = "✔ Uploaded"
            "RC" -> binding.tvRcStatus.text = "✔ Uploaded"
            "INSURANCE" -> binding.tvInsuranceStatus.text = "✔ Uploaded"
            "POLLUTION" -> binding.tvPollutionStatus.text = "✔ Uploaded"
        }
    }
}