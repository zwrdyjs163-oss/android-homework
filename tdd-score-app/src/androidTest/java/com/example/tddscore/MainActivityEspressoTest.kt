package com.example.tddscore

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.containsString
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {
    @Test
    fun inputScoresClickButtonAndShowResult() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.studentNameInput)).perform(clearText(), replaceText("Zhang San"), closeSoftKeyboard())
            onView(withId(R.id.objectiveScoreInput)).perform(clearText(), replaceText("36"), closeSoftKeyboard())
            onView(withId(R.id.blankScoreInput)).perform(clearText(), replaceText("24"), closeSoftKeyboard())
            onView(withId(R.id.essayScoreInput)).perform(clearText(), replaceText("20"), closeSoftKeyboard())

            onView(withId(R.id.saveScoreButton)).perform(click())

            onView(withId(R.id.scoreResultText)).check(matches(withText(containsString("Zhang San 总分 80"))))
            onView(withId(R.id.scoreHistoryText)).check(matches(withText(containsString("Zhang San: 36+24+20=80"))))
        }
    }
}
