package com.spatmore.sharedpreferences

import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.junit.Test
import kotlin.reflect.KProperty

private const val PROPERTY_NAME: String = "property name"
private const val KEY: String = "preference-key"

class GsonPreferenceDelegateTest {

    private val sharedPreferences: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk()
    private val property: KProperty<*> = mockk()
    private val value = Dummy("Woot")
    private val jsonValue = "{\"x\":\"Woot\"}"
    private val defaultValue = Dummy("empty")

    private data class Dummy(val x: String)

    @Test
    fun `preferenceKey - key null - property name`() {
        every { property.name } returns PROPERTY_NAME

        GsonPreferenceDelegate.build<Dummy>(
            sharedPreferences = mockk(),
            key = null
        ).apply {
            assertThat(preferenceKey(property)).isEqualTo(PROPERTY_NAME)
        }

        verify { property.name }
    }

    @Test
    fun `preferenceKey - key nonnull - key`() {
        GsonPreferenceDelegate.build<Dummy>(
            sharedPreferences = mockk(),
            key = KEY
        ).apply {
            assertThat(preferenceKey(mockk())).isEqualTo(KEY)
        }
    }

    @Test
    fun `getValue - sharedPreferences returns null, default not specified - null`() {
        every { sharedPreferences.getString(KEY, null) } returns null

        GsonPreferenceDelegate.build<Dummy>(
            sharedPreferences = sharedPreferences,
            key = KEY
        ).apply {
            assertThat(getValue(mockk(), property)).isNull()
        }

        verify { sharedPreferences.getString(KEY, null) }
    }

    @Test
    fun `getValue - sharedPreferences returns null, default not empty - default`() {
        every { sharedPreferences.getString(KEY, null) } returns null

        GsonPreferenceDelegate.build<Dummy>(
            sharedPreferences = sharedPreferences,
            key = KEY,
            default = defaultValue
        ).apply {
            assertThat(getValue(mockk(), property)).isEqualTo(defaultValue)
        }

        verify { sharedPreferences.getString(KEY, null) }
    }

    @Test
    fun `getValue - sharedPreferences returns value - value`() {
        every { sharedPreferences.getString(KEY, null) } returns jsonValue

        GsonPreferenceDelegate.build<Dummy>(
            sharedPreferences = sharedPreferences,
            key = KEY
        ).apply {
            assertThat(getValue(mockk(), property)).isEqualTo(value)
        }

        verify { sharedPreferences.getString(KEY, null) }
    }

    @Test
    fun `setValue - null - store sharedPreferences value`() {
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(KEY, "") } returns editor
        every { editor.apply() } just Runs

        GsonPreferenceDelegate.build<Dummy>(
            sharedPreferences = sharedPreferences,
            key = KEY
        ).setValue(mockk(), property, null)

        verify {
            sharedPreferences.edit()
            editor.putString(KEY, "")
            editor.apply()
        }
    }

    @Test
    fun `setValue - nonnull - store sharedPreferences value`() {
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(KEY, jsonValue) } returns editor
        every { editor.apply() } just Runs

        GsonPreferenceDelegate.build<Dummy>(
            sharedPreferences = sharedPreferences,
            key = KEY
        ).setValue(mockk(), property, value)

        verify {
            sharedPreferences.edit()
            editor.putString(KEY, jsonValue)
            editor.apply()
        }
    }
}