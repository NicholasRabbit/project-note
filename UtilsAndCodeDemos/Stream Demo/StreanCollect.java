
//ʹ��Stream��ȡ������Ԫ�ص�ĳһ���ֶεļ���
public void collect(){
	List<YySetting> settingList = ;
	List<Long> filedIdList = settingList.stream().map(YySetting::getFiledId).collect(Collectors.toList());
}