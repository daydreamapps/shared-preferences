package com.spatmore.sharedpreferences

import android.content.SharedPreferences
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Generic implementation of PreferenceDelegate
 *
 * Supported value types: String, Int, Long, Float, Boolean, Date
 */
class GenericPreferenceDelegate<T>(
    private val sharedPreferences: SharedPreferences,
    private val valueType: KClass<*>,
    private val key: String?,
    private val default: T? = null
) : PreferenceDelegate<T> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val key = preferenceKey(property)

        @Suppress("UNCHECKED_CAST")
        return when (valueType) {
            String::class -> sharedPreferences.getString(key, default as String? ?: "") as T
            Int::class -> sharedPreferences.getInt(key, default as Int? ?: 0) as T
            Long::class -> sharedPreferences.getLong(key, default as Long? ?: 0L) as T
            Float::class -> sharedPreferences.getFloat(key, default as Float? ?: 0F) as T
            Boolean::class -> sharedPreferences.getBoolean(key, default as Boolean? ?: false) as T
            Date::class -> Date(sharedPreferences.getLong(key, default as Long? ?: 0L)) as T
            else -> throw IllegalArgumentException("Type $valueType not supported")
        }
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val key = preferenceKey(property)

        sharedPreferences.edit().apply {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Boolean -> putBoolean(key, value)
                is Date -> putLong(key, value.time)
                else -> throw IllegalArgumentException("$value not supported")
            }
            apply()
        }
    }

    override fun preferenceKey(property: KProperty<*>): String = key ?: property.name

    companion object {
        inline fun <reified T> build(
            sharedPreferences: SharedPreferences,
            key: String?,
            default: T? = null
        ): GenericPreferenceDelegate<T> {
            return GenericPreferenceDelegate(
                sharedPreferences = sharedPreferences,
                valueType = T::class,
                key = key,
                default = default
            )
        }
    }
}