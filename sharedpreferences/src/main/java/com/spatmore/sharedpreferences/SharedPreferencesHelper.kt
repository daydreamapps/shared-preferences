package com.spatmore.sharedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.reflect.KProperty

@Deprecated("Abstract class will be removed by version 1.0. Current approach is incomparable with current practices.")
abstract class SharedPreferencesHelper(
    val sharedPreferences: SharedPreferences
) {

    inline fun <reified T : Any> preference(
        name: String = "",
        default: T? = null
    ): Delegate<T> {
        return object : Delegate<T> {

            override fun getValue(thisRef: Any?, property: KProperty<*>): T {
                return if (name.isNotBlank()) {
                    name.getValue(default)
                } else {
                    property.name.getValue(default)
                }
            }

            override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
                return if (name.isNotBlank()) {
                    name.putValue(value)
                } else {
                    property.name.putValue(value)
                }
            }
        }
    }

    interface Delegate<T> {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
    }

    inline fun <reified T> String.getValue(
        default: T? = null
    ): T {
        if (T::class == String::class) return sharedPreferences.getString(this, default as String? ?: "") as T
        if (T::class == Int::class) return sharedPreferences.getInt(this, default as Int? ?: 0) as T
        if (T::class == Long::class) return sharedPreferences.getLong(this, default as Long? ?: 0L) as T
        if (T::class == Float::class) return sharedPreferences.getFloat(this, default as Float? ?: 0F) as T
        if (T::class == Boolean::class) return sharedPreferences.getBoolean(this, default as Boolean? ?: false) as T

        throw IllegalArgumentException("${T::class} not supported")
    }

    fun String.putValue(value: Any) {
        sharedPreferences.edit {
            when (value) {
                is String -> putString(this@putValue, value)
                is Int -> putInt(this@putValue, value)
                is Long -> putLong(this@putValue, value)
                is Float -> putFloat(this@putValue, value)
                is Boolean -> putBoolean(this@putValue, value)
                else -> throw IllegalArgumentException("${value::class} not supported")
            }
        }
    }
}