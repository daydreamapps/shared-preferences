package com.spatmore.sharedpreferences

import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.junit.Test
import kotlin.reflect.KProperty

private const val PROPERTY_NAME: String = "property name"
private const val KEY: String = "preference-key"
private const val VALUE: String = "preference-value"
private const val DEFAULT: String = "preference-default"

class StringPreferenceDelegateTest {

    private val sharedPreferences: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk()
    private val property: KProperty<*> = mockk()

    @Test
    fun `preferenceKey - key null - property name`() {
        every { property.name } returns PROPERTY_NAME

        StringPreferenceDelegate(
            sharedPreferences = mockk(),
            key = null
        ).apply {
            assertThat(preferenceKey(property)).isEqualTo(PROPERTY_NAME)
        }

        verify { property.name }
    }

    @Test
    fun `preferenceKey - key nonnull - key`() {
        StringPreferenceDelegate(
            sharedPreferences = mockk(),
            key = KEY
        ).apply {
            assertThat(preferenceKey(mockk())).isEqualTo(KEY)
        }
    }

    @Test
    fun `getValue - sharedPreferences returns null, default not specified - empty string`() {
        every { sharedPreferences.getString(KEY, null) } returns null

        StringPreferenceDelegate(
            sharedPreferences = sharedPreferences,
            key = KEY
        ).apply {
            assertThat(getValue(mockk(), property)).isEmpty()
        }

        verify { sharedPreferences.getString(KEY, null) }
    }

    @Test
    fun `getValue - sharedPreferences returns null, default not empty - default`() {
        every { sharedPreferences.getString(KEY, null) } returns null

        StringPreferenceDelegate(
            sharedPreferences = sharedPreferences,
            key = KEY,
            default = DEFAULT
        ).apply {
            assertThat(getValue(mockk(), property)).isEqualTo(DEFAULT)
        }

        verify { sharedPreferences.getString(KEY, null) }
    }

    @Test
    fun `getValue - sharedPreferences returns value - value`() {
        every { sharedPreferences.getString(KEY, null) } returns VALUE

        StringPreferenceDelegate(
            sharedPreferences = sharedPreferences,
            key = KEY
        ).apply {
            assertThat(getValue(mockk(), property)).isEqualTo(VALUE)
        }

        verify { sharedPreferences.getString(KEY, null) }
    }

    @Test
    fun `setValue - store sharedPreferences value`() {
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(KEY, VALUE) } returns editor
        every { editor.apply() } just Runs

        StringPreferenceDelegate(
            sharedPreferences = sharedPreferences,
            key = KEY
        ).setValue(mockk(), property, VALUE)

        verify {
            sharedPreferences.edit()
            editor.putString(KEY, VALUE)
            editor.apply()
        }
    }
}