/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.DAY_OF_WEEK_IN_MONTH;
/**
 *
 * @author 49796
 * @version :  com.wj.updater.util.DateUtil.java,  v  0.1  2020/5/27  15:42  49796  Exp  $$
 */
public class DateUtil {

    /**
     * 日志对象
     **/
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 30分钟的毫秒值
     */
    public static final long MINUTE_30_MILLISECOND=30*60*1000;

    /**
     * 一天的秒数
     */
    public static final long DAY_SECOND=60*60*24;


    /**
     * 一天的秒数
     */
    public static final long DAY_7_SECOND=7*60*60*24;

    /**
     * 默认的时间格式化格式
     */
    public static final String WEB_FORMAT="yyyy-MM-dd HH:mm:ss";

    /**
     *
     */
    public static final String DAY_FORMAT="yyyy-MM-dd";

    /**
     *
     */
    public static final String MONTH_FORMAT="yyyy-MM";

    /**
     * 微信订单中的时间格式
     */
    public static final String WX_ORDER_TIME_FORMAT="yyyyMMddHHmmss";

    /**
     * k线数据中的时间格式
     */
    public static final String KLINE_TIME_FORMAT="yyyyMMddHHmm";


    /**
     * 所有日期格式
     */
    public static final String[] ALL_FORMAT=new String[]{WEB_FORMAT,
            DAY_FORMAT,
            MONTH_FORMAT,
            WX_ORDER_TIME_FORMAT,
            KLINE_TIME_FORMAT};


    /**
     *  main 函数
     * @param args
     */
    public static void main(String[] args) {


    }


    /**
     * 获取一天的开始时间
     * @param date
     * @return
     */
    public static Date getStartTimeOfDay(Date date){
        if(null==date){
            return  null;
        }
        return parse(format(date,DAY_FORMAT),DAY_FORMAT);
    }

    /**
     * 获取指定某月某周的开始时间
     * @param month
     * @param week
     * @return
     */
    public static Date getFirstTimeOfDayWeekMonth(int month,Integer week){
        Calendar c=Calendar.getInstance();
        c.set(Calendar.MONTH,month-1);
        if(null==week){
            c.set(Calendar.DAY_OF_MONTH,1);
        }else{
            c.set(DAY_OF_WEEK_IN_MONTH,week);
            c.set(DAY_OF_WEEK,1);
        }
        c.set(Calendar.HOUR,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        return c.getTime();
    }


    /**
     * 获取指定某月某周的结束时间
     * @param month
     * @param week
     * @return
     */
    public static Date getEndTimeOfDayWeekMonth(int month,Integer week){
        Calendar c=Calendar.getInstance();
        if(null==week){
            c.set(Calendar.MONTH,month);
            c.set(Calendar.DAY_OF_MONTH,1);
        }else{
            c.set(Calendar.MONTH,month-1);
            c.set(DAY_OF_WEEK_IN_MONTH,week+1);
            c.set(DAY_OF_WEEK,1);
        }
        c.set(Calendar.HOUR,-12);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        return c.getTime();
    }


    /**
     * 根据间隔时间，获取间隔当前多少天的时间
     * @param day
     * @return
     */
    public static Date getBeforeDateByDate(int day){
        return getBeforeDateByDate(new Date(),day);
    }

    /**
     * 根据间隔时间，获取间隔指定时间多少天的时间
     * @param day
     * @return
     */
    public static Date getBeforeDateByDate(Date date,int day){
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE,-day);
        return c.getTime();
    }


    /**
     * 根据间隔时间，获取间隔指定时间多少月的时间
     * @param month
     * @return
     */
    public static Date getBeforeDateByMonth(Date date,int month){
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH,-month);
        return c.getTime();
    }


    /**
     * 根据间隔时间，获取间隔当前多少天的时间
     * @param hour
     * @return
     */
    public static Date getBeforeDateByHour(int hour){
        Calendar c=Calendar.getInstance();
        c.add(Calendar.HOUR,hour);
        return c.getTime();
    }


    /**
     * 使用默认格式格式化当前时间
     * @return
     */
    public static String format(){
        return  format(new Date());
    }

    /**
     * 使用默认格式格式化时间
     * @param date
     * @return
     */
    public static String format(Date date){
        return  format(date,WEB_FORMAT);
    }

    /**
     * 格式化时间格式
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date,String format){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @param str
     * @param format
     * @return
     */
    public static Date parse(String str,String format){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
        try{
            Date parse = simpleDateFormat.parse(str);
            return parse;
        }catch (Exception e){
            logger.error("字符串"+str+"转"+format+"异常",e);
        }
        return null;
    }

    /**
     *
     * @param str
     * @return
     */
    public static Date parse(String str){
        return parse(str,WEB_FORMAT);
    }

    /**
     * 获取指定月份的所有日期
     * @param year
     * @param month
     * @return
     */
    public static List<Date> getMonthDay(int year,int month){
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month+1);
        Date nextMonth = c.getTime();
        nextMonth=DateUtil.parse(DateUtil.format(nextMonth,"yyyy-MM"),"yyyy-MM");

        c.set(Calendar.MONTH,month);
        List<Date> thisMonthDays=new ArrayList<>();
        int index=1;
        while (true){
            c.set(Calendar.DAY_OF_MONTH,index);
            Date time = c.getTime();
            if(time.compareTo(nextMonth)>=0){
                break;
            }
            thisMonthDays.add(DateUtil.parse(DateUtil.format(time,"yyyy-MM-dd"),"yyyy-MM-dd"));
            index++;
        }
        return thisMonthDays;
    }


    public static List<Date> findDates(Date beginDate,Date endDate){
        List<Date> dateList=new ArrayList<>();

        Calendar begin=Calendar.getInstance();
        begin.setTime(beginDate);

        Calendar end=Calendar.getInstance();
        end.setTime(endDate);

        while (end.after(begin)){
            dateList.add(begin.getTime());
            begin.add(Calendar.DAY_OF_MONTH,1);
        }
        return dateList;
    }
}
