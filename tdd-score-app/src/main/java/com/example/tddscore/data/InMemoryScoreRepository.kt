package com.example.tddscore.data

import com.example.tddscore.domain.ScoreRecord

class InMemoryScoreRepository : ScoreRepository {
    private val records = mutableListOf<ScoreRecord>()

    override fun save(record: ScoreRecord) {
        records += record
    }

    override fun findAll(): List<ScoreRecord> = records.toList()
}
