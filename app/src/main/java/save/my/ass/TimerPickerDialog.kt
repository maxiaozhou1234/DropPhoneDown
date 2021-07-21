package save.my.ass

import android.app.Dialog
import android.content.Context
import android.support.annotation.StyleRes
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.layout_timer_picker_dialog.*

/**
 * Created by mxz on 2021/7/19.
 */
class TimerPickerDialog @JvmOverloads constructor(context: Context, @StyleRes themeResId: Int = 0) :
    Dialog(context, themeResId) {

    var okListener: ((hour: Int, minute: Int) -> Unit)? = null

    init {
        val content =
            LayoutInflater.from(context).inflate(R.layout.layout_timer_picker_dialog, null)
        setContentView(content)

        btnConfirm.setOnClickListener {
            val hour = picker.currentHour
            val minute = picker.currentMinute
            okListener?.invoke(hour, minute)
            dismiss()
        }

        btnCancel.setOnClickListener { dismiss() }
        picker.setIs24HourView(true)

        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    fun setCurrentTime(hour: Int, minute: Int) {
        picker.currentHour = hour
        picker.currentMinute = minute
    }
}