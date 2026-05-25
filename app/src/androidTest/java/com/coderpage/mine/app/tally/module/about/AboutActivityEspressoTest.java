package com.coderpage.mine.app.tally.module.about;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.coderpage.mine.BuildConfig;
import com.coderpage.mine.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicReference;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AboutActivityEspressoTest {

    @Test
    public void aboutScreenShowsAppVersionAndCheckUpdateEntry() {
        AboutActivity activity = launchAboutActivity();
        try {
            onView(withId(R.id.tvAppVersion))
                    .check(matches(isDisplayed()))
                    .check(matches(withText(containsString(BuildConfig.VERSION_NAME))));

            onView(withId(R.id.tvCheckNewVersion))
                    .check(matches(isDisplayed()));
        } finally {
            activity.finish();
        }
    }

    @Test
    public void clickWeChatInfoCopiesAccountAndShowsToast() {
        AboutActivity activity = launchAboutActivity();
        try {
            AtomicReference<View> decorView = new AtomicReference<>();
            AtomicReference<AboutActivity> activityRef = new AtomicReference<>();
            activityRef.set(activity);
            decorView.set(activity.getWindow().getDecorView());

            onView(withId(R.id.lyWeChatInfo))
                    .perform(scrollTo(), click());

            onView(withText(R.string.tally_about_wechat_copied))
                    .inRoot(withDecorView(not(is(decorView.get()))))
                    .check(matches(isDisplayed()));

            ClipboardManager clipboardManager = (ClipboardManager) activityRef.get()
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            assertNotNull(clipboardManager);
            assertTrue(clipboardManager.hasPrimaryClip());

            ClipData clipData = clipboardManager.getPrimaryClip();
            assertNotNull(clipData);
            assertEquals("MINE应用", clipData.getItemAt(0).coerceToText(activityRef.get()).toString());
        } finally {
            activity.finish();
        }
    }

    private AboutActivity launchAboutActivity() {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = new Intent(targetContext, AboutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        AboutActivity activity = (AboutActivity) InstrumentationRegistry.getInstrumentation()
                .startActivitySync(intent);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        return activity;
    }
}
