package com.skuvahncaptain.network

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.skuvahncaptain.databinding.ActivityNetworkErrorBinding
import com.skuvahncaptain.utilities.CustomToast
import kotlinx.coroutines.launch

class NetworkErrorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNetworkErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNetworkErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSend.setOnClickListener {
            lifecycleScope.launch {
                if (isWiFiConnectedWithInternet()) {
                    // Internet is available, return to splash screen
                    finish()
                } else {
                    CustomToast.showMessage(
                        this@NetworkErrorActivity,
                        "Connect to a Network"
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        // do nothing
    }
}