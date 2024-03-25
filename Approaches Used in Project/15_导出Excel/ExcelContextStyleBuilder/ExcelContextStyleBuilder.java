package com.by4cloud.platform.yunxiao.utils;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;

public class ExcelContextStyleBuilder{

	private XSSFCellStyle style;

	private ExcelContextStyle(Builder builder){
		this.style = builder.style;
	}

	public static class Builder{

		private XSSFWorkbook workbook;
		private XSSFCellStyle style;

		private HorizontalAlignment alignment;
		private VerticalAlignment verticalAlignment;
		private boolean bold;
		private boolean border;

		public Builder (XSSFWorkbook workbook){
			this.workbook = workbook;
			this.style = workbook.createCellStyle();
		}

		public Builder setAlignment(HorizontalAlignment alignment){
			this.style.setAlignment(alignment);
			return this;
		}

		public Builder setVerticalAlignment(VerticalAlignment verticalAlignment) {
			this.style.setVerticalAlignment(verticalAlignment);
			return this;
		}

		public Builder setBold(boolean bold) {
			XSSFFont font = workbook.createFont();
			font.setBold(bold);
			this.style.setFont(font);
			return this;
		}

		public Builder setBorder(boolean border){
			if(border){
				//set text color
				this.style.setBorderBottom(BorderStyle.THIN);//设置文本边框
				this.style.setBorderLeft(BorderStyle.THIN);
				this.style.setBorderRight(BorderStyle.THIN);
				this.style.setBorderTop(BorderStyle.THIN);
				//set border color
				this.style.setTopBorderColor(new XSSFColor(Color.BLACK));//设置文本边框颜色
				this.style.setBottomBorderColor(new XSSFColor(Color.BLACK));
				this.style.setLeftBorderColor(new XSSFColor(Color.BLACK));
				this.style.setRightBorderColor(new XSSFColor(Color.BLACK));
			}
			return this;
		}

		//设置数字格式"0.00"
		public Builder setDataFormat(String fmt){
			this.style.setDataFormat(workbook.createDataFormat().getFormat(fmt));
			return this;
		}

		public XSSFCellStyle build(){
			return new ExcelContextStyle(this).style;
		}
	}

}
