package com.by4cloud.platform.scdd.util;

import java.math.BigDecimal;

/**
 * @author xfei
 * @date 2022/5/27下午8:26
 */
public class NumUtils {
	/**
	 * 精确小数
	 * @param f 小数
	 * @param length 精确位数
	 * @return
	 */
	public static Float accurateFloat(Float f,int length){
		BigDecimal b = new BigDecimal(f.toString());
		f = b.setScale(length,BigDecimal.ROUND_HALF_UP).floatValue();
		return f;
	}
	public static Float accurateFloat(Float f){
		return accurateFloat(f,3);
	}

	public static Double accurateFloat(Double f,int length){
		BigDecimal b = new BigDecimal(f.toString());
		f = b.setScale(length,BigDecimal.ROUND_HALF_UP).doubleValue();
		return f;
	}
	public static Double accurateFloat(Double f){
		return accurateFloat(f,3);
	}


	public static Double accurateDouble(Double f,int length){
		BigDecimal b2 = new BigDecimal(f.toString());
		f = b2.setScale(length,BigDecimal.ROUND_HALF_UP).doubleValue();
		return f;
	}
	public static Double accurateDouble(Double f){
		return accurateDouble(f,3);
	}

	/**
	 * 加
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double addDouble(Double a,Double b){
		if (a==null)a=0d;
		if (b==null)b=0d;
		BigDecimal aa = new BigDecimal(a+"");
		BigDecimal bb = new BigDecimal(b+"");
		aa = aa.add(bb);
		aa = aa.setScale(2,BigDecimal.ROUND_HALF_UP);
		return aa.doubleValue();
	}

	/**
	 * 减
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double subtractDouble(Double a,Double b){
		if (a==null)a=0d;
		if (b==null)b=0d;
		BigDecimal aa = new BigDecimal(a+"");
		BigDecimal bb = new BigDecimal(b+"");
		aa = aa.subtract(bb);
		aa = aa.setScale(2,BigDecimal.ROUND_HALF_UP);
		return aa.doubleValue();
	}

	/**
	 * 乘
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double multiplyDouble(Double a,Double b){
		if (a==null)a=0d;
		if (b==null)b=0d;
		BigDecimal aa = new BigDecimal(a+"");
		BigDecimal bb = new BigDecimal(b+"");
		aa = aa.multiply(bb);
		aa = aa.setScale(2,BigDecimal.ROUND_HALF_UP);
		return aa.doubleValue();
	}
	/**
	 * 除
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double divideDouble(Double a,Double b){
		if (a==null)a=0d;
		if (b==null)b=0d;
		BigDecimal aa = new BigDecimal(a+"");
		BigDecimal bb = new BigDecimal(b+"");
		aa = aa.divide(bb);
		aa = aa.setScale(2,BigDecimal.ROUND_HALF_UP);
		return aa.doubleValue();
	}
	/**
	 * 加
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double addDouble(Double a,Double b,int len){
		if (a==null)a=0d;
		if (b==null)b=0d;
		BigDecimal aa = new BigDecimal(a+"");
		BigDecimal bb = new BigDecimal(b+"");
		aa = aa.add(bb);
		aa = aa.setScale(len,BigDecimal.ROUND_HALF_UP);
		return aa.doubleValue();
	}

	/**
	 * 减
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double subtractDouble(Double a,Double b,int len){
		if (a==null)a=0d;
		if (b==null)b=0d;
		BigDecimal aa = new BigDecimal(a+"");
		BigDecimal bb = new BigDecimal(b+"");
		aa = aa.subtract(bb);
		aa = aa.setScale(len,BigDecimal.ROUND_HALF_UP);
		return aa.doubleValue();
	}

	/**
	 * 乘
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double multiplyDouble(Double a,Double b,int len){
		if (a==null)a=0d;
		if (b==null)b=0d;
		BigDecimal aa = new BigDecimal(a+"");
		BigDecimal bb = new BigDecimal(b+"");
		aa = aa.multiply(bb);
		aa = aa.setScale(len,BigDecimal.ROUND_HALF_UP);
		return aa.doubleValue();
	}
	/**
	 * 除
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double divideDouble(Double a,Double b,int len){
		if (a==null)a=0d;
		if (b==null)b=0d;
		BigDecimal aa = new BigDecimal(a+"");
		BigDecimal bb = new BigDecimal(b+"");
		aa = aa.divide(bb,len, BigDecimal.ROUND_HALF_UP);
		return aa.doubleValue();
	}



	public static Double floatToD(Float f){
		if (f==null){
			return new Double(0);
		}
		return new Double(f);
	}

}
