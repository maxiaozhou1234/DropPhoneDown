package save.my.ass

/**
 * Created by mxz on 2021/7/19.
 */

const val SP_NAME = "config"

const val SWITCH_ENABLE = "switch_enable"//应用开关
const val CURRENT_PERIOD = "current_period" //当前设置周期，0 每天/ 1 自定义周几
const val START_TIME_H = "start_hour"//开始时间 hour
const val START_TIME_M = "start_minute"//开始时间 minute
const val END_TIME_H = "end_hour"//结束时间 hour
const val END_TIME_M = "end_minute"//结束时间 minute

const val EVERY_DAY = 0 //每一天
const val CUSTOM_WEEK = 1//自定义

const val WEEK_VALUE = "week_value"//自定义星期 0-6

const val ACTION_UPDATE_CONFIG = "action_update_config"//广播，更新配置
const val ACTION_TIME_TICK = "action_time_tick"//广播，半小时刷新一次