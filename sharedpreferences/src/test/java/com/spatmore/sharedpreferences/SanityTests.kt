package com.spatmore.sharedpreferences

import com.google.common.truth.Truth
import com.google.gson.Gson
import org.junit.Test

class SanityTests {

    @Test
    fun `gson sanity test`() {
        val gson = Gson()

        Truth.assertThat(gson.fromJson(gson.toJson(1), Int::class.java)).isEqualTo(1)
        Truth.assertThat(gson.fromJson(gson.toJson(1.1), Double::class.java)).isEqualTo(1.1)
        Truth.assertThat(gson.fromJson(gson.toJson(1.1F), Float::class.java)).isEqualTo(1.1F)
        Truth.assertThat(gson.fromJson(gson.toJson(1L), Long::class.java)).isEqualTo(1L)
        Truth.assertThat(gson.fromJson(gson.toJson(true), Boolean::class.java)).isEqualTo(true)
        Truth.assertThat(gson.fromJson(gson.toJson("1L"), String::class.java)).isEqualTo("1L")
    }
}