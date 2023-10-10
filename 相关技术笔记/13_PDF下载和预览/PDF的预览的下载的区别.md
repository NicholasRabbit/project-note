#### PDF的预览的下载的区别

1，注意：后端对于PDF在网页端的下载和预览的处理是不同的

下载的话，把后台映射文件路径URL传给前端，前端直接访问就是默认下载。

而预览的话，是后端传输文件流到前端。

```java
//下载文件进行如下设置，预览不用。
response.setHeader("Content-Disposition", "attachment; filename=EncryptPDF");  

```



