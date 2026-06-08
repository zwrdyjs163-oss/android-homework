package com.example.tddscore.domain

data class ScoreRecord(
    val id: Long,
    val studentName: String,
    val objectiveScore: Int,
    val blankScore: Int,
    val essayScore: Int,
    val totalScore: Int,
    val gradeLevel: GradeLevel,
)
