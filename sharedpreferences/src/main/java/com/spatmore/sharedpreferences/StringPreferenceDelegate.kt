package com.spatmore.sharedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.reflect.KProperty

class StringPreferenceDelegate(
    private val sharedPreferences: SharedPreferences,
    private val key: String?,
    private val default: String = ""
) : PreferenceDelegate<String> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return sharedPreferences.getString(preferenceKey(property), null) ?: default
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        sharedPreferences.edit {
            putString(preferenceKey(property), value)
        }
    }

    override fun preferenceKey(property: KProperty<*>): String = key ?: property.name
}