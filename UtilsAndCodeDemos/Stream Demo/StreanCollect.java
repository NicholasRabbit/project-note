
//使用Stream获取集合中元素的某一个字段的集合
public void collect(){
	List<YySetting> settingList = ;
	List<Long> filedIdList = settingList.stream().map(YySetting::getFiledId).collect(Collectors.toList());
}