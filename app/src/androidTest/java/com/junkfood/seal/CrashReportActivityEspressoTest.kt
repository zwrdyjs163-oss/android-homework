package com.junkfood.seal

import android.content.Context
import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import org.junit.Rule
import org.junit.Test

class CrashReportActivityEspressoTest {

    @get:Rule val composeRule = createEmptyComposeRule()

    @Test
    fun crashReportPage_launchesAndShowsReportText() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val report = "Test crash report"
        val intent =
            Intent(context, CrashReportActivity::class.java)
                .putExtra("error_report", report)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        ActivityScenario.launch<CrashReportActivity>(intent).use {
            onView(isRoot()).check(matches(isDisplayed()))
            composeRule.onNodeWithText(context.getString(R.string.unknown_error_title)).assertIsDisplayed()
            composeRule.onNodeWithText(report).assertIsDisplayed()
        }
    }

    @Test
    fun crashReportPage_copyAndExitButtonCanBeClicked() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent =
            Intent(context, CrashReportActivity::class.java)
                .putExtra("error_report", "Button click report")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        ActivityScenario.launch<CrashReportActivity>(intent).use {
            onView(isRoot()).check(matches(isDisplayed()))
            composeRule.onNodeWithText(context.getString(R.string.copy_and_exit)).performClick()
        }
    }
}
