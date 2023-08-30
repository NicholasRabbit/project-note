package com.by4cloud.platform.scdd.constant;

public enum CategoryCodeEnum {

	原煤("0205"),精煤("0202"),商品煤("0204");

	private String value;

	CategoryCodeEnum(String value){
		this.value = value;
	}

	public String getValue(){
		return value;
	}


}
