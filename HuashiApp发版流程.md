# HuashiApp发版流程
---
## 1.更改版本号：  
- 修改在`config.gradle`中的ext块中的版本号，2.4.x，如有大改动再升级2.5  
另所有新添加的dependencies都需添加到config.gradle中集中管理，然后再到各个板块中引用

## 注：打包签名需要的 HuashiApp.jks和 keystore.properties请像相关人员请求
## 2.Tinker配置
1. 运行gradle的assemble任务生成基准包
2. 在ccnubox/build/bakApk/文件夹下，找到这次build生成的包，然后把该文件名改到 tinker.gradle的baseInfo属性上
3. 修改tinker.gradle的上面对应的app版本号，应设置与步骤一中相同
4. 把该基准包存到百度网盘或其他不易丢失的地方保存，并分享给大家，方便大家热更新生成补丁

## 3.打一个release tag
- git tag命令为该次release打一个tag指针push到github上

## 4.将release包（apk）上传到七牛云
1. 先删除原来的release/latest/ccnubox.apk
2. 再将apk上传至团队七牛云的华南ossccnubox空间
3. 刷新对应文件缓存
4. post向后端发送更新数据(需添加签名认证，basic auth)
5. 详细参见[上传流程](https://zybuluo.com/Humbert/note/1048906)

## 5.代码规范---请依照《阿里Android开发手册》
## 6.github代码管理
- 由于历史遗留原因，huashiapp github仓库的master分支基本已废弃，目前使用维护new_architecture分支，请不要合并new_architecture与master分支，如果进行新的功能开发，请新建分支，完成后合并到new_architecture分支上

## 7.后续项目管理维护
1. bugly：在bugly上查看崩溃率与bug出新情况，及时修复bug，将崩溃率维持在3%以下
2. 在tinkerpatch上发布热更新补丁


