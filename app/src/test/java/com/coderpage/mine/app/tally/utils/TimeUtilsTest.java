package com.coderpage.mine.app.tally.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeUtilsTest {

    @Test
    public void getDaysTotalOfMonth_returns31DaysForLongMonths() {
        assertEquals(31, TimeUtils.getDaysTotalOfMonth(2024, 1));
        assertEquals(31, TimeUtils.getDaysTotalOfMonth(2024, 8));
        assertEquals(31, TimeUtils.getDaysTotalOfMonth(2024, 12));
    }

    @Test
    public void getDaysTotalOfMonth_returns30DaysForShortMonths() {
        assertEquals(30, TimeUtils.getDaysTotalOfMonth(2024, 4));
        assertEquals(30, TimeUtils.getDaysTotalOfMonth(2024, 6));
        assertEquals(30, TimeUtils.getDaysTotalOfMonth(2024, 11));
    }

    @Test
    public void getDaysTotalOfMonth_handlesFebruaryLeapYear() {
        assertEquals(29, TimeUtils.getDaysTotalOfMonth(2024, 2));
        assertEquals(28, TimeUtils.getDaysTotalOfMonth(2023, 2));
    }
}
