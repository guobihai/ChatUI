#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#忽略警告
-ignorewarning
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}
-keep public class * extends android.os.IInterface


-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}




-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
 public void *(android.view.View);
}

-keepclassmembers enum * { # 保持枚举 enum 类不被混淆
 public static **[] values();
 public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
 public static final android.os.Parcelable$Creator *;
}

#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------

#eventBus
-keepattributes *Annotation*

-keep enum org.greenrobot.eventbus.ThreadMode { *; }


#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.** { *; }

#retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-dontwarn org.robovm.**
-keep class org.robovm.** { *; }

#okhttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-keep class okhttp3.** { *;}
-keep class okio.** { *;}
-dontwarn sun.security.**
-keep class sun.security.** { *;}
-dontwarn okio.**
-dontwarn okhttp3.**

#okhttp
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-keep class okhttp.** { *;}
-keep class okio.** { *;}
-dontwarn sun.security.**
-keep class sun.security.** { *;}
-dontwarn okio.**
-dontwarn okhttp.**

#rxjava
#-dontwarn rx.**
#-keep class rx.** { *; }
#
#-dontwarn sun.misc.**
#-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
# long producerIndex;
# long consumerIndex;
#}
#-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
# rx.internal.util.atomic.LinkedQueueNode producerNode;
#}
#-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
# rx.internal.util.atomic.LinkedQueueNode consumerNode;
#}

##baidu
#-keep class com.baidu.** { *; }
#-dontwarn com.baidu.**
#
##alipay
#-keep class com.alipay.android.app.IAlixPay{*;}
#-keep class com.alipay.android.app.IAlixPay$Stub{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
#-keep class com.alipay.sdk.app.PayTask{ public *;}
#-keep class com.alipay.sdk.app.AuthTask{ public *;}
#-keep class com.alipay.mobilesecuritysdk.*
#-keep class com.ut.*
#-dontwarn android.net.**
#-keep class android.net.** { *; }


-keep class trf.smt.com.netlibrary.utils** {*;}
-keep class trf.smt.com.netlibrary.bean** {*;}
-keep class trf.smt.com.netlibrary.enity** {*;}
-keep class com.smart.chatui.enity** {*;}
-keep class com.smart.chatui.widget** {*;}


#gson
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.** {
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-dontwarn com.google.gson.**

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }


#pinyin4j
-dontwarn net.soureceforge.pinyin4j.**
-dontwarn demo.**
#-libraryjars libs/pinyin4j-2.5.0.jar
-keep class net.sourceforge.pinyin4j.** { *;}
-keep class demo.** { *;}
-keep class com.hp.** { *;}
-keep class net.sourceforge.pinyin4j.**{*;}
-keep class net.sourceforge.pinyin4j.format.**{*;}
-keep class net.sourceforge.pinyin4j.format.exception.**{*;}

#httpclient (org.apache.http.legacy.jar)
-dontwarn android.net.compatibility.**
-dontwarn android.net.http.**
-dontwarn com.android.internal.http.multipart.**
-dontwarn org.apache.commons.**
-dontwarn org.apache.http.**
-dontwarn org.apache.http.protocol.**
-keep class android.net.compatibility.**{*;}
#-keep class android.net.http.**{*;}
-keep class com.android.internal.http.multipart.**{*;}
-keep class org.apache.commons.**{*;}
-keep class org.apache.org.**{*;}
-keep class org.apache.harmony.**{*;}

##lib-wheel
#-dontwarn kankan.wheel.**
#-keep class kankan.wheel.**{*;}
#
##PhotoPicker
#-dontwarn me.iwf.photopicker.**
#-keep class me.iwf.photopicker.**{*;}
#
##nineoldandroids
#-dontwarn com.nineoldandroids.*
#-keep class com.nineoldandroids.** { *;}

#weixin
#-dontwarn com.tencent.mm.**
#-keep class com.tencent.mm.**{*;}

#topsnackbar
#-dontwarn com.androidadvance.topsnackbar.**
#-keep class com.androidadvance.topsnackbar.**{*;}



#授权
#-dontwarn com.zhy.m.**
#-keep class com.zhy.m.** {*;}
#-keep interface com.zhy.m.** { *; }
#-keep class **$$PermissionProxy { *; }




-keep class com.jph.takephoto.** { *; }
-dontwarn com.jph.takephoto.**

-keep class com.darsh.multipleimageselect.** { *; }
-dontwarn com.darsh.multipleimageselect.**

-keep class com.soundcloud.android.crop.** { *; }
-dontwarn com.soundcloud.android.crop.**

#green dao
-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}


# keep okhttp3、okio
-dontwarn okhttp3.**
-keep class okhttp3.** { *;}
-keep interface okhttp3.** { *; }
-dontwarn okio.**

-keep class com.smtcartpay.bean.PhoneNo{*;}
# keep rx
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

# 如果你需要兼容6.0系统，请不要混淆org.apache.http.legacy.jar
-dontwarn android.net.compatibility.**
-dontwarn android.net.http.**
-dontwarn com.android.internal.http.multipart.**
-dontwarn org.apache.commons.**
-dontwarn org.apache.http.**
-keep class android.net.compatibility.**{*;}
-keep class android.net.http.**{*;}
-keep class com.android.internal.http.multipart.**{*;}
-keep class org.apache.commons.**{*;}
-keep class org.apache.http.**{*;}


-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}
# tinker混淆规则
-dontwarn com.tencent.tinker.**
-keep class com.tencent.tinker.** { *; }


-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static void dropTable(org.greenrobot.greendao.database.Database, boolean);
    public static void createTable(org.greenrobot.greendao.database.Database, boolean);
}