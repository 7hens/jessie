# Jessie 开发文档

## 名词解释

- 宿主：也叫主程序，指可以动态加载并运行插件的程序
- 插件：运行在宿主上的程序，可以免安装运行
- 插件化：将项目分为宿主和插件部分，插件按需加载
- 热修复：动态更新类或者函数，一般用于修复 BUG

## Jessie 的启动流程

Jessie 在 AndroidManifest 中预埋了一个 MainInitProvider，用来做一些初始化工作。
MainInitProvider 会跟随着宿主 APP 的 Application 一起启动，
它会通过启动 JessieDaemonService 来主动唤醒一个名为 `:jcdm` 的守护进程（Daemon 进程）。

JessieDaemonProvider 会伴随着 Daemon 进程启动，
它会自动扫描已安装的 APK，并自动将他们加载入内存。
以后的所有插件 APK 的安装都会在 Daemon 进程中进行。

## 插进是如何安装的

安装插件的入口是 `Jessie.install(apk)`，
它会通过 AIDL 获取与 Daemon 进程通信的 `IJessieProgramManager` 实例。
在 Daemon 进程中，与之通信的是 `JessieProgramManagerImpl`，
它会调用 `PluginManager.install(apk)` 来安装插件。
`PluginManager` 内部维护了一张表，记录了所有安装过的插件程序（Program）。

> 整个插件的安装过程都是在 Daemon 进程中。

## 插件是如何运行的

每个插件都拥有自己单独的进程，进程分配的任务也是在 Daemon 进程进行的。
负责进程分配任务的类为 `ProcessDispatcher`。

### ClassLoader

ClassLoader 是类加载器，用来加载 Class。
ClassLoader 是插件运行的核心，任何插件化方案和热修复方案都离不开 ClassLoader。

> 在 Android 中，有 2 种最常见的 ClassLoader：
PathClassLoader 用来加载当前运行的 App 的 Class，
DexClassLoader 用来动态加载 APK 或 DEX 文件中的 Class。
要是实现插件化，必须要使用 DexClassLoader。

Jessie 中的每个插件都有自己的 ClassLoader —— PluginDexClassLoader，它们都继承制至 DexClassLoader。
PluginDexClassLoader 破坏了 ClassLoader 原有的[双亲委托机制](https://blog.csdn.net/xiangzhihong8/article/details/65446152)，
也就是说它会优先加载自己的 Class，在没有找到的情况下才会去寻找父 ClassLoader 中的 Class。

### Application

### Activity

我们知道 Android 中的 Activity 等四大组件等都有自己的生命周期，并且都必须要在 Manifest 文件中申明之后才能使用。但是插件是动态加载进来的，除非在宿主的 Manifest 中预埋一些 Activity 的坑位，否则很难让插件的 Activity 正常运行。

### Service

### Provider

## 测试

### 安装示例插件

```shell
./gradlew installPlugin
```

### 推荐测试 APP

为了在测试过程中更快的定位到问题，建议使用源码透明的 APP 来测试。

下面是一些建议的测试 APP。

| 名称 | 源码 | APK |
| ---- | ---- | ---- |
| 开源中国 | [Gitee](https://gitee.com/oschina/android-app/tree/v4.1.7/) | [应用宝](https://android.myapp.com/myapp/detail.htm?apkName=net.oschina.app&ADTAG=mobile) |
| Flutter-OSC | [Github](https://github.com/yubo725/flutter-osc) | [Github](https://github.com/yubo725/flutter-osc/blob/master/apk/app-release.apk) |
| Termux | [Github](https://github.com/termux/termux-app) | [F-Droid](https://f-droid.org/packages/com.termux/) |
| AndroidFire | [Github](https://github.com/jaydenxiao2016/AndroidFire) | [fir.im](https://fir.im/androidFire) |
| ARouter | [Github](https://github.com/alibaba/ARouter) | [Github](https://github.com/alibaba/ARouter/blob/develop/demo/arouter-demo.apk) |
| flutter-go | [Github](https://github.com/alibaba/flutter-go) | [Github](https://github.com/alibaba/flutter-go) |
| WanAndroid | [Github](https://github.com/senonwx/WanAndroid) | [Github](https://github.com/senonwx/WanAndroid) |
| GSYGithubApp | [Github](https://github.com/CarGuo/GSYGithubApp) | [蒲公英](https://www.pgyer.com/GSYGithubApp) |
| GSYGithubAppKotlin | [Github](https://github.com/CarGuo/GSYGithubAppKotlin) | [蒲公英](https://www.pgyer.com/XGtw) |
| GSYGithubAppWeex | [Github](https://github.com/CarGuo/GSYGithubAppWeex) | [蒲公英](https://www.pgyer.com/K5kU) |
| GSYGithubAppFlutter | [Github](https://github.com/CarGuo/GSYGithubAppFlutter) | [蒲公英](https://www.pgyer.com/vj2B) |

## 参考

- [Android P - veridex 工具扫描非 SDK 接口](https://blog.csdn.net/yi_master/article/details/80664674)