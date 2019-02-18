# android-tbs-file-reader
简介：希望利用TBS腾讯浏览服务使得第三方APP可以在自己APP的页面浏览word、excel、ppt、txt这些常见的文档。

代码结构：

1.后端代码提供服务以供Android客户端下载文件来测试。是一个很简单的web服务。
  两个接口，分别是：http://localhost:8080/file/list 和 http://localhost:8080/file/download?fileName=没密码.doc
  对应获取文件列表和下载文件（fileName参数填文件名）
  
  启动项目：用idea打开代码，等构造完应该就能直接run了
  
2.android客户端代码用来测试TBS腾讯浏览服务。
  有2个页面，一个是文件列表页面，另一个是预览文件页面。
  其中MainActivity有个HOST参数，需要换成上面后代服务器的ip，不然调用不了后端接口


已知问题：
1.不能打开有密码的文件。
2.如果手机第一次打开app处于断网的状态，那么就算再次联网，TBS插件也会一直报错，除非重新安装APP。
