package com.yjf.myvideo.util

import android.content.res.Resources
import android.util.TypedValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun dp2px(dp:Int):Float{

    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        Resources.getSystem().displayMetrics)
}


inline fun <reified T> genericType() = object: TypeToken<T>() {}.type


inline fun <reified T : Any> Gson.fromJson(json: String): T {
    return Gson().fromJson(json, T::class.java)
}