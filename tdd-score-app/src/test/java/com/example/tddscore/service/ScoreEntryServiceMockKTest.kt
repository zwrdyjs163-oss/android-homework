package com.example.tddscore.service

import com.example.tddscore.data.ScoreRepository
import com.example.tddscore.domain.GradeLevel
import com.example.tddscore.domain.ScoreCalculator
import com.example.tddscore.domain.ScoreInput
import com.example.tddscore.domain.ScoreRecord
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class ScoreEntryServiceMockKTest {
    @Test
    fun submitSavesCalculatedRecordToRepositoryDependency() {
        val repository = mockk<ScoreRepository>(relaxed = true)
        val service = ScoreEntryService(ScoreCalculator(), repository, nextId = { 20260608L })

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

        val savedRecord = slot<ScoreRecord>()
        verify(exactly = 1) { repository.save(capture(savedRecord)) }
        assertEquals(record, savedRecord.captured)
    }

    @Test
    fun historyReadsFromRepositoryDependency() {
        val repository = mockk<ScoreRepository>()
        val expected =
            listOf(
                ScoreRecord(
                    id = 1L,
                    studentName = "李四",
                    objectiveScore = 32,
                    blankScore = 24,
                    essayScore = 20,
                    totalScore = 76,
                    gradeLevel = GradeLevel.GOOD,
                )
            )
        every { repository.findAll() } returns expected

        val service = ScoreEntryService(ScoreCalculator(), repository)

        assertEquals(expected, service.history())
        verify(exactly = 1) { repository.findAll() }
    }
}
