package com.example.tddscore

import android.app.Activity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.example.tddscore.data.InMemoryScoreRepository
import com.example.tddscore.domain.ScoreCalculator
import com.example.tddscore.domain.ScoreInput
import com.example.tddscore.domain.ScoreRecord
import com.example.tddscore.service.ScoreEntryService

class MainActivity : Activity() {
    private val service =
        ScoreEntryService(
            calculator = ScoreCalculator(),
            repository = InMemoryScoreRepository(),
        )

    private lateinit var studentNameInput: EditText
    private lateinit var objectiveScoreInput: EditText
    private lateinit var blankScoreInput: EditText
    private lateinit var essayScoreInput: EditText
    private lateinit var resultText: TextView
    private lateinit var historyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(buildContent())
    }

    private fun buildContent(): View {
        val container =
            LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(32, 32, 32, 32)
            }

        container.addView(
            TextView(this).apply {
                text = "试卷成绩电子化"
                textSize = 24f
            }
        )

        studentNameInput = textInput("学生姓名", R.id.studentNameInput, InputType.TYPE_CLASS_TEXT)
        objectiveScoreInput = textInput("选择题分数(0-40)", R.id.objectiveScoreInput)
        blankScoreInput = textInput("填空题分数(0-30)", R.id.blankScoreInput)
        essayScoreInput = textInput("简答题分数(0-30)", R.id.essayScoreInput)

        container.addView(studentNameInput)
        container.addView(objectiveScoreInput)
        container.addView(blankScoreInput)
        container.addView(essayScoreInput)

        container.addView(
            Button(this).apply {
                id = R.id.saveScoreButton
                text = "计算并保存"
                setOnClickListener { submitScore() }
            }
        )

        resultText =
            TextView(this).apply {
                id = R.id.scoreResultText
                text = "等待录入成绩"
                textSize = 18f
                setPadding(0, 24, 0, 12)
            }
        historyText =
            TextView(this).apply {
                id = R.id.scoreHistoryText
                text = "暂无成绩记录"
                textSize = 16f
            }

        container.addView(resultText)
        container.addView(historyText)

        return ScrollView(this).apply { addView(container) }
    }

    private fun textInput(
        hintText: String,
        viewId: Int,
        inputTypeValue: Int = InputType.TYPE_CLASS_NUMBER,
    ): EditText =
        EditText(this).apply {
            id = viewId
            hint = hintText
            inputType = inputTypeValue
            setSingleLine(true)
        }

    private fun submitScore() {
        try {
            val record =
                service.submit(
                    ScoreInput(
                        studentName = studentNameInput.text.toString(),
                        objectiveScore = objectiveScoreInput.asInt(),
                        blankScore = blankScoreInput.asInt(),
                        essayScore = essayScoreInput.asInt(),
                    )
                )
            resultText.text = record.toResultText()
            historyText.text = service.history().joinToString("\n") { it.toHistoryLine() }
        } catch (error: RuntimeException) {
            resultText.text = "输入有误：${error.message}"
        }
    }

    private fun EditText.asInt(): Int = text.toString().trim().toIntOrNull() ?: -1

    private fun ScoreRecord.toResultText(): String =
        "${studentName} 总分 ${totalScore}，等级 ${gradeLevel.label}"

    private fun ScoreRecord.toHistoryLine(): String =
        "${studentName}: ${objectiveScore}+${blankScore}+${essayScore}=${totalScore} ${gradeLevel.label}"
}
