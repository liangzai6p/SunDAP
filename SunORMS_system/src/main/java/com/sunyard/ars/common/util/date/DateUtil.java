package com.sunyard.ars.common.util.date;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    /** FORMATE_DATE   日期*/
    public static final String FORMATE_DATE = "yyyy-MM-dd";
    public static final String FORMATE_SECONDS = "HH:mm:ss";
    public static final String FORMATE_DATE_yyyyMMdd = "yyyyMMdd";
    public static final String FORMATE_FULL = FORMATE_DATE.concat(" ").concat(FORMATE_SECONDS);


    /**
     * 当前日期long
     * @param date
     * @return
     */
    public static long getLongTime(String date,String formate_type){
        DateTimeFormatter fmt = DateTimeFormat.forPattern(formate_type);
        return  DateTime.parse(date,fmt).getMillis();
    }

    /**
     * 格式化字符串获取系统日期
     * @param formate_type
     * @return
     */
    public static String getNewDate(String formate_type){
        return DateTime.now().toString(formate_type);
    }

    /**
     * 格式化字符串获取指定日期
     * @param date
     * @param formate_type
     * @return
     */
    public static String getDateFormate(String date,String formate_type){

        DateTimeFormatter fmt = DateTimeFormat.forPattern(formate_type);
        return DateTime.parse(date, fmt).toString();

    }


    public static void main(String[] args) {
        DateUtil.getDateFormate("20170119",FORMATE_DATE_yyyyMMdd);
        DateTimeFormatter format = DateTimeFormat .forPattern("yyyy-MM-dd HH:mm:ss");
       DateTime dateTime = DateTime.parse("2017-01-19 11:32:50", format);

    }

    /**
     * 判断闰月
     * @return
     */
    public static boolean checkLeapMonth(String date){
        DateTime dt4 = new DateTime(date);
        DateTime.Property month = dt4.monthOfYear();
        return month.isLeap();

    }


    /**
     * 判断闰年
     * @return
     */
    public static boolean checkLeapYear(String date){
        DateTime dt4 = new DateTime(date);
        DateTime.Property year = dt4.year();
        return year.isLeap();

    }



    /**
     * 2个日期间隔多少天
     * @param date
     * @param edate
     * @return
     */
    public static int dayBetween(String date,String edate){
        return Days.daysBetween(DateTime.parse(addDateRow(date)), DateTime.parse(addDateRow(edate))).getDays();
    }

    // 增加时间的"-"
    public static String addDateRow(String something) {
        if(null == something || "".equals(something)){
            return something;
        }
        if(something.indexOf("-") > -1){
            return something;
        }
        String dateTime = something.substring(0, 4) + "-"
                + something.substring(4, 6) + "-" + something.substring(6);

        return dateTime;
    }

    /**
     * 当前日期周一日期
     * @param type
     * @return
     */
    public static String firstDayOfWeek(String type){
        return DateTime.now().dayOfWeek().withMinimumValue().toString(type);
    }
    /**
     * 指定日期周一日期
     * @param date
     * @return
     */
    public static String firstDayOfWeek(String date,String type){
        return DateTime.parse(addDateRow(date)).dayOfWeek().withMinimumValue().toString(type);
    }


    /**
     * 当前日期周一日期
     * @param type
     * @return
     */
    public static String LastDayOfWeek(String type){
        return DateTime.now().dayOfWeek().withMaximumValue().toString(type);
    }
    /**
     * 指定日期周一日期
     * @param date
     * @return
     */
    public static String lastDayOfWeek(String date,String type){
        return DateTime.parse(addDateRow(date)).dayOfWeek().withMaximumValue().toString(type);
    }


    /**
     * 当前日期的月份第一天
     * @param type
     * @return
     */
    public static String firstDayOfMonth(String type){
        return DateTime.now().dayOfMonth().withMinimumValue().toString(type);
    }

    /**
     * 当前日期的月份第一天
     * @param type
     * @return
     */
    public static String firstDayOfMonth(String date,String type){
        return DateTime.parse(addDateRow(date)).dayOfMonth().withMinimumValue().toString(type);
    }


    /**
     * 当前日期的月份最后一天
     * @param type
     * @return
     */
    public static String lastDayOfMonth(String type){
        return DateTime.now().dayOfMonth().withMaximumValue().toString(type);
    }

    /**
     * 当前日期的月份最后一天
     * @param type
     * @return
     */
    public static String lastDayOfMonth(String date,String type){
        return DateTime.parse(addDateRow(date)).dayOfMonth().withMaximumValue().toString(type);
    }



    /**
     * 当前日期N天前日期
     *
     * @param days
     * @param type
     * @return
     */
    public static String minusBeforeDays(int days,String type){
        return DateTime.now().minusDays(days).toString(type);
    }
    /**
     * 指定日期N天前日期
     *
     * @param days
     * @param type
     * @return
     */
    public static String minusBeforeDays(String date,int days,String type){
        return DateTime.parse(addDateRow(date)).minusDays(days).toString(type);
    }

    /**
     * 当前日期N天后日期
     *
     * @param days
     * @param type
     * @return
     */
    public static String minusAfterDays(int days,String type){
        return DateTime.now().plusDays(days).toString(type);
    }
    /**
     * 指定日期N天后日期
     *
     * @param days
     * @param type
     * @return
     */
    public static String minusAfterDays(String date,int days,String type){
        return DateTime.parse(addDateRow(date)).plusDays(days).toString(type);
    }

    /**
     * 当前时间N小时后日期
     *
     * @param hours
     * @param type
     * @return
     */
    public static String minusAfterHours(int hours,String type){
        return 	DateTime.now().minusHours(hours).toString(type);
    }

    /**
     * 当前时间N分钟前后日期
     *
     * @param min
     * @param type
     * @return
     */
    public static String minusAfterMin(int min,String type){
        return 	DateTime.now().minusMinutes(min).toString(type);
    }


    /**
     * 当前时间N小时后日期
     *
     * @param hours
     * @param type
     * @return
     */
    public static String minusAfterHours(String time,int hours,String type){
        return DateTime.parse(addDateRow(time)).minusHours(hours).toString(type);
    }

    /**
     * 2个日期间隔天数
     * @param satrtDate
     * @param endDate
     * @return
     */
    public static int dateDaybetween(String satrtDate,String endDate){
        return Days.daysBetween(DateTime.parse(addDateRow(satrtDate)), DateTime.parse(addDateRow(endDate))).getDays();
    }

    /**
     * @Title: getNow
     * @Description: (获取当天日期)
     * @return
     * @return String
     */
    public static String getNow() {
        String date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        date = sdf.format(new Date(System.currentTimeMillis()));
        return date;
    }


    /**
     * @Title: detailTime
     * @Description: 获取当值格式化的时间
     * @return
     * @return String
     */
    public static String detailTimeSSS() {
        String date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        date = sdf.format(new Date(System.currentTimeMillis()));
        return date;
    }



    /**
     * @Title: nDayBeforeToday
     * @Description: 获取当前系统时间前 N天的时间
     * @param nday
     * @return
     * @return String
     */
    public static String nDayBeforeToday(int nday){
        long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date(now-nday*24*60*60*1000));
    }

    //



    /**
     * @Title: getDate
     * @Description:获取格式化日期
     * @param date
     * @param fromat
     * @return
     * @return Date
     */
    public static Date getDate(String date, String fromat)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(fromat);
        Date returnDate = null;
        try {
            returnDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }


    /**
     * 获取日期集合
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getDatesBetweenTwoDate(Date startDate, Date endDate) {
        List<Date> dateList = new ArrayList<Date>();
        List<String> stringList = new ArrayList<String>();
        dateList.add(startDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        while (true) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            if (endDate.after(cal.getTime())) {
                dateList.add(cal.getTime());
            } else {
                break;
            }
        }
//		dateList.add(endDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for (Date i : dateList){
            stringList.add(sdf.format(i).toString());
        }
        return stringList;
    }




    public static String generateBatchId(){
        Date date = new Date();
        String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(date);
        String HHmmssSSS = new SimpleDateFormat("HHmmssSSS").format(date);
        SecureRandom random = new SecureRandom();
        return (yyyyMMdd + HHmmssSSS + random.nextInt(10) + random.nextInt(10) + random.nextInt(10));
    }

    /**
     * @Title: theDayBeforeToday
     * @Description:  获取当前系统时间前N天的时间
     * @param nday
     * @return
     * @return long
     */
    public static long theDayBeforeToday(int nday){
        return System.currentTimeMillis()-nday*24*60*60*1000;
    }

    public static int theDaysBeforeToday(String simpleDate){
        int days=0;
        String day = simpleDate.trim().substring(6, 8);
        String month = simpleDate.trim().substring(4, 6);
        String year = simpleDate.trim().substring(0, 4);
        Calendar calendar = new GregorianCalendar(new Integer(year), new Integer(month)-1, new Integer(day));
        long millis = calendar.getTimeInMillis();
        long rangMillis = System.currentTimeMillis()-millis;
        days = ((Long)(rangMillis/86400000)).intValue();
        return days;
    }

    /**
     * 获取某日期，格式由参数传入 剔除节假日时间的计算
     */
    public static String getDayNotIncludeHoliday(String reg, int days, String holiday, boolean type) {
        String returnDate = "";
        try{
            if(type){
                String jjrDate = holiday;
                //节假日过滤
                if(jjrDate != null){
                    returnDate = jjrDate;
                }else{
                    returnDate = SkipWeekend(reg, days);
                }
            }else{
                returnDate = SkipWeekend(reg, days);
            }
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            returnDate = SkipWeekend(reg, days);
        }
        return returnDate;
    }
    /**
     * 跳过周末获取工作日
     * @param format
     * @param days
     * @return
     */
    public static String SkipWeekend(String format, Integer days) {
        String dataString = null;
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setFirstDayOfWeek(1);
        for (int i = 1; i <= days; i++) {
            calendar.add(Calendar.DATE, 1);
            if(calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7) {
                calendar.add(Calendar.DATE, 1);
                if(calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7) {
                    calendar.add(Calendar.DATE, 1);
                }
            }
        }
        dataString = sdf.format(calendar.getTime());
        return dataString;
    }


}
