package com.coderpage.mine.utils

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SelectionBuilderMockKTest {

    @Test
    fun updatePassesBuiltSelectionToDatabase() {
        val db = mockk<SQLiteDatabase>()
        val values = mockk<ContentValues>()
        val expectedArgs = arrayOf("expense", "%coffee%")
        every {
            db.update(
                eq("record"),
                match { it === values },
                eq("(type=?) AND (name LIKE ?)"),
                match { it.contentEquals(expectedArgs) }
            )
        } returns 3

        val affectedRows = SelectionBuilder()
            .table("record")
            .where("type=?", "expense")
            .where("name LIKE ?", "%coffee%")
            .update(db, values)

        assertEquals(3, affectedRows)
        verify {
            db.update(
                eq("record"),
                match { it === values },
                eq("(type=?) AND (name LIKE ?)"),
                match { it.contentEquals(expectedArgs) }
            )
        }
        confirmVerified(db)
    }

    @Test(expected = IllegalArgumentException::class)
    fun whereRejectsArgumentsWithoutSelection() {
        SelectionBuilder().where("", "unused")
    }
}
