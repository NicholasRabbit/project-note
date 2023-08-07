package com.by4cloud.platform.wrzs.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

/**
 * 无人值守处理车牌号等工具类
 * */
public class WrzsStringUtil {


    /**
     * 格式化车牌号，去除null，多个逗号
     * @param carNums
     * @return
     */
    public static String formatString(String carNums){
        List<String> carNumList = getCarNumAsList(carNums);
        if(carNumList == null || carNumList.size() == 0)
            return null;
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < carNumList.size(); i ++){
            if(StrUtil.isEmpty(carNumList.get(i)))
                continue;
            buffer.append(carNumList.get(i));
            if(i < carNumList.size() - 1)
                buffer.append(",");
        }
        return buffer.toString();
    }


    /**
     * 替换所有符号为英文逗号
     * @param srcCarNums 源车牌号
     * @return 新车牌号
     */
    public static String replaceAllPunctuationByComma(String srcCarNums){
        //车牌号替换所有符号为英文逗号,清除空格，换行符，制表位等
        if(StrUtil.isEmpty(srcCarNums))
            return srcCarNums;
        srcCarNums =  replaceAllBlanks(srcCarNums);
        return srcCarNums.replaceAll("\\p{P}", ",");

    }


    /**
     * 两个车牌号合并去重
     * @param carNums1
     * @param carNums2
     * @return
     */
    public static String distinctCarNums(String carNums1,String carNums2){
        if(StrUtil.isEmpty(carNums1))
            return carNums2 == null ? "" : carNums2;
        else if(StrUtil.isEmpty(carNums2))
            return carNums1 == null ? "" : carNums1;
        String[] carNumsArray1 = carNums1.split(",");
        String[] carNumsArray2 = carNums2.split(",");

        List<String> allCarList = Stream.concat(Arrays.asList(carNumsArray1).stream(), Arrays.asList(carNumsArray2).stream())
                .distinct().collect(Collectors.toList());
        StringBuffer buffer = new StringBuffer();
        for(String str : allCarList){
            if(StrUtil.isEmpty(str) || "null".equals(str))
                continue;
            buffer.append(str);
            buffer.append(",");
        }
        return StrUtil.removeSuffix(buffer.toString(),",");
    }

    /**
     * 去除所有的空格，\t\n\r等无字符
     * @param srcString 原字符串
     * @return
     */
    public static String replaceAllBlanks(String srcString){
        if(StrUtil.isEmpty(srcString))
            return srcString;
        return srcString.replaceAll("\\s*","");  //去除所有空格，\t\n。

    }

    /**
     * 去除所有的null
     * @param srcString 原字符串
     * @return
     */
    public static String removeNull(String srcString){
        if(StrUtil.isEmpty(srcString))
            return srcString;
        return srcString.replaceAll("null","");
    }


    /**
     * 车牌号判断
     * @param carNo
     * @return
     */
    public static boolean isCarNo(String carNo){
        String pattern = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
        return Pattern.matches(pattern, carNo);

    }

	//逗号拼接车牌号转为数组
    public static List<String> getCarNumAsList(String carNos){
        if(StrUtil.isEmpty(carNos))
            return new ArrayList<>();
        carNos = replaceAllPunctuationByComma(carNos);
        String[] strings = removeNull(carNos).split(",");
        return Arrays.asList(strings);
    }

    //获取字符串首字母
    public static String getPinYinHeadChar(String str) {
        if (isNull(str)) {
            return "";
        }
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            // 提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            }
            else {
                convert += word;
            }
        }

        convert = replaceAllBlanks(convert);
        return convert.toUpperCase();
    }


}
