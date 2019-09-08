
![Jessie](./docs/jessie-svg.svg)

# Jessie

[![Download](https://api.bintray.com/packages/7hens/maven/jessie/images/download.svg)](https://bintray.com/7hens/maven/jessie/_latestVersion)
[![license](https://img.shields.io/github/license/7hens/jessie.svg)](https://github.com/7hens/jessie/blob/master/LICENSE)

Jessie 是一个插件化框架，可以让其他 APP 在免安装的情况下直接运行。

Jessie 可以像一个普通的 library 一样直接添加到宿主程序的依赖中。在宿主启动之后，Jessie 会自动运行。
其实，Jessie 在这背后在了很多事情，但这些事情对开发者来说是无感知的。

**Jessie 支持以下特性：**

- 支持加载第三方 APK
- 支持四大组件和 Fragment（动态和静态）
- 支持 Android Support 和 Jetpack
- 支持自定义 Theme
- 纯 Kotlin 实现

[开发中...](./docs/develop) 

## 开始使用

### 设置依赖

在宿主程序的 build.gradle 中添加下面的依赖，插件程序无需配置。

```groovy
implementation "cn.thens:jessie:<last_version>"
```

### 初始化

在宿主程序启动之后，Jessie 会自动运行并初始化，不需要额外的配置。

### 安装插件

```kotlin
val program = Jessie.install(apk)
```

### 卸载插件

```kotlin
Jessie.uninstall(programPackageName)
```

### 获取已安装的所有插件

```kotlin
Jessie.programs
```

### 获取插件信息

```kotlin
program.packageName
program.packageInfo
program.packageComponents
program.resources
program.classLoader
program.dexInfo
```

### 启动插件

宿主启动插件的 Launcher Activity

```kotlin
program.start()
```

宿主启动插件的特定 Activity

```kotlin
val intent = Intent().setComponent(programActivityComponent)
Jessie.startActivity(context, intent)
Jessie.startActivityForResult(context, intent, requestCode)
```
