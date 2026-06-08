package com.example.tddscore.domain

class ScoreCalculator {
    fun calculate(input: ScoreInput): Int {
        validateScore("objectiveScore", input.objectiveScore, max = 40)
        validateScore("blankScore", input.blankScore, max = 30)
        validateScore("essayScore", input.essayScore, max = 30)
        return input.objectiveScore + input.blankScore + input.essayScore
    }

    fun levelOf(totalScore: Int): GradeLevel {
        require(totalScore in 0..100) { "totalScore must be in 0..100" }
        return when {
            totalScore >= 90 -> GradeLevel.EXCELLENT
            totalScore >= 75 -> GradeLevel.GOOD
            totalScore >= 60 -> GradeLevel.PASS
            else -> GradeLevel.FAIL
        }
    }

    private fun validateScore(name: String, value: Int, max: Int) {
        require(value in 0..max) { "$name must be in 0..$max" }
    }
}
