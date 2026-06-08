package com.example.tddscore.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.tddscore.domain.GradeLevel
import com.example.tddscore.domain.ScoreRecord
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ScorePreferencesStoreRobolectricTest {
    @Test
    fun saveAndLoadRecordsWithAndroidSharedPreferences() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.getSharedPreferences("score-records", Context.MODE_PRIVATE).edit().clear().commit()
        val store = ScorePreferencesStore(context)
        val records =
            listOf(
                ScoreRecord(
                    id = 1L,
                    studentName = "赵六",
                    objectiveScore = 36,
                    blankScore = 26,
                    essayScore = 24,
                    totalScore = 86,
                    gradeLevel = GradeLevel.GOOD,
                )
            )

        store.save(records)

        assertEquals(records, store.load())
    }
}
