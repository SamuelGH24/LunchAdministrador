package com.example.lunchadministrador.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Fechas {
    fun inicioDelDia(): Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    fun inicioDelMes(): Long = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

object Formato {
    fun pesos(valor: Int): String = "$" + "%,d".format(Locale.US, valor).replace(',', '.')

    fun hora(ms: Long?): String =
        if (ms == null) "--:--" else SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(ms))

    fun tiempoEnCompania(creadoEn: Long): String {
        val dias = ((System.currentTimeMillis() - creadoEn) / 86_400_000L).toInt()
        return when {
            dias <= 0 -> "Desde hoy"
            dias == 1 -> "1 día"
            dias < 30 -> "$dias días"
            else -> "${dias / 30} mes(es)"
        }
    }
}
