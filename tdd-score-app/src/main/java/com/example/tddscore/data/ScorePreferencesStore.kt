package com.example.tddscore.data

import android.content.Context
import com.example.tddscore.domain.GradeLevel
import com.example.tddscore.domain.ScoreRecord

class ScorePreferencesStore(context: Context) {
    private val preferences = context.getSharedPreferences("score-records", Context.MODE_PRIVATE)

    fun save(records: List<ScoreRecord>) {
        val text = records.joinToString("\n") { record ->
            listOf(
                record.id,
                record.studentName.escape(),
                record.objectiveScore,
                record.blankScore,
                record.essayScore,
                record.totalScore,
                record.gradeLevel.name,
            ).joinToString("|")
        }
        preferences.edit().putString(KEY_RECORDS, text).apply()
    }

    fun load(): List<ScoreRecord> {
        val text = preferences.getString(KEY_RECORDS, "") ?: ""
        if (text.isBlank()) return emptyList()
        return text.lines().mapNotNull { line ->
            val parts = line.split("|")
            if (parts.size != 7) return@mapNotNull null
            ScoreRecord(
                id = parts[0].toLongOrNull() ?: return@mapNotNull null,
                studentName = parts[1].unescape(),
                objectiveScore = parts[2].toIntOrNull() ?: return@mapNotNull null,
                blankScore = parts[3].toIntOrNull() ?: return@mapNotNull null,
                essayScore = parts[4].toIntOrNull() ?: return@mapNotNull null,
                totalScore = parts[5].toIntOrNull() ?: return@mapNotNull null,
                gradeLevel = runCatching { GradeLevel.valueOf(parts[6]) }.getOrNull()
                    ?: return@mapNotNull null,
            )
        }
    }

    private fun String.escape(): String = replace("\\", "\\\\").replace("|", "\\p").replace("\n", "\\n")

    private fun String.unescape(): String =
        replace("\\n", "\n").replace("\\p", "|").replace("\\\\", "\\")

    private companion object {
        const val KEY_RECORDS = "records"
    }
}
