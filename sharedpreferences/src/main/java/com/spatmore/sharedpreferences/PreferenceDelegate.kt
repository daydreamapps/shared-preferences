package com.spatmore.sharedpreferences

import kotlin.reflect.KProperty

interface PreferenceDelegate<T> {

    fun getValue(thisRef: Any?, property: KProperty<*>): T
    fun setValue(thisRef: Any?, property: KProperty<*>, value: T)

    fun preferenceKey(property: KProperty<*>): String
}