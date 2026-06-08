package com.example.tddscore.data

import com.example.tddscore.domain.GradeLevel
import com.example.tddscore.domain.ScoreRecord
import org.junit.Assert.assertEquals
import org.junit.Test

class InMemoryScoreRepositoryTest {
    @Test
    fun saveKeepsScoreRecordsInInsertionOrder() {
        val repository = InMemoryScoreRepository()
        val first = record(id = 1, name = "张三")
        val second = record(id = 2, name = "李四")

        repository.save(first)
        repository.save(second)

        assertEquals(listOf(first, second), repository.findAll())
    }

    private fun record(id: Long, name: String): ScoreRecord =
        ScoreRecord(
            id = id,
            studentName = name,
            objectiveScore = 30,
            blankScore = 20,
            essayScore = 20,
            totalScore = 70,
            gradeLevel = GradeLevel.PASS,
        )
}
