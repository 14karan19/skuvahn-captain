package com.skuvahncaptain.utilities

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.skuvahncaptain.R

object CustomToast {

    fun showSnackBar(view: View, msg: String) {
        val snack = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
        snack.show()
    }

    fun showMessage(context: Context, message: String) {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, null)
        val textviewMessage = layout.findViewById<TextView>(R.id.tv_msg)

        textviewMessage.text = message

        val toast = Toast(context)
        toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 8, 40)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }

}
