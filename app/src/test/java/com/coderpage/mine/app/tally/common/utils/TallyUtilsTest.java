package com.coderpage.mine.app.tally.common.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * TallyUtils 的纯 JUnit 测试。
 * 测试 formatDisplayMoney 方法，不依赖 Android 框架。
 */
public class TallyUtilsTest {

    @Test
    public void formatDisplayMoney_positiveAmount() {
        assertEquals("12.50", TallyUtils.formatDisplayMoney(12.5));
    }

    @Test
    public void formatDisplayMoney_zeroAmount() {
        assertEquals("0.00", TallyUtils.formatDisplayMoney(0));
    }

    @Test
    public void formatDisplayMoney_negativeAmount() {
        assertEquals("-5.30", TallyUtils.formatDisplayMoney(-5.3));
    }

    @Test
    public void formatDisplayMoney_largeAmount() {
        assertEquals("999999.99", TallyUtils.formatDisplayMoney(999999.99));
    }

    @Test
    public void formatDisplayMoney_verySmallAmount() {
        assertEquals("0.01", TallyUtils.formatDisplayMoney(0.01));
    }

    @Test
    public void formatDisplayMoney_roundsHalfUp() {
        // DecimalFormat 默认 HALF_EVEN，验证实际行为
        assertEquals("1.23", TallyUtils.formatDisplayMoney(1.234));
        assertEquals("1.24", TallyUtils.formatDisplayMoney(1.235));
    }

    @Test
    public void formatDisplayMoney_truncatesExtraDecimals() {
        assertEquals("3.14", TallyUtils.formatDisplayMoney(3.14159));
    }

    @Test
    public void formatDisplayMoney_integerValue() {
        assertEquals("100.00", TallyUtils.formatDisplayMoney(100));
    }
}
