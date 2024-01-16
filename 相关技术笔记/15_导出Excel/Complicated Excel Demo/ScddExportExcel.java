
	/*
	 * scdd项目导出单元格案例
	 * */

	@Operation(summary = "分页查询" , description = "分页查询")
	@GetMapping("/createExcel")
	public void createExcel(String queryDate, String groupViewApproval, String approvalType, String type, HttpServletResponse response, String reportType, String compArea) {
		String title = "集团煤炭产量日报-" + queryDate;
		//获取数据
		List<ReportDailyV> data = dailyVList(queryDate, groupViewApproval, approvalType, type);

		//生成未完成计划说明
		Map<String, String> map = planService.getApprovalRemark(type, reportType, queryDate, approvalType, groupViewApproval, compArea);

		Date planDate = DateUtils.getPlanTime(queryDate, type, null);
		ReportApprovalJimu reportApprovalJimu = reportApprovalJimuService.getReportApproval(SecurityUtils.getUser().getCompId(), planDate, approvalType, reportType, compArea);
		map.put("submitName", reportApprovalJimu.getSubmitName());
		map.put("submitPhone", reportApprovalJimu.getSubmitPhone());
		map.put("personnelName", reportApprovalJimu.getPersonnelName());
		map.put("personnelPhone", reportApprovalJimu.getPersonnelPhone());

		try {
			generateExcel(title, data, response, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void generateExcel(String title, List<ReportDailyV> list, HttpServletResponse response, Map<String,String> map) throws Exception {
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
		XSSFCellStyle boldStyle = ExcelUtil.genBoldContextStyle(workbook);//创建加粗格式


		//创建Excel
		//生成基本数据
		genExcel(title, list, sheet, titleStyle, contextStyle, contextRightStyle, map, boldStyle);


		//将工作薄写入文件输出流中
		workbook.write(bos);
	}

	private static void genExcel(String title, List<ReportDailyV> list, XSSFSheet sheet, XSSFCellStyle titleStyle, XSSFCellStyle contextStyle,
								 XSSFCellStyle contextRightStyle, Map<String, String> map, XSSFCellStyle boldStyle) {
		//设置宽度
		for (int i = 0; i < 10; i++) {
			if(i == 2){
				sheet.setColumnWidth(i,2000);
			} else if(i == 3){
				sheet.setColumnWidth(i,1000);
			}else if(i == 11){
				sheet.setColumnWidth(i,4000);
			}else {
				sheet.setColumnWidth(i, 3000);
			}
		}

		//合并和设置标题位置
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 24));
		XSSFRow row = sheet.createRow(0);//创建第一行，为标题，index从0开始
		row.setHeight((short)1000);
		ExcelUtil.setCell(0, title, row, titleStyle);

		//======合并操作=======
		//合并前六列中的2，3行表头
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 5, 5));
		//合并原煤表头
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 12));
		//合并精煤表头
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 13, 19));
		//合并商品煤表头
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 20, 21));
		//合并累计库存
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 22, 22));
		//合并填表人
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 23, 23));
		//合并联系方式
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 24, 24));

		//创建表头
		row = sheet.createRow(1);//创建第二行
		ExcelUtil.setCell(0, "集团", row, contextStyle);
		ExcelUtil.setCell(1, "子公司", row, contextStyle);
		ExcelUtil.setCell(2, "省内外", row, contextStyle);
		ExcelUtil.setCell(3, "序号", row, contextStyle);
		ExcelUtil.setCell(4, "矿场名称", row, contextStyle);
		ExcelUtil.setCell(5, "值班领导", row, contextStyle);
		//原煤表头6-12
		ExcelUtil.setCell(6, "原煤", row, contextStyle);

		ExcelUtil.setCell(7, "", row, contextStyle);
		ExcelUtil.setCell(8, "", row, contextStyle);
		ExcelUtil.setCell(9, "", row, contextStyle);
		ExcelUtil.setCell(10, "", row, contextStyle);
		ExcelUtil.setCell(11, "", row, contextStyle);
		ExcelUtil.setCell(12, "", row, contextStyle);

		//精煤表头13-19
		ExcelUtil.setCell(13, "精煤", row, contextStyle);


		ExcelUtil.setCell(14, "", row, contextStyle);  //设置空值，否则表格无边线
		ExcelUtil.setCell(15, "", row, contextStyle);
		ExcelUtil.setCell(16, "", row, contextStyle);
		ExcelUtil.setCell(17, "", row, contextStyle);
		ExcelUtil.setCell(18, "", row, contextStyle);
		ExcelUtil.setCell(19, "", row, contextStyle);
		ExcelUtil.setCell(20, "", row, contextStyle);

		//商品煤表头20-21
		ExcelUtil.setCell(20, "商品煤", row, contextStyle);

		//累计库存
		ExcelUtil.setCell(22, "累计库存", row, contextStyle);

		//填表人，审核人。
		ExcelUtil.setCell(23, "填表人/审核人", row, contextStyle);
		//联系方式
		ExcelUtil.setCell(24, "联系方式", row, contextStyle);



		//日计划等表头
		row = sheet.createRow(2); //创建第三行
		ExcelUtil.setCell(0, "", row, contextStyle);   //设置空值，否则表格无边线
		ExcelUtil.setCell(1, "", row, contextStyle);
		ExcelUtil.setCell(2, "", row, contextStyle);
		ExcelUtil.setCell(3, "", row, contextStyle);
		ExcelUtil.setCell(4, "", row, contextStyle);
		ExcelUtil.setCell(5, "", row, contextStyle);
		ExcelUtil.setCell(6, "日计划", row, contextStyle);
		ExcelUtil.setCell(7, "日完成", row, contextStyle);
		ExcelUtil.setCell(8, "增减", row, contextStyle);
		ExcelUtil.setCell(9, "月计划", row, contextStyle);
		ExcelUtil.setCell(10, "年累计计划", row, contextStyle);
		ExcelUtil.setCell(11, "年累计完成", row, contextStyle);
		ExcelUtil.setCell(12, "增减", row, contextStyle);
		ExcelUtil.setCell(13, "日计划", row, contextStyle);
		ExcelUtil.setCell(14, "日完成", row, contextStyle);
		ExcelUtil.setCell(15, "增减", row, contextStyle);
		ExcelUtil.setCell(16, "月计划", row, contextStyle);
		ExcelUtil.setCell(17, "年累计计划", row, contextStyle);
		ExcelUtil.setCell(18, "年累计完成", row, contextStyle);
		ExcelUtil.setCell(19, "增减", row, contextStyle);
		ExcelUtil.setCell(20, "日销量", row, contextStyle);
		ExcelUtil.setCell(21, "年累计销量", row, contextStyle);



		int sortOrder = 1;
		int rowNum = 3;  //从第四行开始填充数据
		//分公司合并单元格
		int startRow = 6;
		int endRow = 3;  //合并截止到行
		//省内外合并单元格初始化
		int startRow2 = 6;
		int endRow2 = 3;

		XSSFCellStyle tempStyle = contextStyle;

		for (int i = 0; i < list.size(); i++) {
			ReportDailyV data = list.get(i);

			String provinceTag = data.getProvinceTag();
			if("0".equals(data.getProvinceTag())){
				provinceTag = "省内";
			}else if("1".equals(data.getProvinceTag())){
				provinceTag = "省外";
			}

			//合计设置加粗
			if("合计".equals(data.getParentCompName())
					|| "合计".equals(provinceTag) && endRow2 - startRow2 > 1){
				contextStyle = boldStyle;
			}

			row = sheet.createRow(rowNum++);
			ExcelUtil.setCell(0, data.getHeadquarter(),row,contextStyle);
			ExcelUtil.setCell(1, data.getParentCompName(), row, contextStyle);


			ExcelUtil.setCell(2, provinceTag, row, contextStyle);
			if(StrUtil.isNotBlank(data.getCompName())){
				ExcelUtil.setCell(3, sortOrder, row, contextStyle);
				sortOrder ++;
			}else{
				ExcelUtil.setCell(3, "", row, contextStyle);
			}
			ExcelUtil.setCell(4, data.getCompName(), row, contextStyle);
			ExcelUtil.setCell(5, data.getLeaderName(), row, contextStyle);
			ExcelUtil.setCell(6, data.getProductionPlan(), row, contextStyle);
			ExcelUtil.setCell(7, data.getActualProduction(), row, contextStyle);

			Double actual = data.getActualProduction() == null ? 0 : data.getActualProduction();
			Double plan = data.getProductionPlan() == null ? 0 : data.getProductionPlan();
			ExcelUtil.setCell(8, actual - plan, row, contextStyle);

			ExcelUtil.setCell(9, data.getParentPlan(), row, contextStyle);
			ExcelUtil.setCell(10, data.getTotalProductionPlan(), row, contextStyle);
			ExcelUtil.setCell(11, data.getTotalActualProduction(), row, contextStyle);

			Double actual2 = data.getTotalActualProduction() == null ? 0 : data.getTotalActualProduction();
			Double plan2 = data.getTotalProductionPlan() == null ? 0 : data.getTotalProductionPlan();
			ExcelUtil.setCell(12, actual2 - plan2, row, contextStyle);

			ExcelUtil.setCell(13, data.getSumProductionPlan(), row, contextStyle);
			ExcelUtil.setCell(14, data.getSumActualProduction(), row, contextStyle);

			Double actual3 = data.getSumActualProduction() == null ? 0 : data.getSumActualProduction();
			Double plan3 = data.getSumProductionPlan() == null ? 0 : data.getSumProductionPlan();
			ExcelUtil.setCell(15, actual3 - plan3, row, contextStyle);

			ExcelUtil.setCell(16, data.getSumParentPlan(), row, contextStyle);
			ExcelUtil.setCell(17, data.getSumTotalProductionPlan(), row, contextStyle);
			ExcelUtil.setCell(18, data.getSumTotalActualProduction(), row, contextStyle);

			Double actual4 = data.getSumTotalActualProduction() == null ? 0 : data.getSumTotalActualProduction();
			Double plan4 = data.getSumProductionPlan() == null ? 0 : data.getSumTotalProductionPlan();
			ExcelUtil.setCell(19, actual4 - plan4, row, contextStyle);

			ExcelUtil.setCell(20, data.getProductSale(), row, contextStyle);
			ExcelUtil.setCell(21, data.getTotalProductSale(), row, contextStyle);
			ExcelUtil.setCell(22, data.getStockOfDate(), row, contextStyle);
			ExcelUtil.setCell(23, data.getPersonnelName(), row, contextStyle);
			ExcelUtil.setCell(24, data.getPersonnelPhone(), row, contextStyle);

			//先合并分公司行
			if("合计".equals(data.getParentCompName())){
				sheet.addMergedRegion(new CellRangeAddress(startRow, endRow - 1, 1, 1));  //此时endRow下标是"合计"行, "合计"不需要合并单元格
				startRow = endRow + 1;
				//分公司合计，则省内外起始行下移一行
				startRow2 ++;
				//合计不需要设置序号
				ExcelUtil.setCell(3, "", row, contextStyle);
			}
			//合并省内外
			if("合计".equals(provinceTag) && endRow2 - startRow2 > 1){
				sheet.addMergedRegion(new CellRangeAddress(startRow2, endRow2 - 1, 2, 2));  //此时endRow2下标是"合计"行, "合计"不需要合并单元格
				startRow2 = endRow2 + 1;
				//合计不需要设置序号
				ExcelUtil.setCell(3, "", row, contextStyle);
			}

			endRow ++;
			endRow2 ++;

			//加粗后复原原样式
			contextStyle = tempStyle;

		}

		//第一列“集团公司”合并单元格
		if(rowNum - 1 > 6){
			sheet.addMergedRegion(new CellRangeAddress(6, rowNum - 1, 0, 0));
		}

		//未完成计划说明
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 1));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 24));
		row = sheet.createRow(rowNum++);
		row.setHeight((short)1500);  //设置高度
		ExcelUtil.setCell(0, "未完成计划说明", row, contextStyle);
		ExcelUtil.setCell(2, map.get("remark"), row, contextRightStyle);

		//填表人等数据
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 1));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 4));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 6, 7));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 9, 10));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 12, 13));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 14, 24));
		row = sheet.createRow(rowNum++);
		ExcelUtil.setCell(0, "填表人", row, contextStyle);
		ExcelUtil.setCell(1, "", row, contextStyle);
		ExcelUtil.setCell(2, map.get("submitName"), row, contextStyle);
		ExcelUtil.setCell(3, "", row, contextStyle);
		ExcelUtil.setCell(4, "", row, contextStyle);
		ExcelUtil.setCell(5, "联系方式", row, contextStyle);
		ExcelUtil.setCell(6, map.get("submitPhone"), row, contextStyle);
		ExcelUtil.setCell(7, "", row, contextStyle);
		ExcelUtil.setCell(8, "审核人", row, contextStyle);
		ExcelUtil.setCell(9, map.get("personnelName"), row, contextStyle);
		ExcelUtil.setCell(10, "", row, contextStyle);
		ExcelUtil.setCell(11, "联系方式", row, contextStyle);
		ExcelUtil.setCell(12, map.get("personnelPhone"), row, contextStyle);
		ExcelUtil.setCell(13, "", row, contextStyle);
		ExcelUtil.setCell(14, "", row, contextStyle);
		ExcelUtil.setCell(15, "", row, contextStyle);
		ExcelUtil.setCell(16, "", row, contextStyle);
		ExcelUtil.setCell(17, "", row, contextStyle);
		ExcelUtil.setCell(18, "", row, contextStyle);
		ExcelUtil.setCell(19, "", row, contextStyle);
		ExcelUtil.setCell(20, "", row, contextStyle);
		ExcelUtil.setCell(21, "", row, contextStyle);
		ExcelUtil.setCell(22, "", row, contextStyle);
		ExcelUtil.setCell(23, "", row, contextStyle);
		ExcelUtil.setCell(24, "", row, contextStyle);


	}

