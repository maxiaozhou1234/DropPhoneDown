package save.my.ass

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews

/**
 * Created by mxz on 2021/7/20.
 */
class KidWidgetProvider : AppWidgetProvider() {

    override fun onEnabled(context: Context?) {
        Log.d("zhou", "enable")
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        Log.d("zhou", "deleted")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d("zhou","onReceive")
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("zhou", "KidWidgetProvider on update")
        appWidgetIds.forEach { id ->
            val pendingIntent = Intent(context, MainActivity::class.java)
                .let {
                    PendingIntent.getActivity(context, 0, it, 0)
                }
            val views = RemoteViews(context.packageName, R.layout.layout_app_widget_provider)
                .apply {
                    setOnClickPendingIntent(R.id.layout, pendingIntent)

                    val rule = Rule.load(context)
                    setTextViewText(R.id.tvStartTime, rule.getStartTime())
                    setTextViewText(R.id.tvEndTime, rule.getEndTime())

                    setViewVisibility(R.id.ivEnable,if(rule.switchEnable())View.VISIBLE else View.GONE)
                }
            appWidgetManager.updateAppWidget(id, views)
        }

        context.sendBroadcast(Intent(ACTION_TIME_TICK))
    }

    //尺寸发生变化
    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        Log.d("zhou", "onAppWidgetOptionsChanged")
    }
}