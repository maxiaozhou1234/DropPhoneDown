package save.my.ass

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val dayArray: Array<CheckBox> by lazy {
        arrayOf(cb0, cb1, cb2, cb3, cb4, cb5, cb6)
    }

    private val startTimePickerDialog: TimerPickerDialog by lazy {
        val dialog = TimerPickerDialog(this)
        dialog.okListener = { hour, minute ->

            if (checkTimeRange(hour, minute, map[END_TIME_H] as Int, map[END_TIME_M] as Int)) {

                btnStart.text = String.format("%02d:%02d", hour, minute)
                map[START_TIME_H] = hour
                map[START_TIME_M] = minute
            } else {
                Toast.makeText(this, "错误，开始时间大于结束时间", Toast.LENGTH_LONG).show()
            }
        }
        dialog
    }

    private val endTimerPickerDialog: TimerPickerDialog by lazy {
        val dialog = TimerPickerDialog(this)
        dialog.okListener = { hour, minute ->
            if (checkTimeRange(map[START_TIME_H] as Int, map[START_TIME_M] as Int, hour, minute)) {
                btnEnd.text = String.format("%02d:%02d", hour, minute)
                map[END_TIME_H] = hour
                map[END_TIME_M] = minute
            } else {
                Toast.makeText(this, "错误，开始时间大于结束时间", Toast.LENGTH_LONG).show()
            }
        }
        dialog
    }

    private val sp: SharedPreferences by lazy {
        getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    private val map = HashMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "上网限制器"

        map[SWITCH_ENABLE] = sp.getBoolean(SWITCH_ENABLE, false)
        map[CURRENT_PERIOD] = sp.getInt(CURRENT_PERIOD, EVERY_DAY)
        map[WEEK_VALUE] = sp.getInt(WEEK_VALUE, 0)
        map[START_TIME_H] = sp.getInt(START_TIME_H, 0)
        map[START_TIME_M] = sp.getInt(START_TIME_M, 0)
        map[END_TIME_H] = sp.getInt(END_TIME_H, 23)
        map[END_TIME_M] = sp.getInt(END_TIME_M, 59)

        switchButton.isChecked = map[SWITCH_ENABLE] as Boolean

        switchButton.setOnCheckedChangeListener { _, isChecked ->
            map[SWITCH_ENABLE] = isChecked
        }

        rgDaySetting.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbWeek) {
                layoutWeek.visibility = View.VISIBLE
                loadWeekDay()
            } else {
                map[CURRENT_PERIOD] = EVERY_DAY
                layoutWeek.visibility = View.GONE
            }
        }
        ((rgDaySetting.getChildAt(map[CURRENT_PERIOD] as Int)) as RadioButton).isChecked = true

        btnSave.setOnClickListener { saveWeekDay() }

        btnStart.setOnClickListener {
            val h = map[START_TIME_H] as Int
            val m = map[START_TIME_M] as Int
            startTimePickerDialog.setCurrentTime(h, m)
            startTimePickerDialog.show()
        }
        btnEnd.setOnClickListener {
            val h = map[END_TIME_H] as Int
            val m = map[END_TIME_M] as Int
            endTimerPickerDialog.setCurrentTime(h, m)
            endTimerPickerDialog.show()
        }

        btnStart.text =
            String.format("%02d:%02d", map[START_TIME_H] as Int, map[START_TIME_M] as Int)
        btnEnd.text = String.format("%02d:%02d", map[END_TIME_H] as Int, map[END_TIME_M] as Int)
    }

    private fun loadWeekDay() {
        val value = map[WEEK_VALUE] as Int
        dayArray.forEachIndexed { index, cb ->
            val other = 1.shl(6 - index)
            cb.isChecked = value.and(other) == other
        }
    }

    private fun saveWeekDay() {

        map[CURRENT_PERIOD] = CUSTOM_WEEK

        var value = 0
        dayArray.forEachIndexed { index, cb ->
            if (cb.isChecked) {
                value = value.or(1.shl(6 - index))
            }
        }
        map[WEEK_VALUE] = value
    }

    private fun checkTimeRange(sh: Int, sm: Int, eh: Int, em: Int): Boolean {
        if (sh > eh) return false
        if (sh < eh) return true
        if (sm < em) return true
        return false
    }

    private fun saveToFile() {
        val editor = sp.edit()!!
        map.forEach { entry ->
            val k = entry.key
            when (val v = entry.value) {
                is Int -> editor.putInt(k, v)
                is Boolean -> editor.putBoolean(k, v)
                is String -> editor.putString(k, v)
            }
        }
        editor.apply()
    }

    override fun onStop() {
        saveToFile()
        super.onStop()
        sendBroadcast(Intent(ACTION_UPDATE_CONFIG))
        sendBroadcast(Intent(ACTION_TIME_TICK))
    }
}
