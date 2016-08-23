# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/ybao/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.preference.Preference
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.**

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }




# bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}


# andfix
-keep public class * extends java.lang.annotation.Annotation
-keepclasseswithmembernames class * {native <methods>;}
-keep class com.alipay.euler.andfix.**{ *; }

# weixin
-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.sdk.** {*;}

# 信鸽推送
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.android.tpush.**  {* ;}
-keep class com.tencent.mid.**  {* ;}


# okhttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

# retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# butterknife
-keep class butterknife.** { *; }

-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


# rxjava
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


# 内嵌类
-keep class com.null.test.MainActivity$* {
    *;
}


-keepclassmembers class * {
    void *(**On*Event);
}

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}


# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# 保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}


 # 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


 # 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}




#保持自定义组件不被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}


#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
-keepclassmembers enum * {
  public static **[] values();
 public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}


#xUtils(保持注解，及使用注解的Activity不被混淆，不然会影响Activity中你使用注解相关的代码无法使用)
-keep class * extends java.lang.annotation.Annotation {*;}
-keep class com.otb.designerassist.activity.** {*;}



##混淆保护自己项目的部分代码以及引用的第三方jar包library（想混淆去掉"#"）
#-libraryjars libs/jg_filter_sdk_1.1 上午11.48.50.jar
#-libraryjars libs/libammsdk.jar
#-libraryjars libs/mta-sdk-1.6.2.jar
#-libraryjars libs/open_sdk_r5756.jar
#-libraryjars libs/tbs_sdk_thirdapp_v2.1.2.1096_36511_withdownload_obfs_20160727_105857.jar
#-libraryjars libs/weiboSDKCore_3.1.4.jar
#-libraryjars libs/wup-1.0.0.E-SNAPSHOT 上午11.48.50.jar
#-libraryjars libs/Xg_sdk_v2.46_20160602_1638 上午11.48.50.jar




#第三方library
-keep class io.reactivex.**{ *; }
-keep class com.jakewharton.**{ *; }
-keep class com.facebook.fresco.**{ *; }
-keep class com.bigkoo.**{ *; }
-keep class me.drakeet.materialdialog.**{ *; }
-keep class com.yqritc.**{ *; }
-keep class com.daimajia.numberprogressbar.**{ *; }
-keep class com.alipay.euler.**{ *; }
-keep class com.tencent.bugly.**{ *; }
-keep class com.zhuge.analysis.**{ *;}
-keep class me.biubiubiu.justifytext.**{ *;}



###-------- Gson 相关的混淆配置--------
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }



###---------  reservoir 相关的混淆配置-------
-keep class com.anupcowkur.reservoir.** { *;}



#webview
-keepclassmembers class net.muxi.huashiapp.webview.WebViewActivity {
       public *;
}
-keepattributes *JavascriptInterface*

#忽略警告
-ignorewarnings





