package com.lottery.lotteryallinone.prefs

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private lateinit var prefs : SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private const val PREFS_NAME = "powerball"
    private const val IS_PURCHASED_No : String = "is_purchased"

    fun init(context: Context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = prefs.edit()
        editor.commit()
    }

    public fun setPurchased(b : Boolean){
        editor.putBoolean(IS_PURCHASED_No, b)
        editor.commit()
    }

    fun isPurchased() : Boolean{
        return prefs.getBoolean(IS_PURCHASED_No, false)
    }
}