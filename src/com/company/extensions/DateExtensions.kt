package com.company.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.formatToTimeDefault(): String{
    val sdf= SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(this)
}
