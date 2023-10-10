#### PDF的预览的下载的区别

1，注意：后端对于PDF在网页端的下载和预览的处理是不同的

下载的话，把后台映射文件路径URL传给前端，前端直接访问就是默认下载。

而预览的话，是后端传输文件流到前端。

```java
//下载文件进行如下设置，预览不用。
response.setHeader("Content-Disposition", "attachment; filename=EncryptPDF");  
```

参照本目录下范例代码以及下面代码。

其它代码范例，注意根据实际情况修改和完善。

```java
@Inner(value = false)
	@RequestMapping(value = "/preview", method = RequestMethod.GET)
	public void pdfStreamHandler(HttpServletResponse response) {
		//PDF文件地址
		File file = new File("D:\\123.pdf");
		if (file.exists()) {
			byte[] data = null;
			FileInputStream input=null;
			OutputStream out = null;
			try {
				input= new FileInputStream(file);
				data = new byte[1024 * 1024];
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/pdf");
				out = response.getOutputStream();
				int count = 0;
				while ((count = input.read(data)) != -1){
					out.write(data,0,count);
				}

				out.flush();

			} catch (Exception e) {
				System.out.println("pdf文件处理异常：" + e);
			}finally{
				if(input != null){
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(out != null){
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
```

