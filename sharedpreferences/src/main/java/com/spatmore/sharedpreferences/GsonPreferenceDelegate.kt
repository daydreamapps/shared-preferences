package com.spatmore.sharedpreferences

import android.content.SharedPreferences
import com.google.gson.Gson
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

internal class GsonPreferenceDelegate<T>(
    private val stringDelegate: StringPreferenceDelegate,
    private val gson: Gson,
    private val type: KClass<*>,
    private val default: T? = null
) : PreferenceDelegate<T?> {

    constructor(
        type: KClass<*>,
        sharedPreferences: SharedPreferences,
        key: String?,
        default: T? = null
    ) : this(
        stringDelegate = StringPreferenceDelegate(sharedPreferences, key),
        gson = Gson(),
        type = type,
        default = default
    )

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