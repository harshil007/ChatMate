# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Android-adb\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:\



# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:

-keep class com.squareup.wire.** { *; }
-keep public class * extends com.squareup.wire.**{*;}

# Keep methods with Wire annotations (e.g. @ProtoField)
-keepclassmembers class ** {
    @com.squareup.wire.ProtoField public *;
    @com.squareup.wire.ProtoEnum public *;
}

#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
