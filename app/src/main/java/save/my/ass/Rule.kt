package save.my.ass

import android.content.Context
import java.util.*

/**
 * Created by mxz on 2021/7/19.
 */
class Rule(private val enable: Boolean, private val period: Int) {

    companion object {
        fun load(context: Context): Rule {

            val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

            val enable = sp.getBoolean(SWITCH_ENABLE, false)
            val period = sp.getInt(CURRENT_PERIOD, EVERY_DAY)
            val rule = Rule(enable, period)

            val sh = sp.getInt(START_TIME_H, -1)
            val sm = sp.getInt(START_TIME_M, -1)

            val eh = sp.getInt(END_TIME_H, -1)
            val em = sp.getInt(END_TIME_M, -1)

            rule.startTime = sh to sm
            rule.endTime = eh to em
            rule.weekValue = sp.getInt(WEEK_VALUE, 0)

            return rule
        }
    }

    private lateinit var startTime: Pair<Int, Int>
    private lateinit var endTime: Pair<Int, Int>
    private var weekValue = 0

    fun shouldIntercepted(): Boolean {
        if (!enable) return false
        if (startTime.first < 0 || endTime.first < 0) return false

        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val minute = Calendar.getInstance().get(Calendar.MINUTE)

        if ((hour >= startTime.first && minute >= startTime.second) && (hour <= endTime.first && minute <= endTime.second)) {
            return if (period == EVERY_DAY) {
                true
            } else {
                val week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
                val other = 1.shl(6 - week)
                weekValue.and(other) == other
            }
        }
        return false
    }

    fun getStartTime(): String = if (startTime.first < 0) "" else String.format(
        "%02d:%02d",
        startTime.first,
        startTime.second
    )

    fun getEndTime(): String =
        if (endTime.first < 0) "" else String.format("%02d:%02d", endTime.first, endTime.second)

    fun switchEnable() = enable
}