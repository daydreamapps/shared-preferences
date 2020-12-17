package com.spatmore.sharedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.reflect.KProperty

class LongPreferenceDelegate(
    private val sharedPreferences: SharedPreferences,
    private val key: String?,
    private val default: Long = 0L
) : PreferenceDelegate<Long> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): Long {
        return sharedPreferences.getLong(preferenceKey(property), default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
        sharedPreferences.edit {
            putLong(preferenceKey(property), value)
        }
    }

    override fun preferenceKey(property: KProperty<*>): String = key ?: property.name
}