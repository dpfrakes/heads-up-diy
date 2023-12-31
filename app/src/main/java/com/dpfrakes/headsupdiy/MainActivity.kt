package com.dpfrakes.headsupdiy

import ApiService
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dpfrakes.headsupdiy.AppData.DEBUG
import com.dpfrakes.headsupdiy.AppData.cluesChallenged
import com.dpfrakes.headsupdiy.AppData.isWaitingForRoundStart
import com.dpfrakes.headsupdiy.AppData.numCorrect
import com.dpfrakes.headsupdiy.AppData.numPassed
import com.dpfrakes.headsupdiy.AppData.selectedCategory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class Clue(val text: String, val status: ClueStatus, val color: Int)

enum class ClueStatus {
    PASSED,
    CORRECT,
    UNANSWERED
}

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    // UI components from activity_main
    private lateinit var mainLayout: RelativeLayout
    private lateinit var clueTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var phoneOnForeheadTextView: TextView
    private lateinit var countdownTextView: TextView

    private lateinit var debugTextView: TextView

    // Other variables specific to MainActivity
    private lateinit var gameClockTimer: CountDownTimer
    private lateinit var countdownTimer: CountDownTimer
    private lateinit var api: ApiService
    private lateinit var handler: Handler

    private var gameClockRunning = false
    private var preventGamePlay = false

    private var viewInitializedTimestamp = System.currentTimeMillis()
    private var gameStartGracePeriod = 2000L
    private var gameTimeLimit = 60000L
    private var lastAnswerTimestamp = 0L
    private val minAnswerDuration = 0L
    private var isBeingAnswered = true

    private var roundClues = mutableListOf<String>("")
    private var clueIndex = 0
    private var nextClue = "<clue>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayout = findViewById(R.id.mainLayout)
        clueTextView = findViewById(R.id.clueTextView)
        progressBar = findViewById(R.id.progressBar)
        phoneOnForeheadTextView = findViewById(R.id.phoneOnForeheadTextView)
        countdownTextView = findViewById(R.id.countdownTextView)

        debugTextView = findViewById(R.id.debugTextView)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!

        handler = Handler(Looper.getMainLooper())

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/dpfrakes/heads-up-diy/master/api/categories/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        // Create the API service
        api = retrofit.create(ApiService::class.java)

        // Get Ready
        setupTimers()
        getReadyForRound()
    }

    private fun setupTimers() {
        gameClockTimer = object : CountDownTimer(gameTimeLimit, 100) {
            override fun onTick(millisUntilFinished: Long) {
                progressBar.progress = millisUntilFinished.toInt()
            }

            override fun onFinish() {
                endGame()
            }
        }

        countdownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilGameStarts: Long) {
                val secondsLeft = (millisUntilGameStarts / 1000).toInt() + 1
                countdownTextView.text = secondsLeft.toString()
            }

            override fun onFinish() {
                countdownTextView.visibility = View.GONE
                startGame()
            }
        }

        // Initialize one-time configurations
        progressBar.visibility = View.GONE
        progressBar.max = gameTimeLimit.toInt()
    }

    private fun getReadyForRound() {
        setupClues(selectedCategory)
        cluesChallenged.clear()
        numCorrect = 0
        numPassed = 0

        // Wait for accelerometer to trigger startCountdown
        isWaitingForRoundStart = true
    }

    private fun startCountdown() {
        phoneOnForeheadTextView.visibility = View.GONE
        countdownTimer.start()
        countdownTextView.visibility = View.VISIBLE
    }

    private fun startGame() {
        gameClockTimer.start()
        gameClockRunning = true
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        progressBar.progress = gameTimeLimit.toInt()
        progressBar.visibility = View.VISIBLE
        clueTextView.visibility = View.VISIBLE
    }

    private fun endGame() {
        gameClockRunning = false

        // Set UI to GameResults
        val intent = Intent(this, GameResultsActivity::class.java)
        startActivity(intent)
    }

    private fun stopGame() {
        preventGamePlay = true
        gameClockTimer.cancel()
        countdownTimer.cancel()
        mainLayout.setBackgroundColor(getColor(R.color.red))
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            updateAccelerometerValues(event.values[0], event.values[1], event.values[2])
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used, but required to implement the interface
    }

    private fun updateAccelerometerValues(x: Float, y: Float, z: Float) {
        if (DEBUG) debugTextView.text = "x: $x\ny: $y\nz: $z"
        val positionUp = z > 7
        val positionDown = z < -5

        if (preventGamePlay) return

        if (isWaitingForRoundStart) {
            val isBeingHeldHorizontal = -3 < y && y < 3
            val currentTime = System.currentTimeMillis()
            if (!positionUp && !positionDown && isBeingHeldHorizontal && currentTime - viewInitializedTimestamp >= gameStartGracePeriod) {
                isWaitingForRoundStart = false
                handler.postDelayed({
                    startCountdown()
                }, 500)
            }
            return
        }

        if (!gameClockRunning) {
            isBeingAnswered = false
            mainLayout.setBackgroundColor(getColor(R.color.defaultBackgroundColor))
            clueTextView.visibility = View.GONE
            return
        };

        // Check conditions for background color and text
        when {
            positionUp -> {
                if (!isBeingAnswered)
                    cluePass()
            }
            positionDown -> {
                if (!isBeingAnswered)
                    clueCorrect()
            }
            else -> {
                if (isBeingAnswered) {
                    handler.postDelayed({
                        isBeingAnswered = false
                        mainLayout.setBackgroundColor(getColor(R.color.defaultBackgroundColor))
                        clueTextView.text = nextClue
                    }, 250)
                }
            }
        }
    }

    private fun cluePass() {
        isBeingAnswered = true
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAnswerTimestamp >= minAnswerDuration) {
            mainLayout.setBackgroundColor(getColor(R.color.orange))
            cluesChallenged.add(Clue(clueTextView.text.toString(), ClueStatus.PASSED, Color.GRAY))
            clueTextView.text = "PASS"
            lastAnswerTimestamp = currentTime
            numPassed++

            // Stage next answer
            nextClue = roundClues[clueIndex++]
        } else {
            // Too soon, do not change anything
        }
    }

    private fun clueCorrect() {
        isBeingAnswered = true
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAnswerTimestamp >= minAnswerDuration) {
            mainLayout.setBackgroundColor(getColor(R.color.green))
            cluesChallenged.add(Clue(clueTextView.text.toString(), ClueStatus.CORRECT, Color.WHITE))
            clueTextView.text = "CORRECT!"
            lastAnswerTimestamp = currentTime
            numCorrect++

            // Stage next answer
            nextClue = roundClues[clueIndex++]
        } else {
            // Too soon, do not change anything
        }
    }

    private fun setupClues(category: String) {
        // Make an API request to get random texts
        val call = api.getAnswers(category)
        val context = this

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    // Check if the response body is not null
                    responseBody?.let {
                        // Split the text into an array of strings based on line breaks
                        val lines: List<String> = it.string().split("\n")
                        roundClues = lines.shuffled().toMutableList()
                        clueIndex = 0
                        nextClue = roundClues[clueIndex++]
                        clueTextView.text = nextClue
                    }
                } else {
                    stopGame()
                    ErrorDialog.showErrorDialog(context, "API failed to retrieve cards for $category")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle failure (e.g., network error)
                t.printStackTrace()
            }
        })
    }
}
