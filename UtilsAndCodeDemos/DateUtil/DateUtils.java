package com.by4cloud.platform.scdd.util;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {

	public static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate asLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime asLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	//转换计划时间
	public static Date getPlanTime(String queryDate, String type){
		if(DateTypeConstants.YEAR.equals(type) && StrUtil.isNotEmpty(queryDate)){
			return DateUtil.parse(queryDate,"yyyy");
		}else if(DateTypeConstants.MONTH.equals(type) && StrUtil.isNotEmpty(queryDate)){
			return DateUtil.parse(queryDate,"yyyy-MM");
		}else if(DateTypeConstants.DAY.equals(type) && StrUtil.isNotEmpty(queryDate)){
			return DateUtil.parse(queryDate,"yyyy-MM-dd");
		}else if(DateTypeConstants.WEEK.equals(type) && StrUtil.isNotEmpty(queryDate)){
			return DateUtil.parse(queryDate,"yyyy");
		}
		return null;
	}

	//根据不同类型减去一个时间单位
	public static Date minusOneDate(String type,Date dateToAdd){
		switch (type) {
			case DateTypeConstants.YEAR:
				 return DateUtil.offset(dateToAdd, DateField.YEAR, -1);
			case DateTypeConstants.MONTH:
				return DateUtil.offsetMonth(dateToAdd, -1);
			case DateTypeConstants.DAY:
				return DateUtil.offsetDay(dateToAdd, -1);
			case DateTypeConstants.WEEK:
				return dateToAdd;
			default:
				return null;
		}
	}

	public static String getParentType(String type){
		if(DateTypeConstants.WEEK.equals(type)){
			return String.valueOf(Integer.valueOf(type) - 2);  //周改为月 -2
		}
		return String.valueOf(Integer.valueOf(type) - 1);
	}



	public static Date getMonthFromWeek(int week, int year) {
		// Create a calendar instance and set the week and year
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, week);
		cal.set(Calendar.YEAR, year);
		// Get the first day of the week as a date object
		return cal.getTime();

	}

	//判断最近的日期是否在同一个月，或者月是在同一年
	public static boolean isSameMonthOrYear(String type, Date dateToAdd,Date lastDate){
		if(DateTypeConstants.DAY.equals(type)){
			return DateUtil.beginOfMonth(dateToAdd).compareTo(DateUtil.beginOfMonth(lastDate)) == 0;
		}else if(DateTypeConstants.MONTH.equals(type)){
			return  DateUtil.beginOfYear(dateToAdd).compareTo(DateUtil.beginOfYear(lastDate)) == 0;
		}
		return false;
	}

	public static String getReallyType(String type){
		if(DateTypeConstants.WEEK.equals(type)){
			return String.valueOf(Integer.valueOf(type) + 2);  //周改为月 -2
		}
		return String.valueOf(Integer.valueOf(type) + 1);
	}

	//获取年，周获取该周周一日期
	public static Date beginDateOfWeek(Date year,Integer week){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(DateUtil.format(year,"yyyy"))); // 2023年
		cal.set(Calendar.WEEK_OF_YEAR, week); // 设置为2023年的第1周
		cal.set(Calendar.DAY_OF_WEEK, 2); // 1表示周日，2表示周一，7表示周六
		return cal.getTime();
	}

}
