package com.spatmore.sharedpreferences

import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.junit.Test
import kotlin.reflect.KProperty

private const val PROPERTY_NAME: String = "property name"
private const val KEY: String = "preference-key"
private const val VALUE = 42L
private const val DEFAULT = -1L

class LongPreferenceDelegateTest {

    private val sharedPreferences: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk()
    private val property: KProperty<*> = mockk()

    @Test
    fun `preferenceKey - key null - property name`() {
        every { property.name } returns PROPERTY_NAME

        LongPreferenceDelegate(
            sharedPreferences = mockk(),
            key = null
        ).apply {
            assertThat(preferenceKey(property))
                .isEqualTo(PROPERTY_NAME)
        }

        verify { property.name }
    }

    @Test
    fun `preferenceKey - key nonnull - key`() {
        LongPreferenceDelegate(
            sharedPreferences = mockk(),
            key = KEY
        ).apply {
            assertThat(
                preferenceKey(
                    mockk()
                )
            ).isEqualTo(KEY)
        }
    }

    @Test
    fun `getValue - sharedPreferences returns default - default`() {
        every { sharedPreferences.getLong(KEY, DEFAULT) } returns DEFAULT

        LongPreferenceDelegate(
            sharedPreferences = sharedPreferences,
            key = KEY,
            default = DEFAULT
        ).apply {
            assertThat(getValue(mockk(), property)).isEqualTo(DEFAULT)
        }

        verify {
            sharedPreferences.getLong(KEY, DEFAULT)
        }
    }

    @Test
    fun `getValue - sharedPreferences returns value - value`() {
        every { sharedPreferences.getLong(KEY, 0L) } returns VALUE

        LongPreferenceDelegate(
            sharedPreferences = sharedPreferences,
            key = KEY
        ).apply {
            assertThat(getValue(mockk(), property)).isEqualTo(VALUE)
        }

        verify {
            sharedPreferences.getLong(KEY, 0L)
        }
    }

    @Test
    fun `setValue - store sharedPreferences value`() {
        every { sharedPreferences.edit() } returns editor
        every { editor.putLong(KEY, VALUE) } returns editor
        every { editor.apply() } just Runs

        LongPreferenceDelegate(
            sharedPreferences = sharedPreferences,
            key = KEY
        ).setValue(mockk(), property, VALUE)

        verify {
            sharedPreferences.edit()
            editor.putLong(KEY, VALUE)
            editor.apply()
        }
    }
}