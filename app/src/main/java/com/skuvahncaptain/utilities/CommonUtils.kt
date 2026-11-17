package com.hes.vayu.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import com.skuvahncaptain.R
import com.skuvahncaptain.base.progress.CustomProgressDialog
import com.skuvahncaptain.database.SkuVahnDatabase
import com.skuvahncaptain.module.auth.login.LoginActivity
import com.skuvahncaptain.utilities.AppPref
import com.skuvahncaptain.utilities.network.ConnectionDetector
import com.skuvahncaptain.utilities.DateConstants
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


@SuppressLint("UseCompatLoadingForDrawables", "StaticFieldLeak")
object CommonUtils {

    var dialog: Dialog? = null

    var tvMessage: TextView? = null

    var userToken: String = ""
    var groupCodeValue: String = ""

    @SuppressLint("StaticFieldLeak")
    var progressDialog: CustomProgressDialog? = null

    fun disableButton(cardView: CardView, view: Button, resource: Resources) {
        view.isEnabled = false
        cardView.setCardBackgroundColor(resource.getColor(R.color.disable))
        view.setTextColor(resource.getColor(R.color.gray))
    }

    fun enableButton(cardView: CardView, view: Button, resource: Resources) {
        view.isEnabled = true
        cardView.setCardBackgroundColor(resource.getColor(R.color.theme))
        view.setTextColor(resource.getColor(R.color.white))
    }

    fun disableTextView(cardView: CardView, view: TextView, resource: Resources) {
        view.isEnabled = false
        cardView.setCardBackgroundColor(resource.getColor(R.color.disable))
        view.setTextColor(resource.getColor(R.color.gray))
    }

    fun enableTextView(cardView: CardView, view: TextView, resource: Resources) {
        view.isEnabled = true
        cardView.setCardBackgroundColor(resource.getColor(R.color.theme))
        view.setTextColor(resource.getColor(R.color.white))
    }

    fun disableEmptyButton(view: Button, resource: Resources) {
        view.isEnabled = false
        view.background = resource.getDrawable(R.drawable.bg_button_empty_disable)
        view.setTextColor(resource.getColor(R.color.gray))
    }

    fun enableEmptyButton(view: Button, resource: Resources) {
        view.isEnabled = true
        view.background = resource.getDrawable(R.drawable.bg_button_empty)
        view.setTextColor(resource.getColor(R.color.theme))
    }

    fun disableView(cardView: CardView, view: TextView, resource: Resources) {
        cardView.isEnabled = false
        cardView.setCardBackgroundColor(resource.getColor(R.color.disable))
        view.setTextColor(resource.getColor(R.color.gray))
    }

    fun enableView(cardView: CardView, view: TextView, resource: Resources) {
        cardView.isEnabled = true
        cardView.setCardBackgroundColor(resource.getColor(R.color.orange))
        view.setTextColor(resource.getColor(R.color.white))
    }

    fun disableEmptyView(layout: LinearLayout, view: TextView, resource: Resources) {
        layout.isEnabled = false
        layout.background = resource.getDrawable(R.drawable.bg_button_empty_disable)
        view.setTextColor(resource.getColor(R.color.gray))
    }

    fun enableEmptyView(layout: LinearLayout, view: TextView, resource: Resources) {
        layout.isEnabled = true
        layout.background = resource.getDrawable(R.drawable.bg_button_empty_red)
        view.setTextColor(resource.getColor(R.color.red))
    }

    fun errorField(editText: EditText, resource: Resources) {
        editText.setBackgroundResource(R.drawable.bg_edittext_error)
    }

    fun validField(editText: EditText, resource: Resources) {
        editText.setBackgroundResource(R.drawable.bg_edittext)
    }

    fun validFieldBlue(editText: EditText, resource: Resources) {
        editText.setBackgroundResource(R.drawable.bg_edittext_blue)
    }

    fun hideKeyboard(context: Context?, v: View) {
        val imm: InputMethodManager? =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun checkEmptyString(value: EditText): Boolean {
        return value.text.toString().trim().equals("")
    }

    fun checkPasswordLength(value: EditText): Boolean {
        return value.text.toString().trim().length < 6
    }

    fun checkPhoneNumberLength(value: EditText): Boolean {
        return value.text.toString().trim().length < 10
    }

    fun fieldValue(field: EditText): String {
        return field.text.trim().toString()
    }

    fun showProgress(context: Context) {
        if (progressDialog != null)
            progressDialog!!.dismiss()

        progressDialog = CustomProgressDialog.create(context)
            .setStyle(CustomProgressDialog.Style.SPIN_INDETERMINATE)
        progressDialog!!.setCancellable(false)

        if (!progressDialog!!.isShowing) {
            progressDialog!!.show()
        }
    }

    fun hideProgress() {
        progressDialog!!.dismiss()
    }

    fun showLoader(context: Context, message: String = "Loading...") {
        if (dialog?.isShowing == true) return

        dialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.progress_loader)

            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            tvMessage = findViewById(R.id.tvProgressMessage)
            tvMessage?.text = message

            show()
        }
    }

    fun updateMessage(message: String) {
        tvMessage?.text = message
    }

    fun hideLoader() {
        dialog?.dismiss()
        dialog = null
        tvMessage = null
    }

//    fun saveUser(user: OTPResponseModel.UserData, pref: AppPref) {
//        pref.token = "Bearer ${user.accessToken!!}"
//        userToken = "Bearer ${user.accessToken!!}"
//        pref.groupCode = user.groupCode ?: ""
//        groupCodeValue = user.groupCode ?: ""
//        pref.userId = user.userId!!
//        pref.expiresIn = user.expiresIn!!
//        pref.allowOffline = user.allowOffline!!
//        pref.roleName = user.roleName ?: ""
//        pref.isLoggedIn = true
//    }

    fun setToken(value: String) {
        userToken = value
    }

    fun getToken(): String {
        return userToken
    }

    fun setGroupCode(value: String) {
        groupCodeValue = value
    }

    fun getGroupCode(): String {
        return groupCodeValue
    }

    fun logout(pref: AppPref, skuVahnDatabase: SkuVahnDatabase, activity: Activity) {
        pref.clearAll()
        activity.startActivity(Intent(activity, LoginActivity::class.java))
        activity.finishAffinity()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getDateTime(date: String): String {
        var finalDate = "-"

        try {
            var spf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSXXX")
            var spfTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSXXX")
            val newDate: Date = spf.parse(date)
            val newTime: Date = spfTime.parse(date)
            spf = SimpleDateFormat("dd-MM-yyyy")
            spfTime = SimpleDateFormat("hh:mm a")
            finalDate = "${spf.format(newDate)}, ${spfTime.format(newTime)}"
        } catch (ex: Exception) {
            try {
                var spf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX")
                var spfTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX")
                val newDate: Date = spf.parse(date)
                val newTime: Date = spfTime.parse(date)
                spf = SimpleDateFormat("dd-MM-yyyy")
                spfTime = SimpleDateFormat("hh:mm a")
                finalDate = "${spf.format(newDate)}, ${spfTime.format(newTime)}"
            } catch (ex: Exception) {
                finalDate = "-"
            }
        }

        return finalDate
    }

    fun getDate(date: String): String {
        var finalDate = "-"

        try {
            var spf = SimpleDateFormat(DateConstants.dateFormat1)
            val newDate: Date = spf.parse(date)
            spf = SimpleDateFormat(DateConstants.outputFormat)
            finalDate = spf.format(newDate)
        } catch (ex: Exception) {
            try {
                var spf = SimpleDateFormat(DateConstants.dateFormat2)
                val newDate: Date = spf.parse(date)
                spf = SimpleDateFormat(DateConstants.outputFormat)
                finalDate = spf.format(newDate)
            } catch (ex: Exception) {
                finalDate = "-"
            }
        }

        return finalDate
    }

    fun isConnectingToInternet(context: Context): Boolean {
        val connectionDetector = ConnectionDetector(context)
        return connectionDetector.isConnectingToInternet()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentUTC(): String? {
        val time = Calendar.getInstance().time
        val outputFmt = SimpleDateFormat(DateConstants.dateFormat1)
        outputFmt.timeZone = TimeZone.getTimeZone("UTC")
        return outputFmt.format(time)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decryptString(key: String, cipherText: String): String {
        val iv = ByteArray(16)
        val buffer = Base64.getDecoder().decode(cipherText)
        val aes = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
        val ivSpec = IvParameterSpec(iv)
        aes.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        val decryptedBytes = aes.doFinal(buffer)
        val inputStream = ByteArrayInputStream(decryptedBytes)
        val reader = InputStreamReader(inputStream)
        return reader.readText()
    }

    fun appendRecord(context: Context, tag: String, value: String) {
        val fileName = "records.txt"
        val file = File(context.filesDir, fileName)

        // Append record in format: tag=value
        file.appendText("$tag = $value\n")
    }

    fun sendFileViaGmail(context: Context) {
        val fileName = "records.txt"
        val file = File(context.filesDir, fileName)

        if (!file.exists()) {
            Toast.makeText(context, "No records file found", Toast.LENGTH_SHORT).show()
            return
        }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Records File")
            putExtra(Intent.EXTRA_TEXT, "Please find attached records file.")
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        // Open Gmail (or any mail app)
        context.startActivity(Intent.createChooser(intent, "Send email via:"))
    }

    fun EditText.onChange(action: (String) -> Unit) {
        this.doAfterTextChanged { editable ->
            action(editable?.toString().orEmpty())
        }
    }

    fun EditText.showError(
        errorView: View,
        message: String,
        resources: Resources
    ) {
        errorView.visibility = View.VISIBLE
        if (errorView is android.widget.TextView) {
            errorView.text = message
        }
        CommonUtils.errorField(this, resources)
    }

    fun EditText.clearError(
        errorView: View,
        resources: Resources
    ) {
        errorView.visibility = View.GONE
        CommonUtils.validField(this, resources)
    }

    fun EditText.isNotEmpty(): Boolean =
        this.text.toString().trim().isNotEmpty()

    fun checkValidEmail(value: EditText): Boolean {
        val pattern =
            Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

        return pattern.matcher(value.text.toString().trim()).matches()
    }

    fun isValidPasswordFormat(password: String): Boolean {
        val passwordREGEX = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"
        );
        return passwordREGEX.matcher(password).matches()
    }

    fun isValidIndianMobile(value: EditText): Boolean {
        val mobile = value.text.toString().trim()
        val pattern = Regex("^[6-9]\\d{9}$")
        return pattern.matches(mobile)
    }
}
