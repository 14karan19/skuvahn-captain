package com.skuvahncaptain.base.listeners

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

interface BaseListener {
    fun openA(kClass: KClass<out AppCompatActivity>, extras: Bundle? = Bundle())
}