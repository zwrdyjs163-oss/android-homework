package com.example.tddscore.service

import com.example.tddscore.data.ScoreRepository
import com.example.tddscore.domain.GradeLevel
import com.example.tddscore.domain.ScoreCalculator
import com.example.tddscore.domain.ScoreInput
import com.example.tddscore.domain.ScoreRecord
import org.junit.Assert.assertEquals
import org.junit.Test

class ScoreEntryServiceTest {
    @Test
    fun submitCalculatesGradeAndSavesRecordToRepository() {
        val repository = RecordingScoreRepository()
        val service = ScoreEntryService(ScoreCalculator(), repository, nextId = { 1001L })

        val record =
            service.submit(
                ScoreInput(
                    studentName = " 王五 ",
                    objectiveScore = 38,
                    blankScore = 27,
                    essayScore = 25,
                )
            )

        assertEquals("王五", record.studentName)
        assertEquals(90, record.totalScore)
        assertEquals(GradeLevel.EXCELLENT, record.gradeLevel)
        assertEquals(listOf(record), repository.savedRecords)
    }

    @Test
    fun historyReturnsRepositoryRecords() {
        val repository = RecordingScoreRepository()
        val service = ScoreEntryService(ScoreCalculator(), repository, nextId = { 7L })
        val record =
            service.submit(
                ScoreInput(
                    studentName = "赵六",
                    objectiveScore = 30,
                    blankScore = 20,
                    essayScore = 10,
                )
            )

        assertEquals(listOf(record), service.history())
    }

    private class RecordingScoreRepository : ScoreRepository {
        val savedRecords = mutableListOf<ScoreRecord>()

        override fun save(record: ScoreRecord) {
            savedRecords += record
        }

        override fun findAll(): List<ScoreRecord> = savedRecords.toList()
    }
}
