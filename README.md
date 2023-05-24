# SerialPort
 Android 串口通讯实现，附带Modbus
 
 ## so文件
 ## 方便快捷的串口测试工具
 ## 简单直观的界面，测试置零和接受数据简单明了
 ## 参数保存功能

## 1、local.properties文件添加ndk路径
```
ndk.dir=D\:\\Android_Studio_2021\\Sdk\\ndk\\25.2.9519653
```

## 2、最好指定java的版本，library和引用项目的java版本一致
```
android{
	compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```

## 3、项目根build.gradle引用云仓（并且jcenter{ url ""}好像被弃用了）
```
repositories {
	maven { url 'https://jitpack.io' }
	google { url "https://maven.aliyun.com/repository/google" }
	mavenCentral { url "https://maven.aliyun.com/repository/public" }
}
```

## 4、编译打包library，在Terminal执行命令行 ./gradlew makeJar
```
./gradlew makeJar
```

## 5、编译打包aar文件存放路径 \build\outputs\aar\
```
\build\outputs\aar\
```

## 6、在build.gradle 文件中开启混淆，并设置混淆规则，混淆会让编译打包的aar文件无法使用，
## 需要在(proguard-rules.pro)添加一些开放文件，不混淆
```
android{
	
	buildTypes {
        release {
            //开启混淆
            minifyEnabled true
            // 移除无用的resource文件，与 minifyEnabled 一起使用
            //shrinkResources true//Error:Resource shrinker cannot be used for libraries.
            //Zipalign优化，让安装包的资源按4字节对齐，减少应用在运行时的内存消耗
            zipAlignEnabled true
            
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}
```

## 7、编译打包的aar文件分debug、release版本，release版本默认会混淆，混淆后的入口代码可能无法使用
## 需要过滤混淆，在(proguard-rules.pro)文件添加过滤目录，参考proguard-rules编写规则
```
-keep class android.serialport.*{*;}
```

## 8、通过下面方式，可以编译打包纯代码jar库文件，同样在Terminal执行命令行 ./gradlew makeJar
```
android{
	
	//Copy类型
    task makeJar(type: Copy) {
        //删除存在的
        delete 'build/libs/serialport.jar'
        //设置拷贝的文件
        //from('build/intermediates/bundles/release/')
        from('build/intermediates/aar_main_jar/release/')
        //打进jar包后的文件目录
        into('build/libs/')
        //将classes.jar放入build/libs/目录下
        //include ,exclude参数来设置过滤
        //（我们只关心classes.jar这个文件）
        include('classes.jar')
        //重命名
        rename ('classes.jar', 'serialport.jar')
    }
    makeJar.dependsOn(build)
	
	
    //在终端执行生成JAR包
    // ./gradlew makeJar
}
```

## 9、编译打包的aar文件，可以修改后缀并通过压缩文件删除不必要的资源文件
```

```







 
