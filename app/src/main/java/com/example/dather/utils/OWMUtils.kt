package com.example.dather.utils

const val OWM_ICON = "http://openweathermap.org/img/wn"

class OWMUtils {
    companion object {
        fun getIconFromDescription(icon: String, x: Int = 2): String {
            return "${OWM_ICON}/${icon}@${x}x.png"
        }
    }
}