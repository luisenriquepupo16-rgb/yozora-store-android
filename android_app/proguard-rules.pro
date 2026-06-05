# ============================================
# PROGUARD RULES PARA YOZORA STORE
# ============================================

# === REGLAS GENERALES ===

# Mantener nuestras clases principales (no ofuscar nombres importantes)
-keep class com.yozorastore.** { *; }
-keep class com.yozorastore.MainActivity { *; }
-keep class com.yozorastore.YozoraApplication { *; }

# === WEBVIEW (necesario para que funcione) ===

# Mantener WebView y sus métodos
-keepclassmembers class * extends android.webkit.WebView {
    public *;
}

# Mantener interfaces JavaScript (si las usas)
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# === ANDROIDX Y COMPATIBILIDAD ===

# Mantener componentes de AndroidX
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-dontwarn androidx.**

# Mantener AppCompat
-keep class androidx.appcompat.** { *; }
-keep interface androidx.appcompat.** { *; }

# === KOTLIN ===

# Mantener anotaciones de Kotlin
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }
-dontwarn kotlin.**

# === SWIPEREFRESHLAYOUT ===

-keep class androidx.swiperefreshlayout.** { *; }

# === REGLAS DE OPTIMIZACIÓN ===

# Optimizar agresivamente
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Mantener nombres de atributos para que no se rompa el JSON (si usas)
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

# === ELIMINAR LOGS EN RELEASE ===

# Eliminar todas las llamadas a Log.d, Log.v, Log.i en release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# === MANTENER EXCEPCIONES (para debugging) ===

-keepattributes Exceptions,InnerClasses