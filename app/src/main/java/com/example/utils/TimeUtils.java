package com.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 高峰 on 2017/6/26.
 */

public class TimeUtils {
    //得到前一个zhou的时间
    public static Date getBeforeWeek() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, -7);    //得到前一zhou
        return calendar.getTime();

    }
    //得到现在的时间
    public static Date getPresentMonth() {

        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();

    }

    //按yyyyMMdd格式输出日期
    public static Date transform(String s) {
        Date d = null;

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
            d = simpleDateFormat.parse(s);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
    //按yyyy-MM-dd格式输出日期
    public static Date transform2(String s) {
        Date d = null;

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            d = simpleDateFormat.parse(s);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
    //按一定格式得到前一个月日期的输出
    public static  String getBeforeForm() {
        Date d = getBeforeWeek();
        String str = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        str = simpleDateFormat.format(d);
        return str;


    }

    //检测两个日期是否相等
    public static boolean sameDate(Date d1, Date d2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        //fmt.setTimeZone(new TimeZone()); // 如果需要设置时间区域，可以在这里设置
        return fmt.format(d1).equals(fmt.format(d2));
    }

    //转换起始日期格式
    public static String beginFormat(Date bTime){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String str = simpleDateFormat.format(bTime);
        return  str+"0点";
    }


    //转换结束日期格式
    public static String  endFormat(Date eTime){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String str = simpleDateFormat.format(eTime);
        return  str+"24点";

    }
    //显示内容日期格式
    public  static String disContent(String s){

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            Date d = sdf.parse(s);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
            String str = sdf2.format(d);
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    //按yyyyMMddHHmm输出
    public static String start_date(){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String str = sdf.format(getBeforeWeek());
            return str+"0000";
    }

    public static String end_date(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String str = sdf.format(date);
        return str;
    }

   //按照yyyyMMddHHmm输出指定字符串
   public static String toStart(String start) {

       String[] s = start.split("-");
       String str = "";
       for (int i = 0; i < s.length; i++) {

           str += s[i];
       }
       return str + "0000";
   }

    public static String  toEnd(String end){

        String[] s = end.split("-");
        String str = "";
        for (int i = 0; i < s.length; i++) {

            str += s[i];
        }
            return str+"2359";


    }



}
