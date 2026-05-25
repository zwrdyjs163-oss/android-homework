package com.coderpage.mine.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.coderpage.mine.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class PreferencesUtilsRobolectricTest {

    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
    }

    @Test
    public void getClientChangeableUuid_generatesUuidOnceAndReusesIt() {
        String firstUuid = PreferencesUtils.getClientChangeableUuid(context);
        String secondUuid = PreferencesUtils.getClientChangeableUuid(context);

        assertFalse(firstUuid.isEmpty());
        assertEquals(UUID.fromString(firstUuid).toString(), firstUuid);
        assertEquals(firstUuid, secondUuid);
    }

    @Test
    public void searchHistory_roundTripsThroughSharedPreferences() {
        List<String> history = Arrays.asList("coffee", "taxi", "book");

        PreferencesUtils.setSearchHistory(context, history);

        assertEquals(history, PreferencesUtils.getSearchHistory(context));
    }
}
