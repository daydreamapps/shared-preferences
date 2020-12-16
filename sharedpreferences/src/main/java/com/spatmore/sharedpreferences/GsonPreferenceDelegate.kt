package com.spatmore.sharedpreferences

import android.content.SharedPreferences
import com.google.gson.Gson
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class GsonPreferenceDelegate<T>(
    private val type: KClass<*>,
    sharedPreferences: SharedPreferences,
    key: String?,
    private val default: T? = null
) : PreferenceDelegate<T?> {

    private val stringDelegate = StringPreferenceDelegate(sharedPreferences, key)
    private val gson: Gson by lazy { Gson() }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        val json = stringDelegate.getValue(thisRef, property)
        return gson.fromJson<T>(json, type.java) ?: default
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        val json = value?.let(gson::toJson).orEmpty()
        stringDelegate.setValue(thisRef, property, json)
    }

    override fun preferenceKey(property: KProperty<*>): String {
        return stringDelegate.preferenceKey(property)
    }

    companion object {

        inline fun <reified T> build(
            sharedPreferences: SharedPreferences,
            key: String?,
            default: T? = null
        ): GsonPreferenceDelegate<T> {
            return GsonPreferenceDelegate(
                type = T::class,
                sharedPreferences = sharedPreferences,
                key = key,
                default = default
            )
        }
    }
}