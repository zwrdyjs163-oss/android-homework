package com.coderpage.mine.app.tally.utils;

import android.util.Pair;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * DateUtils 的 Robolectric Android 单元测试。
 * 测试 monthDateRange、dayDateRange、yearDateRange 等方法。
 * 使用 Robolectric 提供 android.util.Pair 等 Android API 支持。
 */
@RunWith(RobolectricTestRunner.class)
public class DateUtilsRobolectricTest {

    // ===== monthDateRange =====

    @Test
    public void monthDateRange_january2024_returnsCorrectRange() {
        Pair<Long, Long> range = DateUtils.monthDateRange(2024, 1);

        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(range.first);
        assertEquals(2024, start.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, start.get(Calendar.MONTH));
        assertEquals(1, start.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, start.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, start.get(Calendar.MINUTE));
        assertEquals(0, start.get(Calendar.SECOND));

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(range.second);
        assertEquals(2024, end.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, end.get(Calendar.MONTH));
        assertEquals(31, end.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, end.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, end.get(Calendar.MINUTE));
        assertEquals(59, end.get(Calendar.SECOND));
    }

    @Test
    public void monthDateRange_february2024_leapYear_has29Days() {
        Pair<Long, Long> range = DateUtils.monthDateRange(2024, 2);

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(range.second);
        assertEquals(29, end.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void monthDateRange_february2023_nonLeapYear_has28Days() {
        Pair<Long, Long> range = DateUtils.monthDateRange(2023, 2);

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(range.second);
        assertEquals(28, end.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void monthDateRange_december_endsAt31st() {
        Pair<Long, Long> range = DateUtils.monthDateRange(2024, 12);

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(range.second);
        assertEquals(31, end.get(Calendar.DAY_OF_MONTH));
    }

    // ===== dayDateRange =====

    @Test
    public void dayDateRange_returnsCorrectStartAndEnd() {
        Pair<Long, Long> range = DateUtils.dayDateRange(2024, 3, 15);

        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(range.first);
        assertEquals(2024, start.get(Calendar.YEAR));
        assertEquals(Calendar.MARCH, start.get(Calendar.MONTH));
        assertEquals(15, start.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, start.get(Calendar.HOUR_OF_DAY));

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(range.second);
        assertEquals(15, end.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, end.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, end.get(Calendar.MINUTE));
        assertEquals(59, end.get(Calendar.SECOND));
    }

    @Test
    public void dayDateRange_durationIsOneDay() {
        Pair<Long, Long> range = DateUtils.dayDateRange(2024, 6, 10);
        long duration = range.second - range.first;
        // 一天 = 24*60*60*1000 - 1ms（因为 start 是 00:00:00.000，end 是 23:59:59.999）
        assertEquals(24 * 60 * 60 * 1000 - 1, duration);
    }

    // ===== yearDateRange =====

    @Test
    public void yearDateRange_2024_returnsJan1ToDec31() {
        Pair<Long, Long> range = DateUtils.yearDateRange(2024);

        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(range.first);
        assertEquals(2024, start.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, start.get(Calendar.MONTH));
        assertEquals(1, start.get(Calendar.DAY_OF_MONTH));

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(range.second);
        assertEquals(2024, end.get(Calendar.YEAR));
        assertEquals(Calendar.DECEMBER, end.get(Calendar.MONTH));
        assertEquals(31, end.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void yearDateRange_startBeforeEnd() {
        Pair<Long, Long> range = DateUtils.yearDateRange(2024);
        assertTrue(range.first < range.second);
    }

    // ===== DAY_MILLISECONDS constant =====

    @Test
    public void dayMilliseconds_constantValue() {
        assertEquals(24 * 60 * 60 * 1000, DateUtils.DAY_MILLISECONDS);
    }
}
