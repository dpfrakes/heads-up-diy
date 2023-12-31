package com.dpfrakes.headsupdiy

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dpfrakes.headsupdiy.AppData.cluesChallenged
import com.dpfrakes.headsupdiy.AppData.numCorrect
import com.dpfrakes.headsupdiy.AppData.numPassed

class GameResultsActivity : AppCompatActivity() {
    private lateinit var cluesListScrollView: ScrollView
    private lateinit var textViewNumCorrect: TextView
    private lateinit var textViewNumPassed: TextView
    private lateinit var cluesListTextView: TextView
    private lateinit var goToMenuButton: Button
    private lateinit var playAgainButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_results)

        textViewNumCorrect = findViewById(R.id.textViewNumCorrect)
        textViewNumPassed = findViewById(R.id.textViewNumPassed)
        goToMenuButton = findViewById(R.id.goToMenuButton)
        playAgainButton = findViewById(R.id.playAgainButton)
        cluesListTextView = findViewById(R.id.cluesListTextView)
        cluesListScrollView = findViewById(R.id.cluesListScrollView)

        textViewNumCorrect.text = "Correct: $numCorrect"
        textViewNumPassed.text = "Skipped: $numPassed"

        cluesListScrollView.scrollTo(0, 0)

        val allCluesText = buildString {
            for (clue in cluesChallenged) {
                append("<font color='${clue.color}'>${clue.text}</font><br/><br/>")
            }
        }

        cluesListTextView.text = Html.fromHtml(allCluesText, Html.FROM_HTML_MODE_LEGACY)

        setButtonClickListeners()
    }

    private fun setButtonClickListeners() {
        goToMenuButton.setOnClickListener {
            val intent = Intent(this, GameMenuActivity::class.java)
            startActivity(intent)
        }

        playAgainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
