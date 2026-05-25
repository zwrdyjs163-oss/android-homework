package com.coderpage.mine.app.tally.module.chart.data;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CategoryDataTest {

    @Test
    public void getPercentRate_returnsZeroWhenTotalIsZero() {
        CategoryData data = new CategoryData();
        data.setAmount(20);
        data.setAmountTotal(0);

        assertEquals(0, data.getPercentRate(), 0.0001);
        assertEquals("0.00", data.getPercentRateString());
    }

    @Test
    public void getPercentRate_calculatesAmountShare() {
        CategoryData data = new CategoryData();
        data.setAmount(25);
        data.setAmountTotal(200);

        assertEquals(12.5, data.getPercentRate(), 0.0001);
        assertEquals("12.50", data.getPercentRateString());
    }
}
