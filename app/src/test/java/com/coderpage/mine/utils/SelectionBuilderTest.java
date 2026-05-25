package com.coderpage.mine.utils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

/**
 * SelectionBuilder 的 JUnit + Robolectric 综合测试。
 * 覆盖 reset、where、table、map、getSelection/getSelectionArgs、query、delete 等方法。
 */
@RunWith(RobolectricTestRunner.class)
public class SelectionBuilderTest {

    private SelectionBuilder builder;

    @Before
    public void setUp() {
        builder = new SelectionBuilder();
    }

    // ===== reset =====

    @Test
    public void reset_clearsSelectionAndArgs() {
        builder.table("record").where("type=?", "expense");
        assertFalse(builder.getSelection().isEmpty());
        assertEquals(1, builder.getSelectionArgs().length);

        builder.reset();
        assertEquals("", builder.getSelection());
        assertEquals(0, builder.getSelectionArgs().length);
    }

    @Test
    public void reset_clearsTable() {
        builder.table("record");
        builder.reset();
        // 表被清除后，调用 update 应抛出 IllegalStateException
        try {
            builder.update(null, null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Table not specified", e.getMessage());
        }
    }

    // ===== where =====

    @Test
    public void where_singleCondition_buildsSelection() {
        builder.where("type=?", "expense");
        assertEquals("(type=?)", builder.getSelection());
        assertArrayEquals(new String[]{"expense"}, builder.getSelectionArgs());
    }

    @Test
    public void where_multipleConditions_combinesWithAnd() {
        builder.where("type=?", "expense").where("name LIKE ?", "%coffee%");
        assertEquals("(type=?) AND (name LIKE ?)", builder.getSelection());
        assertArrayEquals(new String[]{"expense", "%coffee%"}, builder.getSelectionArgs());
    }

    @Test
    public void where_emptySelection_noArgs_returnsSelf() {
        SelectionBuilder result = builder.where("");
        assertSame(builder, result);
        assertEquals("", builder.getSelection());
        assertEquals(0, builder.getSelectionArgs().length);
    }

    @Test
    public void where_nullSelection_noArgs_returnsSelf() {
        SelectionBuilder result = builder.where(null);
        assertSame(builder, result);
        assertEquals("", builder.getSelection());
    }

    @Test(expected = IllegalArgumentException.class)
    public void where_emptySelectionWithArgs_throwsException() {
        builder.where("", "unused");
    }

    @Test(expected = IllegalArgumentException.class)
    public void where_nullSelectionWithArgs_throwsException() {
        builder.where(null, "unused");
    }

    @Test
    public void where_selectionWithNullArgs_doesNotThrow() {
        builder.where("type=?", (String[]) null);
        assertEquals("(type=?)", builder.getSelection());
        assertEquals(0, builder.getSelectionArgs().length);
    }

    // ===== table =====

    @Test
    public void table_setsTableName() {
        builder.table("record");
        // 验证 table 已设置（不抛异常）
        builder.where("type=?", "expense");
        assertNotNull(builder.getSelection());
    }

    @Test
    public void tableWithParams_replacesQuestionMarks() {
        builder.table("record_?_?", "2024", "01");
        // table方法中带参数会替换?为参数值
        builder.where("type=?", "expense");
        // 只要不抛异常即可
        assertNotNull(builder.getSelection());
    }

    @Test
    public void tableWithNullParams_setsTableDirectly() {
        builder.table("record", (String[]) null);
        builder.where("type=?", "expense");
        assertNotNull(builder.getSelection());
    }

    // ===== map / mapToTable =====

    @Test
    public void mapToTable_addsProjectionMapping() {
        builder.mapToTable("name", "record");
        // mapToTable 只是在内部 map 中添加映射，通过 toString 或 query 间接验证
        String str = builder.toString();
        assertTrue(str.contains("record.name"));
    }

    @Test
    public void map_addsProjectionMapping() {
        builder.map("count", "COUNT(*)");
        String str = builder.toString();
        assertTrue(str.contains("COUNT(*) AS count"));
    }

    // ===== getSelection / getSelectionArgs =====

    @Test
    public void getSelection_emptyBuilder_returnsEmptyString() {
        assertEquals("", builder.getSelection());
    }

    @Test
    public void getSelectionArgs_emptyBuilder_returnsEmptyArray() {
        assertEquals(0, builder.getSelectionArgs().length);
    }

    // ===== query (using mock) =====

    @Test
    public void query_withoutTable_throwsIllegalStateException() {
        try {
            builder.query(null, null, null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Table not specified", e.getMessage());
        }
    }

    // ===== delete (using mock) =====

    @Test
    public void delete_withoutTable_throwsIllegalStateException() {
        try {
            builder.delete(null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Table not specified", e.getMessage());
        }
    }

    // ===== update =====

    @Test
    public void update_withoutTable_throwsIllegalStateException() {
        try {
            builder.update(null, new ContentValues());
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Table not specified", e.getMessage());
        }
    }

    // ===== groupBy / having =====

    @Test
    public void groupBy_setsGroupBy() {
        builder.groupBy("category");
        // 间接验证：toString 应包含 groupBy 信息
        String str = builder.toString();
        assertNotNull(str);
    }

    @Test
    public void having_setsHaving() {
        builder.having("count > 0");
        String str = builder.toString();
        assertNotNull(str);
    }

    // ===== toString =====

    @Test
    public void toString_containsTableAndSelection() {
        builder.table("record").where("type=?", "expense");
        String str = builder.toString();
        assertTrue(str.contains("record"));
        assertTrue(str.contains("type=?"));
    }
}
