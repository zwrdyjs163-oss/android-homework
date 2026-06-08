package com.example.tddscore.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ScoreCalculatorTest {
    private val calculator = ScoreCalculator()

    @Test
    fun calculateReturnsTotalScoreFromThreeQuestionTypes() {
        val total =
            calculator.calculate(
                ScoreInput(
                    studentName = "张三",
                    objectiveScore = 36,
                    blankScore = 24,
                    essayScore = 20,
                )
            )

        assertEquals(80, total)
    }

    @Test
    fun calculateRejectsScoreOverQuestionTypeLimit() {
        assertThrows(IllegalArgumentException::class.java) {
            calculator.calculate(
                ScoreInput(
                    studentName = "李四",
                    objectiveScore = 41,
                    blankScore = 20,
                    essayScore = 20,
                )
            )
        }
    }

    @Test
    fun levelOfMapsScoreToGradeLevel() {
        assertEquals(GradeLevel.EXCELLENT, calculator.levelOf(95))
        assertEquals(GradeLevel.GOOD, calculator.levelOf(80))
        assertEquals(GradeLevel.PASS, calculator.levelOf(60))
        assertEquals(GradeLevel.FAIL, calculator.levelOf(59))
    }
}
