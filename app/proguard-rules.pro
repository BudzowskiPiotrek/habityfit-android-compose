# =========================================================================
# CONFIGURACIÓN DE PROGUARD / R8 - SETPOINT APP
# =========================================================================

# -------------------------------------------------------------------------
# 1. REGLAS DE NETWORKING (DTOs de Sync)
# -------------------------------------------------------------------------

# Mantiene el nombre de las clases y sus miembros (variables y métodos)
-keep class com.piodev.network.dto.sync.** { *; }
-keep class com.piodev.network.dto.request.** { *; }
-keep class com.piodev.network.dto.response.** { *; }
-keepclassmembers class com.piodev.network.dto.sync.** { *; }
-keepclassmembers class com.piodev.network.dto.request.** { *; }
-keepclassmembers class com.piodev.network.dto.response.** { *; }

# -------------------------------------------------------------------------
# 2. ATRIBUTOS Y ANOTACIONES
# -------------------------------------------------------------------------

-keepattributes Signature, InnerClasses, AnnotationDefault, *Annotation*

# -------------------------------------------------------------------------
# 3. REGLAS PARA RETROFIT / OKHTTP (Opcional pero recomendado)
# -------------------------------------------------------------------------

-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**

# -------------------------------------------------------------------------
# 4. NOTAS DE MANTENIMIENTO
# -------------------------------------------------------------------------
