# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

##Glide
-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.**{*;}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# MMKV
-dontwarn com.tencent.mmkv.**
-keep class com.tencent.mmkv.**{*;}
-keep interface com.tencent.mmkv.**{*;}

# BaseRecyclerViewAdapterHelper
-keep class com.chad.library.**{*;}
-keep interface com.chad.library.**{*;}

# agentweb
-keep class com.just.agentweb.** {
    *;
}
-dontwarn com.just.agentweb.**

# pictureselector 图片选择
-keep class com.luck.picture.lib.** { *; }
-keep class com.luck.lib.camerax.** { *; }
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

# BaseRecyclerViewAdapterHelper
-keep class com.chad.library.**{*;}
-keep interface com.chad.library.**{*;}

# autosize
-dontwarn me.jessyan.**
-keep class me.jessyan.**{*;}
-keep class me.jessyan.**{*;}


#arouter
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider
#沉浸式状态栏
-keep class com.gyf.immersionbar.* {*;}
-dontwarn com.gyf.immersionbar.**
#banner
-keep class androidx.recyclerview.widget.**{*;}
-keep class androidx.viewpager2.widget.**{*;}
#Xpop
-dontwarn com.lxj.xpopup.widget.**
-keep class com.lxj.xpopup.widget.**{*;}
#loadsir
-dontwarn com.kingja.loadsir.**
-keep class com.kingja.loadsir.** {*;}
