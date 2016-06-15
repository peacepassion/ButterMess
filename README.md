# ButterMess

## 背景
在使用Butterknife的时候,混淆时需要keep被Butterkife注入的类以及响应的生成类.如下所示:  
```yml
-keep class butterknife.** { *; }  
-dontwarn butterknife.internal.**  
-keep class **$$ViewInjector { *; }  
-keepclasseswithmembernames class * {@butterknife.* <fields>; }  
-keepclasseswithmembernames class * {@butterknife.* <methods>;}  
```

原因是因为Butterknife是按照一定的字符拼接规则来查找每一个对象的Injector的类名,然后反射实例化这个Injector,进而完成注入.
字符的规则是`ClassToBeInjected$$ViewInjector`, 这样的话,每一个需要使用Butterknife的类都无法混淆.

## 解决办法
### 构建时
打开`ClassToBeInjected$$ViewInjector`的混淆,在proguard完成的时候,收集到每一个`ClassToBeInjected$$ViewInjector`的混淆映射信息,
进而查找到`ClassToBeInjected`的混淆映射信息, 进而收集到`ClassToBeInjected`混淆后的类与混淆后的ViewInjector的混淆映射信息.
### 运行时
查看Butterknife的源码,Butterknife的Injector查找都是通过`Butterknife#INJECTORS`这么一个map查找的,
通过反射,强行用编译时收集到的映射信息写入这个变量,即可使得Butterknife正确的找到ViewInjector的类型信息.

## 使用方法

```groovy

 dependencies {
        classpath 'me.ele:eradle:1.5.3'
        classpath 'com.android.tools.build:gradle:1.5.+'
        classpath 'me.ele:butter-mess-plugin:0.0.5'
   }
    
apply plugin: 'com.android.application'
apply plugin: 'eradle'
apply plugin: 'me.ele.butter-mess'

```

## Usage

Init ButterMess in the custom Application like following.

```java
public class App extends Application {
  @Override public void onCreate() {
    super.onCreate();
    ButterMess.init();
  }
}
```

```
-dontshrink
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
```

## feel free to use, welcome issue and comment

