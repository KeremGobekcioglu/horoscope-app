# ---------------------------------------------------------------------------
# General
# ---------------------------------------------------------------------------
# Signature/InnerClasses/EnclosingMethod are required for kotlinx.serialization's
# generic serializer resolution (e.g. ListSerializer<T>, MapSerializer<K, V>) and
# for kotlin-reflect-free generic lookups used by Navigation-Compose's type-safe
# routes. *Annotation* is required to keep @SerialName / @Serializable visible
# at runtime. SourceFile/LineNumberTable keep readable stack traces in Crashlytics.
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
-keepattributes SourceFile, LineNumberTable

# ---------------------------------------------------------------------------
# kotlinx.serialization
# Official rules from https://github.com/Kotlin/kotlinx.serialization/blob/master/rules/common.pro
# Needed because this project's DTOs (Firestore/API models) and its
# kotlinx.serialization-based Navigation-Compose routes (navigation/Routes.kt)
# rely on the generated $serializer classes being resolvable at runtime.
# ---------------------------------------------------------------------------
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep `Companion` object fields of serializable classes.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

# Keep `serializer()` on companion objects of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$Companion Companion;
}
-keepclassmembers class <1>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects (covers @Serializable
# `object` routes such as Home, Calendar, Settings, CompatibilityGraph, etc.)
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# Support for kotlinx.serialization's generated $serializer nested classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<1> {
    <init>(...);
}

-keepclassmembers @kotlinx.serialization.Serializable class ** {
    <fields>;
}

-dontnote kotlinx.serialization.**
-dontwarn kotlinx.serialization.**

# ---------------------------------------------------------------------------
# Room (androidx.room + KMP androidx.sqlite)
# Room's own consumer rules already keep "* extends RoomDatabase" (which
# transitively covers the generated *_Impl classes since RoomDatabase.
# getGeneratedImplementation() resolves them via Class.forName using the
# database class's canonical name). Kept explicitly here as a safety net
# since consumer-rule merging for the newer KMP "androidx.kotlin.multiplatform.library"
# plugin is less battle-tested than the classic android-library plugin.
# ---------------------------------------------------------------------------
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-dontwarn androidx.room.paging.**

# ---------------------------------------------------------------------------
# Ktor (client-core + client-okhttp + content-negotiation)
# OkHttp/okio reference optional runtime-only providers (Conscrypt, BouncyCastle,
# JDK9 modules) that are not on the classpath. These are normally covered by
# OkHttp's own bundled consumer rules, but are declared explicitly to avoid an
# R8 "missing classes" build failure if consumer-rule merging misbehaves.
# ---------------------------------------------------------------------------
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn okio.**

# ---------------------------------------------------------------------------
# Firebase / GitLive Firebase wrapper / gRPC-Firestore transitive deps
# ---------------------------------------------------------------------------
-dontwarn com.google.errorprone.annotations.**
-dontwarn com.google.j2objc.annotations.**
-dontwarn org.checkerframework.**
-dontwarn javax.annotation.**
-dontwarn javax.lang.model.**

# ---------------------------------------------------------------------------
# App models that cross a serialization boundary (Firestore docs, API DTOs,
# Room entities holding serialized JSON columns, nav routes). Belt-and-suspenders
# on top of the generic kotlinx.serialization rules above, scoped to our own
# packages so it can't mask real dead-code elsewhere.
# ---------------------------------------------------------------------------
-keep,includedescriptorclasses class com.kg.yildizname.**$$serializer { *; }
-keepclassmembers class com.kg.yildizname.** {
    *** Companion;
}
-keepclasseswithmembers class com.kg.yildizname.** {
    kotlinx.serialization.KSerializer serializer(...);
}
