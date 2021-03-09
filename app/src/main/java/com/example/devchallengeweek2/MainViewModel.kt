
package com.example.devchallengeweek2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devchallengeweek2.utils.GreenProgress
import com.example.devchallengeweek2.utils.RedProgress
import com.example.devchallengeweek2.utils.YellowProgress
import com.example.devchallengeweek2.utils.toFormattedTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var _countDownTimer: PreciseCountdownTimer? = null

    private val _time = MutableLiveData((0L).toFormattedTime())
    val time: LiveData<String> = _time

    private val _progress = MutableLiveData(0.00F)
    val progress: LiveData<Float> = _progress

    private val _isRunning = MutableLiveData(false)
    val isRunning: LiveData<Boolean> = _isRunning

    private val _lastTenSeconds = MutableLiveData(false)
    val lastTenSeconds: LiveData<Boolean> = _lastTenSeconds

    private val _progressColor = MutableLiveData(GreenProgress)
    val progressColor: LiveData<Long> = _progressColor
    val STR="Sometimes the secret to getting more done is to take time off."

    fun startTimer(timeInMs: Long = 80 * 1000L) {
        _isRunning.value = true
        _progressColor.value = GreenProgress
        _progress.value = 1f
        _time.value = (timeInMs).toFormattedTime()

        _countDownTimer = object : PreciseCountdownTimer(timeInMs, 1000) {
            override fun onTick(timeLeft: Long) {
                _progressColor.postValue(YellowProgress)
                _progress.postValue(timeLeft.toFloat() / timeInMs)
                _time.postValue(timeLeft.toFormattedTime())

                if (timeLeft <= 10_000) {
                    _lastTenSeconds.postValue(true)
                }
            }

            override fun onFinished() {
                _isRunning.postValue(false)
                _progress.postValue(0f)
                _time.postValue((0L).toFormattedTime())
            }
        }

        viewModelScope.launch {
            delay(500)
            _countDownTimer?.start()
        }
    }

    fun cancelTimer() {
        _countDownTimer?.cancel()

        _progressColor.value = RedProgress
        _isRunning.value = false
        _progress.value = 0f
        _time.value = (0L).toFormattedTime()
    }

}