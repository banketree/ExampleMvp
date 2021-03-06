# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# androidX
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends android.app.AppCompatActivity                               # 保持哪些类不被混淆
-keep public class * extends androidx.appcompat.app.AppCompatActivity                               # 保持哪些类不被混淆
-keep public class * extends androidx.fragment.app.Fragment                               # 保持哪些类不被混淆
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**

#协程
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}