
	/**
	 * word转pdf
	 * @param inPath word路径
	 * @param outPath pdf存放路径
	 * @throws Exception
	 */
	public static void doc2pdf(String inPath, String outPath) throws Exception {

		long old = System.currentTimeMillis();
		// 新建一个空白pdf文档
		File file = new File(outPath);
		FileOutputStream os = new FileOutputStream(file);

		// Address是将要被转化的word文档
		Document doc = new Document(inPath);

		// 全面支持DOC, DOCX, OOXML, RTF HTML,
		doc.save(os, SaveFormat.PDF);
		long now = System.currentTimeMillis();
		// 转化用时
		System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");

	}
