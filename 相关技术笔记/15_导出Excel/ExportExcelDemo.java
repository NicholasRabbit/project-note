
/*
 * 使用的是Apache的依赖
 * */
	@ApiOperation(value = "生成excel", notes = "生成excel")
	@GetMapping("/createExcel")
	public void createExcel(Page<Laboratory> page, Project project, HttpServletResponse response) {
		List<Laboratory> list = laboratoryService.listScope();
		
		//设置表格名称，表格第一行表头也用到了这个名称
		String title = "重点实验室";
		try {
			generateExcel(title, list, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void generateExcel(String title, List<Laboratory> list, HttpServletResponse response) throws Exception {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("content-disposition", "attachement;filename=" + new String((title + ".xls").getBytes("gb2312"), "ISO8859-1"));
		ServletOutputStream os = null;
		os = response.getOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(os);
		//创建工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		//创建表单
		XSSFSheet sheet = ExcelUtil.genSheet(workbook, "excel");
		//创建表单样式
		XSSFCellStyle titleStyle = ExcelUtil.genTitleStyle(workbook);//创建标题样式
		XSSFCellStyle contextStyle = ExcelUtil.genContextStyle(workbook);//创建文本样式
		XSSFCellStyle contextRightStyle = ExcelUtil.genContextNoBorderStyle(workbook);//创建文本居右样式


		//创建Excel
		genExcel(title, list, sheet, titleStyle, contextStyle, contextRightStyle);

		//将工作薄写入文件输出流中
		workbook.write(bos);
	}

	private static void genExcel(String title, List<Laboratory> list, XSSFSheet sheet, XSSFCellStyle titleStyle, XSSFCellStyle contextStyle, XSSFCellStyle contextRightStyle) {
		//根据不用列设置宽度
		for (int i = 0; i < 10; i++) {
			if (i == 0) {
				sheet.setColumnWidth(i, 1000);
			} else if(i == 1 || i == 4){
				sheet.setColumnWidth(i,5000);
			} else if (i == 2 || i == 7) {
				sheet.setColumnWidth(i, 10000);
			} else {
				sheet.setColumnWidth(i, 3000);
			}
		}

		//设置标题位置
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
		XSSFRow row = sheet.createRow(0);//创建第一行，为标题，index从0开始
		ExcelUtil.setCell(0, title, row, titleStyle);

		row = sheet.createRow(1);//创建第二行
		ExcelUtil.setCell(0, "序号", row, contextStyle);
		ExcelUtil.setCell(1, "企业名称", row, contextStyle);
		ExcelUtil.setCell(2, "重点实验室名称", row, contextStyle);
		ExcelUtil.setCell(3, "级别", row, contextStyle);
		ExcelUtil.setCell(4, "成立时间", row, contextStyle);
		ExcelUtil.setCell(5, "主管单位", row, contextStyle);
		ExcelUtil.setCell(6, "负责人", row, contextStyle);
		ExcelUtil.setCell(7, "依托单位和共建单位", row, contextStyle);
		ExcelUtil.setCell(8, "验收评价情况", row, contextStyle);


		int total = 1;
		int rowNum = 2;
		for (int i = 0; i < list.size(); i++) {
			Laboratory data = list.get(i);//左边数据
			row = sheet.createRow(rowNum++);
			ExcelUtil.setCell(0, total++, row, contextStyle);
			ExcelUtil.setCell(1, "冀中能源集团" ,row, contextStyle);
			ExcelUtil.setCell(2, data.getLabName(), row, contextStyle);
			ExcelUtil.setCell(3, data.getRatingLevelName(), row, contextStyle);
			ExcelUtil.setCell(4, data.getStartDateName(), row, contextStyle);
			ExcelUtil.setCell(5, data.getSuperiorDept(), row, contextStyle);
			ExcelUtil.setCell(6, data.getSupervisor(), row, contextStyle);
			ExcelUtil.setCell(7, data.getCooperatedComp(), row, contextStyle);
			ExcelUtil.setCell(8, data.getVerification(), row, contextStyle);
		}

	}
