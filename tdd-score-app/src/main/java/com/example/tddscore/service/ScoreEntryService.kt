package com.example.tddscore.service

import com.example.tddscore.data.ScoreRepository
import com.example.tddscore.domain.ScoreCalculator
import com.example.tddscore.domain.ScoreInput
import com.example.tddscore.domain.ScoreRecord

class ScoreEntryService(
    private val calculator: ScoreCalculator,
    private val repository: ScoreRepository,
    private val nextId: () -> Long = System::currentTimeMillis,
) {
    fun submit(input: ScoreInput): ScoreRecord {
        require(input.studentName.isNotBlank()) { "studentName must not be blank" }
        val normalizedInput = input.copy(studentName = input.studentName.trim())
        val total = calculator.calculate(normalizedInput)
        val record =
            ScoreRecord(
                id = nextId(),
                studentName = normalizedInput.studentName,
                objectiveScore = normalizedInput.objectiveScore,
                blankScore = normalizedInput.blankScore,
                essayScore = normalizedInput.essayScore,
                totalScore = total,
                gradeLevel = calculator.levelOf(total),
            )
        repository.save(record)
        return record
    }

    fun history(): List<ScoreRecord> = repository.findAll()
}
