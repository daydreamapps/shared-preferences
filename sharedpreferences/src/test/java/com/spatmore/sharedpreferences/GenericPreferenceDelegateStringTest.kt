package com.spatmore.sharedpreferences

import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.junit.Test

private const val KEY: String = "key"
private const val VALUE: String = "value"
private const val DEFAULT_STRING: String = "Default"

class GenericPreferenceDelegateStringTest {

    private val sharedPreferences: SharedPreferences = mockk()
    private var delegate: String by GenericPreferenceDelegate.build(
        sharedPreferences = sharedPreferences,
        key = KEY,
        default = DEFAULT_STRING
    )

    @Test
    fun `getValue - type String - getString`() {
        every { sharedPreferences.getString(KEY, DEFAULT_STRING) } returns VALUE

        assertThat(delegate).isEqualTo(VALUE)

        verify { sharedPreferences.getString(KEY, DEFAULT_STRING) }
    }

    @Test
    fun `setValue - type String - setString`() {
        val editor: SharedPreferences.Editor = mockk()
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(KEY, VALUE) } returns editor
        every { editor.apply() } just runs

        delegate = VALUE

        verify {
            sharedPreferences.edit()
            editor.putString(KEY, VALUE)
            editor.apply()
        }
    }
}