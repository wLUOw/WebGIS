package com.njnu.picshare.util;

/**
 * @Authod oruizn
 * @date 2021年11月2021/11/30 0030日下午 20:41
 * 用于地理信息转换的工具类
 */
public class GeoProcessUtil {
    /**
     * 将图片中以度分秒形式表达的经纬度转换为十进制经纬度
     * @param DMS：即Degree, Minute, Second
     * @return
     */
    public static Double tranformDMSToDegree(String DMS){
        //-----------编码练习部分·开始---------------
        try {
            String[] lntArr = DMS.trim()
                    .replace("°", ";")
                    .replace("′", ";")
                    .replace("'", ";")
                    .replace("\"", "")
                    .split(";");
            Double result = 0D;
            for (int i = lntArr.length; i >0 ; i--) {
                double v = Double.parseDouble(lntArr[i-1]);
                if(i==1){
                    result=v+result;
                }else{
                    result=(result+v)/60;
                }
            }
            return NumberUtil.setNumberScale(result, 6);
        }catch (NullPointerException e){
            return null;
        }
        //-----------编码练习部分·结束---------------
    }

    /**
     * 将经纬度转换为PostgreSQL能够存储的空间点信息
     * @param longitude
     * @param latitude
     * @return
     */
    public static String GeometryToString(double longitude, double latitude){
        String geoStr = "POINT" + "(" + longitude + " " + latitude + ")";
        return geoStr;
    }
}
