

/*
 * 复杂格式的表格，合并单元格，纵向合并等，具体结合Apache文档理解
 * */

	//导出接口
	@ApiOperation(value = "生成excel", notes = "生成excel")
	@Inner(false)
	@GetMapping("/createExcel")
	public void createExcel(Page<Project> page, Project project, HttpServletResponse response) {
		page.setSize(1000l);
		SettingOpen settingOpen = settingOpenService.getById(1);
		String serialNum = settingOpen.getSerialNum();
		if (StrUtil.isNotEmpty(project.getSerialNum())) {
			serialNum = project.getSerialNum();
		}
		Map<String, String> levelMap = remoteDataScopeService.getDictMapByType("project_level");
		Map<String, List<Project>> resMap = new HashMap<>();
		for (String key : levelMap.keySet()) {
			Page<Project> p = projectService.page(page, Wrappers.query(project).lambda()
					.eq(Project::getSerialNum, serialNum)
					.like(Project::getSerialNum, serialNum + "%")
					.ne(Project::getResult, 0)
					.ne(Project::getResult, 3)
					.eq(Project::getMainProjectId, 0)
					.eq(Project::getProjectLevel, key)
					.orderByAsc(Project::getProjectNum));
			p.setRecords(p.getRecords().stream()
					.sorted(Comparator.comparing(Project::getSort1).thenComparing(Project::getSort2))
					.collect(Collectors.toList()));
			p.getRecords().forEach(e -> {
				List<Project> child = projectService.list(new QueryWrapper<Project>().lambda().eq(Project::getMainProjectId, e.getId()));
				e.setChildList(child);
			});
			if (p.getRecords() != null && p.getRecords().size() > 0) {
				resMap.put(key, p.getRecords());
			}

		}

		String title = "冀中能源峰峰集团" + serialNum.split("#")[0] + "年科技创新计划项目申报汇总表";
		try {
			generateExcel(title, resMap, response);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void generateExcel(String title, Map<String, List<Project>> resMap, HttpServletResponse response) throws Exception {
		//设置响应体格式
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
		XSSFCellStyle titleStyle2 = ExcelUtil.genTitleStyle2(workbook);//带背景的小标题
		XSSFCellStyle contextStyle = ExcelUtil.genContextStyle(workbook);//创建文本样式
		XSSFCellStyle contextleftStyle = ExcelUtil.genContextStyleLeft(workbook);//创建文本居右样式


		//创建Excel
		genExcel(title, resMap, sheet, titleStyle, contextStyle, contextleftStyle, titleStyle2);


		//将工作薄写入文件输出流中
		workbook.write(bos);
	}

	private static void genExcel(String title, Map<String, List<Project>> map, XSSFSheet sheet, XSSFCellStyle titleStyle, XSSFCellStyle contextStyle, XSSFCellStyle contextleftStyle, XSSFCellStyle titleStyle2) {
		for (int i = 0; i < 11; i++) {
			if (i == 0) {
				sheet.setColumnWidth(i, 1000);
			} else if (i == 2 || i == 3 || i == 6 || i == 7 || i == 10) {
				sheet.setColumnWidth(i, 10000);
			} else if (i == 4) {
				sheet.setColumnWidth(i, 12000);
			} else if (i == 8) {
				sheet.setColumnWidth(i, 8000);
			} else {
				sheet.setColumnWidth(i, 3000);
			}

		}

		//设置标题位置
		sheet.addMergedRegion(new CellRangeAddress(
				0, //first row
				0, //last row
				0, //first column
				10 //last column
		));
		XSSFRow row = sheet.createRow(0);//项目类别标题
		ExcelUtil.setCell(0, title, row, titleStyle);

		row = sheet.createRow(1);//创建第二行
		ExcelUtil.setCell(0, "序号", row, contextStyle);
		ExcelUtil.setCell(1, "专业", row, contextStyle);
		ExcelUtil.setCell(2, "申报单位", row, contextStyle);
		ExcelUtil.setCell(3, "项目名称", row, contextStyle);
		ExcelUtil.setCell(4, "主要研究内容（精简200字内）", row, contextStyle);
		ExcelUtil.setCell(5, "负责人", row, contextStyle);
		ExcelUtil.setCell(6, "承担单位", row, contextStyle);
		ExcelUtil.setCell(7, "合作单位", row, contextStyle);
		ExcelUtil.setCell(8, "研究时间", row, contextStyle);
		ExcelUtil.setCell(9, "经费预算", row, contextStyle);
		ExcelUtil.setCell(10, "联系人及电话", row, contextStyle);


		int rowNum = 2;
		int seq = 1;
		for (String level : map.keySet()) {
			sheet.addMergedRegion(new CellRangeAddress(
					rowNum, //first row
					rowNum, //last row
					0, //first column
					10 //last column
			));
			row = sheet.createRow(rowNum++);
			ExcelUtil.setCell(0, level, row, titleStyle2);
			for (int i=1;i<11;i++){
				ExcelUtil.setCell(i, "", row, titleStyle2);
			}
			for (Project p : map.get(level)) {
				row = sheet.createRow(rowNum++);
				ExcelUtil.setCell(0, seq++, row, contextStyle);
				ExcelUtil.setCell(1, p.getMajor(), row, contextStyle);
				ExcelUtil.setCell(3, p.getProjectName(), row, contextleftStyle);
				ExcelUtil.setCell(2, p.getCompName(), row, contextStyle);
				ExcelUtil.setCell(4, p.getResearchContent(), row, contextleftStyle);
				ExcelUtil.setCell(5, p.getResponsibilityPerson(), row, contextStyle);
				ExcelUtil.setCell(6, p.getBearComp(), row, contextStyle);
				ExcelUtil.setCell(7, p.getCooperativeInstitutions(), row, contextStyle);
				String date = p.getStartDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + "～" + p.getEndDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
				ExcelUtil.setCell(8, date, row, contextStyle);
				ExcelUtil.setCell(9, p.getBudget(), row, contextStyle);
				ExcelUtil.setCell(10, p.getContacts() + "/" + p.getContactsPhone(), row, contextStyle);
				if (p.getChildList() != null) {
					for (Project c : p.getChildList()) {
						row = sheet.createRow(rowNum++);
						ExcelUtil.setCell(0, seq++, row, contextStyle);
						ExcelUtil.setCell(1, c.getMajor(), row, contextStyle);
						ExcelUtil.setCell(3, "(子)" +c.getProjectName(), row, contextleftStyle);
						ExcelUtil.setCell(2,  c.getCompName(), row, contextStyle);
						ExcelUtil.setCell(4, c.getResearchContent(), row, contextleftStyle);
						ExcelUtil.setCell(5, c.getResponsibilityPerson(), row, contextStyle);
						ExcelUtil.setCell(6, c.getBearComp(), row, contextStyle);
						ExcelUtil.setCell(7, c.getCooperativeInstitutions(), row, contextStyle);
						date = c.getStartDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + "～" + c.getEndDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
						ExcelUtil.setCell(8, date, row, contextStyle);
						ExcelUtil.setCell(9, c.getBudget(), row, contextStyle);
						ExcelUtil.setCell(10, c.getContacts() + "/" + c.getContactsPhone(), row, contextStyle);
					}

				}
			}
		}
	}

