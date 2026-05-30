package com.junkfood.seal

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class, sdk = [28])
class RobolectricContextTest {

    @Test
    fun resources_areAvailableWithoutDevice() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        assertTrue(context.getString(R.string.app_name).isNotBlank())
        assertTrue(context.getString(R.string.copy_and_exit).isNotBlank())
    }

    @Test
    fun sharedPreferences_persistValuesInRobolectricContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val preferences = context.getSharedPreferences("robolectric_sample", Context.MODE_PRIVATE)

        preferences.edit().putString("download_url", "https://example.com/video").apply()

        assertEquals("https://example.com/video", preferences.getString("download_url", null))
    }
}
