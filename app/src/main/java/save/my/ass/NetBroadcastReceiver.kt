package save.my.ass

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast

/**
 * Created by mxz on 2021/7/19.
 */
class NetBroadcastReceiver : BroadcastReceiver() {

    private val tag = "zhou"

    companion object {
        @JvmStatic
        private var rule: Rule? = null
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (ACTION_UPDATE_CONFIG == intent?.action) {
            Log.d(tag, "update")
            rule = Rule.load(context)

            val cn = ComponentName("save.my.ass", KidWidgetProvider::class.java.name)
            val view = RemoteViews("save.my.ass", R.layout.layout_app_widget_provider).apply {
                setTextViewText(R.id.tvStartTime, rule!!.getStartTime())
                setTextViewText(R.id.tvEndTime, rule!!.getEndTime())
                setViewVisibility(R.id.ivEnable,if(rule!!.switchEnable()) View.VISIBLE else View.GONE)
            }
            AppWidgetManager.getInstance(context).updateAppWidget(cn, view)

        } else if (WifiManager.WIFI_STATE_CHANGED_ACTION == intent?.action) {

            val state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1)
            if (WifiManager.WIFI_STATE_ENABLED == state) {
                if (rule == null) {
                    rule = Rule.load(context)
                }
                if (rule?.shouldIntercepted() == true) {
                    Log.d(tag, "<enable>need close wifi")
                    val wifiManager =
                        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    wifiManager.isWifiEnabled = false
                    Toast.makeText(context, "上网限制中，请放下手机", Toast.LENGTH_LONG).show()
                }
            }
        } else if (ACTION_TIME_TICK == intent?.action) {
            Log.d("zhou", "received time_tick,check rule")
            val wifiManager =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifiManager.isWifiEnabled) {
                if (rule == null) {
                    rule = Rule.load(context)
                }
                if (rule?.shouldIntercepted() == true) {

                    Log.d(tag, "<time>need close wifi")

                    wifiManager.isWifiEnabled = false
                    Toast.makeText(context, "上网开始限制，请放下手机", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}