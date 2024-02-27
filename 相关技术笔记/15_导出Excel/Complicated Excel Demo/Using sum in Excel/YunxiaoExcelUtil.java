package com.by4cloud.platform.yunxiao.utils;

import cn.hutool.core.date.DateUtil;
import com.by4cloud.platform.yunxiao.entity.Voucher;
import com.by4cloud.platform.yunxiao.vo.VoucherAndPonderationVo;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YunxiaoExcelUtil{



	//销售汇总表(煤种)，生成Excel
	public static void generateExcel(String title, List<VoucherAndPonderationVo> list, HttpServletResponse response) throws Exception {

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("content-disposition", "attachement;filename=" + new String((title + ".xls").getBytes("gb2312"), "ISO8859-1"));
		ServletOutputStream os = null;
		os = response.getOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(os);
		//创建工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		//创建表单
		XSSFSheet sheet = ExcelUtilStatic.genSheet(workbook, "excel");
		//创建表单样式
		XSSFCellStyle titleStyle = ExcelUtilStatic.genTitleStyle(workbook);//创建标题样式
		XSSFCellStyle contextStyle = ExcelUtilStatic.genContextStyle(workbook);//创建文本样式
		XSSFCellStyle contextRightStyle = ExcelUtilStatic.genContextNoBorderStyle(workbook);//创建文本居右样式

		//创建Excel
		genExcel(title, list, sheet, titleStyle, contextStyle, contextRightStyle);

		//将工作薄写入文件输出流中
		workbook.write(bos);
	}

	private static void genExcel(String title, List<VoucherAndPonderationVo> list, XSSFSheet sheet, XSSFCellStyle titleStyle, XSSFCellStyle contextStyle, XSSFCellStyle contextRightStyle) {
		//设置宽度
		for (int i = 0; i < 4; i++) {
			if(i == 1){
				sheet.setColumnWidth(i, 9000);
			}else{
				sheet.setColumnWidth(i, 3000);
			}

		}

		//设置标题位置
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		XSSFRow row = sheet.createRow(0);//创建第一行，为标题，index从0开始
		ExcelUtilStatic.setCell(0, title, row, titleStyle);

		row = sheet.createRow(1);//创建第二行
		ExcelUtilStatic.setCell(0, "煤种", row, contextStyle);
		ExcelUtilStatic.setCell(1, "收货单位", row, contextStyle);
		ExcelUtilStatic.setCell(2, "日期", row, contextStyle);
		ExcelUtilStatic.setCell(3, "取煤数量", row, contextStyle);
		ExcelUtilStatic.setCell(4, "矿别", row, contextStyle);

		int rowNum = 2;
		int startRow = 2;
		int endRow = 2;
		double totalNum = 0.0;
		boolean totalFlag = false;
		boolean lastRowFlag = false;
		for (int i = 0; i < list.size(); i++) {
			VoucherAndPonderationVo data = list.get(i);
			//合并单元格
			if(i > 1){
				if(!data.getCoalName().equals(list.get(i - 1).getCoalName()) && endRow - 1 > startRow){
					totalFlag = true;
				}else if(endRow - 1 > startRow && i == list.size() - 1){
					//走到最后一行后进行合并
					lastRowFlag = true;

				}
			}

			if(totalFlag){
				//增加合计行
				row = sheet.createRow(rowNum++);
				ExcelUtilStatic.setCell(0,list.get(i - 1).getCoalName(), row, contextStyle);
				ExcelUtilStatic.setCell(1,"合计",row, contextStyle);
				ExcelUtilStatic.setCell(3, NumUtils.accurateDouble(totalNum, 2) , row, contextStyle);
				//合并单元格
				sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 0, 0));
				startRow = endRow + 1;  //下一次的开始行跳过本次的跳过合计行
				totalNum = 0.0;
				totalFlag = false;
				endRow ++;
			}

			row = sheet.createRow(rowNum++);

			ExcelUtilStatic.setCell(0, data.getCoalName(), row, contextStyle);
			ExcelUtilStatic.setCell(1, data.getCustomerName() ,row, contextStyle);
			ExcelUtilStatic.setCell(2, DateUtil.format(data.getDate(), "yyyy-MM-dd") , row, contextStyle);
			ExcelUtilStatic.setCell(3, NumUtils.accurateDouble(data.getNum(), 2) , row, contextStyle);
			ExcelUtilStatic.setCell(4, data.getCompName(), row, contextStyle);

			totalNum += data.getNum();

			//最后一行增加合计数据
			if(lastRowFlag){
				//增加合计行
				row = sheet.createRow(rowNum++);
				ExcelUtilStatic.setCell(0,list.get(i - 1).getCoalName(), row, contextStyle);
				ExcelUtilStatic.setCell(1,"合计",row, contextStyle);
				ExcelUtilStatic.setCell(3, NumUtils.accurateDouble(totalNum, 2) , row, contextStyle);
				//合并单元格
				sheet.addMergedRegion(new CellRangeAddress(startRow, ++ endRow, 0, 0));
			}
			endRow ++;
		}

	}

	public static void generateExcelByCustomer(String title, List<VoucherAndPonderationVo> list, HttpServletResponse response) throws Exception {

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("content-disposition", "attachement;filename=" + new String((title + ".xls").getBytes("gb2312"), "ISO8859-1"));
		ServletOutputStream os = null;
		os = response.getOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(os);
		//创建工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		//创建表单
		XSSFSheet sheet = ExcelUtilStatic.genSheet(workbook, "excel");
		//创建表单样式
		XSSFCellStyle titleStyle = ExcelUtilStatic.genTitleStyle(workbook);//创建标题样式
		XSSFCellStyle contextStyle = ExcelUtilStatic.genContextStyle(workbook);//创建文本样式
		XSSFCellStyle contextRightStyle = ExcelUtilStatic.genContextNoBorderStyle(workbook);//创建文本居右样式

		//创建Excel
		genExcelByCustomer(title, list, sheet, titleStyle, contextStyle, contextRightStyle);

		//将工作薄写入文件输出流中
		workbook.write(bos);
	}

	private static void genExcelByCustomer(String title, List<VoucherAndPonderationVo> list, XSSFSheet sheet, XSSFCellStyle titleStyle, XSSFCellStyle contextStyle, XSSFCellStyle contextRightStyle) {
		//设置宽度
		for (int i = 0; i < 4; i++) {
			if(i == 0){
				sheet.setColumnWidth(i, 8000);
			}else{
				sheet.setColumnWidth(i, 4000);
			}

		}

		//设置标题位置
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		XSSFRow row = sheet.createRow(0);//创建第一行，为标题，index从0开始
		ExcelUtilStatic.setCell(0, title, row, titleStyle);

		row = sheet.createRow(1);//创建第二行
		ExcelUtilStatic.setCell(0, "收货单位", row, contextStyle);
		ExcelUtilStatic.setCell(1, "煤种", row, contextStyle);
		ExcelUtilStatic.setCell(2, "日期", row, contextStyle);
		ExcelUtilStatic.setCell(3, "取煤数量", row, contextStyle);
		ExcelUtilStatic.setCell(4, "矿别", row, contextStyle);

		int rowNum = 2;
		int startRow = 2;
		int endRow = 2;
		double totalNum = 0.0;
		boolean totalFlag = false;
		boolean lastRowFlag = false;
		for (int i = 0; i < list.size(); i++) {
			VoucherAndPonderationVo data = list.get(i);
			//合并单元格
			if(i > 1){
				if(!data.getCustomerName().equals(list.get(i - 1).getCustomerName()) && endRow - 1 > startRow){
					totalFlag = true;
				}else if(endRow - 1 > startRow && i == list.size() - 1){
					//走到最后一行后进行合并
					lastRowFlag = true;

				}
			}

			if(totalFlag){
				//增加合计行
				row = sheet.createRow(rowNum++);
				ExcelUtilStatic.setCell(0,list.get(i - 1).getCustomerName(), row, contextStyle);
				ExcelUtilStatic.setCell(1,"合计",row, contextStyle);
				ExcelUtilStatic.setCell(3, NumUtils.accurateDouble(totalNum, 2) , row, contextStyle);
				//合并单元格
				sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 0, 0));
				startRow = endRow + 1;  //下一次的开始行跳过本次的跳过合计行
				totalNum = 0.0;
				totalFlag = false;
				endRow ++;
			}

			row = sheet.createRow(rowNum++);

			ExcelUtilStatic.setCell(0, data.getCustomerName(), row, contextStyle);
			ExcelUtilStatic.setCell(1, data.getCoalName(), row, contextStyle);
			ExcelUtilStatic.setCell(2, DateUtil.format(data.getDate(), "yyyy-MM-dd") , row, contextStyle);
			ExcelUtilStatic.setCell(3, NumUtils.accurateDouble(data.getNum(), 2) , row, contextStyle);
			ExcelUtilStatic.setCell(4, data.getCompName(), row, contextStyle);

			totalNum += data.getNum();

			//最后一行增加合计数据
			if(lastRowFlag){
				//增加合计行
				row = sheet.createRow(rowNum++);
				ExcelUtilStatic.setCell(0,list.get(i - 1).getCoalName(), row, contextStyle);
				ExcelUtilStatic.setCell(1,"合计",row, contextStyle);
				ExcelUtilStatic.setCell(3, NumUtils.accurateDouble(totalNum, 2) , row, contextStyle);
				//合并单元格
				sheet.addMergedRegion(new CellRangeAddress(startRow, ++ endRow, 0, 0));
			}
			endRow ++;
		}

	}


	//路运生成表格
	public static void generateExcelByVoucher(String title, List<Voucher> list, HttpServletResponse response) throws Exception {

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("content-disposition", "attachement;filename=" + new String((title + ".xls").getBytes("gb2312"), "ISO8859-1"));
		ServletOutputStream os = null;
		os = response.getOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(os);
		//创建工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		//创建表单
		XSSFSheet sheet = ExcelUtilStatic.genSheet(workbook, "excel");
		//创建表单样式
		Map<String, XSSFCellStyle> styleMap = ExcelUtilStatic.getStyleMap(workbook);
		//创建Excel
		genExcelByVoucher(title, list, sheet, styleMap);

		//将工作薄写入文件输出流中
		workbook.write(bos);
	}

	private static void genExcelByVoucher(String title, List<Voucher> list, XSSFSheet sheet, Map<String, XSSFCellStyle> styleMap) {
		//设置宽度
		for (int i = 0; i < 10; i++) {
			if (i == 5 || i == 6) {
				sheet.setColumnWidth(i, 5000);
			} else if (i == 1) {
				sheet.setColumnWidth(i, 10000);
			} else {
				sheet.setColumnWidth(i, 2500);
			}

		}

		//设置标题位置
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
		XSSFRow row = sheet.createRow(0);//创建第一行，为标题，index从0开始
		ExcelUtilStatic.setCell(0, title, row, styleMap.get("title"));

		row = sheet.createRow(1);//创建第二行
		ExcelUtilStatic.setCell(0, "矿别", row, styleMap.get("header"));
		ExcelUtilStatic.setCell(1, "收货单位", row, styleMap.get("header"));
		ExcelUtilStatic.setCell(2, "到站", row, styleMap.get("header"));
		ExcelUtilStatic.setCell(3, "道别", row, styleMap.get("header"));
		ExcelUtilStatic.setCell(4, "煤种", row, styleMap.get("header"));
		ExcelUtilStatic.setCell(5, "开装时间", row, styleMap.get("header"));
		ExcelUtilStatic.setCell(6, "装完时间", row, styleMap.get("header"));
		ExcelUtilStatic.setCell(7, "净重", row, styleMap.get("header"));
		ExcelUtilStatic.setCell(8, "标准", row, styleMap.get("header"));
		ExcelUtilStatic.setCell(9, "车辆数", row, styleMap.get("header"));

		int rowNum = 2;
		int startRow = 2;
		int startRowCustomerName = 2;
		int endRow = 2;
		//总计数据
		double totalWeight = 0.0d;
		double totalWeighUp = 0.0d;
		int totalCarNum = 0;
		//合计收货单位数据
		double totalWeight2 = 0.0d;
		double totalWeighUp2 = 0.0d;
		int totalCarNum2 = 0;

		//合计数据Map，StringBuilder用于最后总计公式
		Map<String, StringBuilder> sumMap = new HashMap<>();   //每个矿别对应的客户合计公式，用于各矿别合计
		StringBuilder sb;
		Map<String, StringBuilder> compSumMap = new HashMap<>();  //每个矿合计数据公式，用于最后总计
		StringBuilder sb2;

		for (int i = 0; i < list.size(); i++) {
			Voucher data = list.get(i);
			Voucher formerRow = null;
			//从数据的第二条开始判断
			if (i > 2)
				formerRow = list.get(i - 1);
			//收货单位计算合计数据
			if (i > 2 && !data.getCustomerName().equals(formerRow.getCustomerName())) {
				//添加总计数据
				row = sheet.createRow(rowNum++);
				ExcelUtilStatic.setCell(2, "合计", row, styleMap.get("bold"));
				//合计各列数据
				for (int j = 0, k = 7; j < 3 && k < 10; j++, k++) {
					char starCell = (char) ('H' + j); //从H列开始，依次递增。ASCII Code, A + 1,2,3 = B,C,D...J,K
					char endCell = (char) ('H' + j);
					String ref = starCell + "" + (startRowCustomerName + 1) + ":" + endCell + (endRow);  //Excel行号从1开始
					if (k == 9)
						ExcelUtilStatic.setCellFormula(k, "SUM(" + ref + ")", row, styleMap.get("right"));  //最后一列总计
					else
						ExcelUtilStatic.setCellFormula(k, "SUM(" + ref + ")", row, styleMap.get("number:0.00"));
					//拼接所有收货单位合计数据在Excel中的位置，用于最后总计公式
					String col = String.valueOf(endCell).substring(0, 1);  //列名
					//原有列名则拿出来拼接，拼接最后总计的公式
					if (sumMap.containsKey(col)) {
						sb = sumMap.get(col);
						sb = sb.append(endCell + "" + (endRow + 1));   //Excel行号从1开始
					} else {
						//否则按列名新建
						sb = new StringBuilder(endCell + "" + (endRow + 1));
					}
					sb.append("+");
					sumMap.put(col, sb);

				}
				//合并相同收货单位
				sheet.addMergedRegion(new CellRangeAddress(startRowCustomerName, endRow, 1, 1));
				startRowCustomerName = endRow + 1;
				endRow++;
			}

			if (i > 2 && !data.getCompName().equals(formerRow.getCompName())) {
				//按每个矿别合计数据
				//第一列合并单元格
				sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 0, 0));
				//合并后增加合计数据
				row = sheet.createRow(rowNum++);
				ExcelUtilStatic.setCell(1, "合计", row, styleMap.get("bold"));
				for (int j = 0, k = 7; j < 3 && k < 10; j++, k++) {
					char col = (char) ('H' + j);
					String ref = sumMap.get(String.valueOf(col)).toString();  //取出公式
					ref = ref.substring(0, ref.length() - 1);  //去掉最后一个加号
					if (k == 9)
						ExcelUtilStatic.setCellFormula(k, "SUM(" + ref + ")", row, styleMap.get("right"));  //最后一列总计
					else
						ExcelUtilStatic.setCellFormula(k, "SUM(" + ref + ")", row, styleMap.get("number:0.00"));

					//把矿别总计所在位置原有列名则拿出来拼接，拼接最后总计的公式
					String colKey = String.valueOf(col).substring(0, 1);  //列名
					if (compSumMap.containsKey(colKey)) {
						sb2 = compSumMap.get(colKey);
						sb2 = sb2.append(col + "" + (endRow + 1));
					} else {
						//否则按列名新建
						sb2 = new StringBuilder(col + "" + (endRow + 1));
					}
					sb2.append("+");
					compSumMap.put(colKey, sb2);
				}

				//每个矿别获取各客户数据后，初始化公式map
				sumMap.clear();
				endRow++;
				startRow = endRow;
				startRowCustomerName += 1;   //收货单位起始行要跳过矿别合计这行
			}

			//遍历每条数据
			row = sheet.createRow(rowNum++);
			ExcelUtilStatic.setCell(0, data.getCompName(), row, styleMap.get("left"));
			ExcelUtilStatic.setCell(1, data.getCustomerName(), row, styleMap.get("left"));
			ExcelUtilStatic.setCell(2, data.getToStation(), row, styleMap.get("left"));
			ExcelUtilStatic.setCell(3, data.getTrack(), row, styleMap.get("left"));
			ExcelUtilStatic.setCell(4, data.getCoalName(), row, styleMap.get("left"));
			ExcelUtilStatic.setCell(5, DateUtil.format(data.getLoadStartTime(), "yyyy-MM-dd HH:mm:ss"), row, styleMap.get("left"));
			ExcelUtilStatic.setCell(6, DateUtil.format(data.getLoadEndTime(), "yyyy-MM-dd HH:mm:ss"), row, styleMap.get("left"));
			ExcelUtilStatic.setCell(7, data.getWeight(), row, styleMap.get("number:0.00"));
			ExcelUtilStatic.setCell(8, data.getAllWeighUp(), row, styleMap.get("number:0.00"));
			ExcelUtilStatic.setCell(9, data.getCarNum(), row, styleMap.get("right"));

			//第一列矿别到最后一行时，全列数据相同，则全部合并
			if (i == list.size() - 1) {
				sheet.addMergedRegion(new CellRangeAddress(startRow, endRow + 2, 0, 0));
			}

			endRow++;

			if (i == list.size() - 1) {
				//合并收货单位到最后一行
				sheet.addMergedRegion(new CellRangeAddress(startRowCustomerName, endRow, 1, 1));
				//添加总计数据
				row = sheet.createRow(rowNum++);
				ExcelUtilStatic.setCell(2, "合计", row, styleMap.get("bold"));
				//收货单位计算合计数据
				for (int j = 0, k = 7; j < 3 && k < 10; j++, k++) {
					char starCell = (char) ('H' + j);
					char endCell = (char) ('H' + j);
					String ref = starCell + "" + (startRowCustomerName + 1) + ":" + endCell + (endRow);
					if (k == 9)
						ExcelUtilStatic.setCellFormula(k, "SUM(" + ref + ")", row, styleMap.get("right"));  //最后一列总计
					else
						ExcelUtilStatic.setCellFormula(k, "SUM(" + ref + ")", row, styleMap.get("number:0.00"));
					//列名
					String col = String.valueOf(endCell).substring(0, 1);
					//原有列名则拿出来拼接
					if (sumMap.containsKey(col)) {
						sb = sumMap.get(col);
						sb = sb.append(endCell + "" + (endRow + 1));
					} else {
						//否则按列名新建
						sb = new StringBuilder(endCell + "" + (endRow + 1));
					}
					sb.append("+");
					sumMap.put(col, sb);

				}

				//最后一个矿按矿别增加合计数据
				row = sheet.createRow(rowNum++);
				ExcelUtilStatic.setCell(1, "合计", row, styleMap.get("bold"));
				for(int m = 2; m < 7; m++){
					ExcelUtilStatic.setCell(m, " ", row, styleMap.get("bold"));
				}
				for (int j = 0, k = 7; j < 3 && k < 10; j++, k++) {
					char col = (char) ('H' + j);
					String ref = sumMap.get(String.valueOf(col)).toString();  //取出公式
					ref = ref.substring(0, ref.length() - 1);  //去掉最后一个加号
					if (k == 9)
						ExcelUtilStatic.setCellFormula(k, "SUM(" + ref + ")", row, styleMap.get("right"));  //最后一列总计
					else
						ExcelUtilStatic.setCellFormula(k, "SUM(" + ref + ")", row, styleMap.get("number:0.00"));
				}
				sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 2, 6));

			}

		}


		//添加总计数据
		row = sheet.createRow(rowNum++);
		ExcelUtilStatic.setCell(0, "总计", row, styleMap.get("bold"));
		for(int m = 1; m < 7; m++){
			ExcelUtilStatic.setCell(m, " ", row, styleMap.get("bold"));
		}
		for (int j = 0, k = 7; j < 3 && k < 10; j++, k++) {
			char col = (char) ('H' + j);
			String ref = compSumMap.get(String.valueOf(col)).toString();  //取出公式
			ref = ref.substring(0, ref.length() - 1);  //去掉最后一个加号
			if (k == 9)
				ExcelUtilStatic.setCellFormula(k, "SUM(" + ref + ")", row, styleMap.get("right"));  //最后一列总计
			else
				ExcelUtilStatic.setCellFormula(k, "SUM(" + ref + ")", row, styleMap.get("number:0.00"));
		}
		//合并最后一行总计数据中间列。
		sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 1, 6));

	}


}
