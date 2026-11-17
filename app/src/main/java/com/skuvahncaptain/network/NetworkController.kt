package com.skuvahncaptain.network

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NetworkController(private val mContext: Context) {

    private val receiver by lazy { NetworkDetector() }

    fun attach(){
        mContext.registerReceiver(receiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))

        GlobalScope.launch {
            receiver.searched.collect() {
                if (!it){
                    //Network changed
                    val intent= Intent(mContext,NetworkErrorActivity::class.java)
                    mContext.startActivity(intent)
                }
            }
        }

    }
}
