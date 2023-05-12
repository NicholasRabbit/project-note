
//使用Stream去重
public void distinct(){
	List<String> carNOList01 = ;
	List<String> carNOList02 = ;
	List<String> allCarList = Stream.concat(carNOList01.stream(),carNOList02.stream())
                .distinct().collect(Collectors.toList());
}
