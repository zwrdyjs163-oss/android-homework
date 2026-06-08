package com.example.tddscore.data

import com.example.tddscore.domain.ScoreRecord

interface ScoreRepository {
    fun save(record: ScoreRecord)
    fun findAll(): List<ScoreRecord>
}
