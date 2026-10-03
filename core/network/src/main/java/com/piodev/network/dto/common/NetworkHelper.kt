package com.piodev.network.dto.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Clase de utilidad para verificar el estado de la conexión de red.
 * Se marca como @Singleton para que Hilt mantenga una única instancia en toda la app.
 */
@Singleton
class NetworkHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun hasInternet(): Boolean {
        // Obtenemos el servicio del sistema encargado de gestionar las conexiones
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // 1. Obtenemos la red activa actual. Si es null, el móvil está en modo avión o sin red.
        val network = connectivityManager.activeNetwork ?: return false

        // 2. Obtenemos las capacidades de esa red (velocidad, tipo de conexión, etc.)
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        // 3. Verificamos dos condiciones críticas:
        //   (evita falsos positivos como Wi-Fi sin acceso a internet o portales cautivos de hoteles).
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}