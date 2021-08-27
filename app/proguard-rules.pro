# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidSDK/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#------------------------------------------主项目混淆规则----------------------------------------------
#实体类不参与混淆

-keepattributes EnclosingMethod
#tkrefreshlayout

-keep class com.lcodecore.tkrefreshlayout.** { *; }
-dontwarn com.lcodecore.tkrefreshlayout.**

#-------------------------------------------Base混淆规则----------------------------------------------
#---------------------------------1.实体类---------------------------------

-keep class com.gw.slbdc.model.** { *; }

-keep class com.gw.slbdc.base.** { *; }
#工具类不混淆
-keep class com.gw.slbdc.util.** { *; }


#图片旋转框架
-keep class com.lzy.imagepicker.**{*;}
#图片处理框架不混淆
-keep class com.slb.imaging.**{*;}
-keep class com.slb.media_lib.**{*;}
#-------------------------------------------------------------------------
#===============================================================
#--------------------------------2.第三方包-------------------------------
#GreenDao
-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

#高德地图

-dontwarn com.amap.api.**
-dontwarn com.a.a.**
-dontwarn com.autonavi.**
-dontwarn com.loc.**


-keep class com.amap.api.**  {*;}
-keep class com.autonavi.**  {*;}
-keep class com.a.a.**  {*;}
-keep class com.loc.**  {*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#umeng
-keep public class com.umeng.fb.ui.ThreadView {
}
-keepclassmembers class * {
  	public <init>(org.json.JSONObject);
}
-keep public class com.gw.slbdc.R$*{
	public static final int *;
}
-keep class com.umeng.commonsdk.**{*;}
-dontwarn com.umeng.commonsdk.**

#腾讯X5内核浏览器中的的代码不被混淆
-keep class com.tencent.** {*;}
#RSA中的代码不被混淆
-keep class Decoder.** {*;}
-keep class org.bouncycastle.** {*;}
#讯飞语音
-keep class com.iflytek.**{*;}
-dontwarn com.iflytek.**
#JZ 视频
-keep public class cn.jzvd.JZMediaSystem {*; }
-keep public class cn.jzvd.demo.CustomMedia.CustomMedia {*; }
-keep public class cn.jzvd.demo.CustomMedia.JZMediaIjk {*; }
-keep public class cn.jzvd.demo.CustomMedia.JZMediaSystemAssertFolder {*; }

-keep class tv.danmaku.ijk.media.player.** {*; }
-dontwarn tv.danmaku.ijk.media.player.*
-keep interface tv.danmaku.ijk.media.player.** { *; }

#小鱼易连
#-keep class com.llvision.glass3.**{*;}
#-dontwarn com.llvision.glass3.**
#-keep class com.ainemo.**{*;}
#-dontwarn com.ainemo.**

-optimizationpasses 5
-dontpreverify
-dontoptimize
-verbose
-keepattributes *Annotation*
-ignorewarnings
-keepclasseswithmembernames class * {
     native <methods>;
}
-keepattributes *Annotation*
-keep enum com.ainemo.sdk.NemoSDKListener** {
    **[] $VALUES;
    public *;
}
-keepclassmembers enum * { *; }
-keepnames class * implements java.io.Serializable
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class com.ainemo.sdk.model.Settings{*;}
-keep class com.ainemo.sdk.NemoSDK{
  public *;
}
-keep class com.ainemo.**{*;}
-keep class android.utils.**{*;}
-keep class android.log.**{*;}
-keep class android.http.**{*;}
-keep class vulture.module.**{*;}
-keep class vulture.home.call.media.omap.**{*;}

-keep class com.google.gson.stream.** {*;}
-keep class com.google.gson.** {*;}
-keep class com.google.gson.Gson {*;}
-keep class com.google.gson.examples.android.model.** {*;}
-keep class rx.internal.util.**{*;}
##需要添加util类，不然会报错
-keep class android.util.**{*;}
-dontwarn com.android.util.**
#support
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-dontwarn android.support.**

#databinding
-keep class android.databinding.** { *; }
-dontwarn android.databinding.**

#annotation
-keep class android.support.annotation.** { *; }
-keep interface android.support.annotation.** { *; }

#retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.sunloto.shandong.bean.** { *; }
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**
#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keep class **$$ViewInjector { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#glide
-keep public class * implements com.bumptech.glide.module.AppGlideModule
-keep public class * implements com.bumptech.glide.module.LibraryGlideModule
-keep class com.bumptech.glide.** { *; }
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#glide-transformations
-keep class jp.wasabeef.glide.transformations.** {*;}
-dontwarn jp.wasabeef.glide.transformations.**

#okhttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn okio.**

#RxJava RxAndroid
-dontwarn rx.*
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

#RxLifecycle
-keep class com.trello.rxlifecycle2.** { *; }
-keep interface com.trello.rxlifecycle2.** { *; }
-dontwarn com.trello.rxlifecycle2.**

#RxPermissions
-keep class com.tbruyelle.rxpermissions2.** { *; }
-keep interface com.tbruyelle.rxpermissions2.** { *; }

#material-dialogs
-keep class com.afollestad.materialdialogs.** { *; }
-dontwarn om.afollestad.materialdialogs.**

#=====================bindingcollectionadapter=====================
-keep class me.tatarka.bindingcollectionadapter.** { *; }
-dontwarn me.tatarka.bindingcollectionadapter.**
-keep class **_FragmentFinder { *; }
-keep class androidx.fragment.app.* { *; }

-keep class com.qmuiteam.qmui.arch.record.RecordIdClassMap { *; }
-keep class com.qmuiteam.qmui.arch.record.RecordIdClassMapImpl { *; }

-keep class com.qmuiteam.qmui.arch.scheme.SchemeMap {*;}
-keep class com.qmuiteam.qmui.arch.scheme.SchemeMapImpl {*;}

#--------------------------------jar包处理-------------------------------------------
#-libraryjars libs/armeabi-v7a/libcallmodule_balanar.so
#-libraryjars libs/armeabi-v7a/liblbs.so
#-libraryjars libs/armeabi-v7a/libmsc.so
#-libraryjars libs/armeabi-v7a/libpalmcodec.so
#-libraryjars libs/armeabi-v7a/libsignature_balanar.so
#-libraryjars libs/tbs_sdk_thirdapp_v4.3.0.1020_43633_sharewithdownload_withoutGame_obfs_20190111_105200.jar
#-libraryjars libs/xylink-android-sdk-2.20.9.jar
#-libraryjars libs/bvroutine_apt.jar
#-libraryjars libs/Msc.jar
#-libraryjars libs/Sunflower.jar


#---------------------------------3.与js互相调用的类------------------------

#-keep class com.gw.waterregime.base.js.** { *; }

#----------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------


#----------------------------------------------------------------------------

#---------------------------------5.自定义控件------------------------------

-keep class com.gw.safty.** { *; }


#----------------------------------------------------------------------------
#---------------------------------6.其他定制区-------------------------------
#native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
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
#保持类中的所有方法名
-keepclassmembers class * {
    public <methods>;
    private <methods>;
}

#----------------------------------------------------------------------------

#---------------------------------基本指令区---------------------------------
# 抑制警告
-ignorewarnings
#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化  不优化输入的类文件
-dontoptimize
 #预校验
-dontpreverify
 #混淆时是否记录日志
-verbose
 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#混淆包路径
#-repackageclasses ''
#-flattenpackagehierarchy ''

#保护注解
-keepattributes *Annotation*

#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature

#保留SourceFile和LineNumber属性
-keepattributes SourceFile,LineNumberTable

#忽略警告
#-ignorewarning
#----------记录生成的日志数据,gradle build时在本项目根目录输出---------
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

#androidx
-keep class androidx.** {*;}
-keep interface androidx.** {*;}
-keep public class * extends androidx.**
-keep class com.google.android.material.** {*;}
-dontwarn androidx.**
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}




-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#----------------------------------------------------------------------------

#hotfix
-applymapping mapping.txt
-keep class com.taobao.sophix.**{*;}
-keep class com.ta.utdid2.device.**{*;}
-dontwarn com.alibaba.sdk.android.utils.**
#防止inline
-dontoptimize

-keepclassmembers class com.gw.slbdc.application.MyApplication {
    public <init>();
}
# 如果不使用android.support.annotation.Keep则需加上此行
-keep class com.gw.slbdc.application.SophixStubApplication$RealApplicationStub

#----------------------------------------------------------------------------
#XPopup
-dontwarn com.lxj.xpopup.widget.**
-keep class com.lxj.xpopup.widget.**{*;}
#----------------------------------------------------------------------------

# week
#----------------------------------------------------------------------------

-keep class com.taobao.weex.layout.** { *; }
-keep class com.taobao.weex.WXSDKEngine { *; }
-keep class com.taobao.weex.base.SystemMessageHandler { *; }
-dontwarn com.taobao.weex.bridge.**

-keep class com.taobao.weex.WXDebugTool{*;}
-keep class com.taobao.weex.devtools.common.LogUtil{*;}
-keepclassmembers class ** {
  @com.taobao.weex.ui.component.WXComponentProp public *;
}
-keep class com.taobao.weex.bridge.**{*;}
-keep class com.taobao.weex.dom.**{*;}
-keep class com.taobao.weex.adapter.**{*;}
-keep class com.taobao.weex.common.**{*;}
-keep class * implements com.taobao.weex.IWXObject{*;}
-keep class com.taobao.weex.ui.**{*;}
-keep class com.taobao.weex.ui.component.**{*;}
-keep class com.taobao.weex.utils.**{
    public <fields>;
    public <methods>;
    }
-keep class com.taobao.weex.view.**{*;}
-keep class com.taobao.weex.module.**{*;}
-keep public class * extends com.taobao.weex.common.WXModule{*;}
-keep public class * extends com.taobao.weex.ui.component.WXComponent{*;}
-keep class * implements com.taobao.weex.ui.IExternalComponentGetter{*;}
#----------------------------------------------------------------------------

#-------------------------AopArms------------------------------------
#-keepclasseswithmembers class * {
#@cn.com.superLei.aoparms.annotation.Permission <methods>;
#}
#
#-keepclasseswithmembers class * {
#@cn.com.superLei.aoparms.annotation.PermissionDenied <methods>;
#}
#
#-keepclasseswithmembers class * {
#@cn.com.superLei.aoparms.annotation.PermissionNoAskDenied <methods>;
#}
#-------------------------AopArms------------------------------------

# liveEventBus
-dontwarn com.jeremyliao.liveeventbus.**
-keep class com.jeremyliao.liveeventbus.** { *; }
-keep class android.arch.lifecycle.** { *; }
-keep class android.arch.core.** { *; }
#----------------------------------------------------------------------------
