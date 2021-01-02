//package com.spatmore.sharedpreferences
//
//import android.content.SharedPreferences
//import com.google.gson.Gson
//import java.util.*
//import kotlin.reflect.KClass
//import kotlin.reflect.KProperty
//
//internal class DatePreferenceDelegate(
//    private val delegate: GsonPreferenceDelegate<Date>,
//    private val type: KClass<*>,
//    private val default: Date? = null
//) : PreferenceDelegate<Date?> {
//
//    constructor(
//        type: KClass<*>,
//        sharedPreferences: SharedPreferences,
//        key: String?,
//        default: Date? = null
//    ) : this(
//        delegate = GsonPreferenceDelegate.build(sharedPreferences, key, default),
//        type = type,
//        default = default
//    )
//
//    override fun getValue(thisRef: Any?, property: KProperty<*>): Date? {
//        val json = stringDelegate.getValue(thisRef, property)
//        return gson.fromJson<Date>(json, type.java) ?: default
//    }
//
//    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Date?) {
//        val json = value?.let(gson::toJson).orEmpty()
//        stringDelegate.setValue(thisRef, property, json)
//    }
//
//    override fun preferenceKey(property: KProperty<*>): String {
//        return stringDelegate.preferenceKey(property)
//    }
//
//    companion object {
//
//        inline fun <reified T> build(
//            sharedPreferences: SharedPreferences,
//            key: String?,
//            default: T? = null
//        ): DatePreferenceDelegate<T> {
//            return DatePreferenceDelegate(
//                type = T::class,
//                sharedPreferences = sharedPreferences,
//                key = key,
//                default = default
//            )
//        }
//    }
//}