/**
 * HPX.com Inc.
 * Copyright (c) 2018-YEARAll Rights Reserved.
 */
package com.qlj.flow.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/** 
 *
 * @author qlj  
 * @version $Id: MathUtil.java, v 0.1
 *    2018-08-25 14:53 qlj Exp $$ 
 */
public class MathUtil {



    /**
     * 日志对象
     **/
    private static final Logger logger = LoggerFactory.getLogger(MathUtil.class);


    /**
     * 加法
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return add(a,b,16);
    }

    /**
     * 加法
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b, int scale) {
        return add(a,b,scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 加法
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b, int scale, int roundingMode) {
        if(null==a){
            a= BigDecimal.ZERO;
        }
        if(null==b){
            b= BigDecimal.ZERO;
        }
        return a.add(b).setScale(scale, roundingMode);
    }


    /**
     * 减法
     */
    public static BigDecimal sub(BigDecimal a, BigDecimal b){
        return  sub(a,b,16);
    }

    /**
     * 减法
     */
    public static BigDecimal sub(BigDecimal a, BigDecimal b, int scale ){
        return sub(a,b,scale, BigDecimal.ROUND_HALF_DOWN);
    }

    /**
     * 减法
     */
    public static BigDecimal sub(BigDecimal a, BigDecimal b, int scale , int roundingMode){
        if(null==a){
            a= BigDecimal.ZERO;
        }
        if(null==b){
            b= BigDecimal.ZERO;
        }
        return a.subtract(b).setScale(scale, roundingMode);
    }

    /**
     * 乘法
     */
    public static BigDecimal mul(BigDecimal a, BigDecimal b, int scale, int roundingMode){
        return a.multiply(b).setScale(scale, roundingMode);
    }
    /**
     * 乘法
     */
    public static BigDecimal mul(BigDecimal a, BigDecimal b, int scale){
        return mul(a,b,scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 乘法
     */
    public static BigDecimal mul(BigDecimal a, BigDecimal b){
        return mul(a,b,16);
    }


    /**
     * 除法
     */
    public static BigDecimal div(BigDecimal a, BigDecimal b, int scale, int roundingMode){
        if (BigDecimal.ZERO.compareTo(b) == 0) {
            return BigDecimal.ZERO;
        }
        return a.divide(b, scale, roundingMode);
    }
    /**
     * 除法
     */
    public static BigDecimal div(BigDecimal a, BigDecimal b, int scale){
        return div(a, b, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 除法
     */
    public static BigDecimal div(BigDecimal a, BigDecimal b){
        return div(a, b, 16, BigDecimal.ROUND_HALF_UP);
    }



    /**
     *
     * @param d
     * @return
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }



    /**
     *
     */
    private static double EARTH_RADIUS = 6371.0;

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1  经度1
     * @param lng1  纬度1
     * @param lat2  经度2
     * @param lng2  纬度2
     * @return 距离
     */
    public static double getDistance(double lat1, double lng1,
                                     double lat2,double lng2) {

        //用haversine公式计算球面两点间的距离。
        //经纬度转换成弧度
        lat1=covertDegreesToRadians(lat1);
        lng1=covertDegreesToRadians(lng1);
        lat2=covertDegreesToRadians(lat2);
        lng2=covertDegreesToRadians(lng2);

        //计算差值
        double vLon = Math.abs(lng1 - lng2);
        double vLat = Math.abs(lat1 - lat2);

        //计算球面距离的长度
        double h=haveSin(vLat)+ Math.cos(lat1)* Math.cos(lat2)*haveSin(vLon);

        //将球面距离长度根据半径转化为实际长度
        double distance=2*EARTH_RADIUS* Math.asin(Math.sqrt(h));

        return distance;
    }


    /**
     * HaverSin 公式
     * @param theta
     * @return
     */
    private static double haveSin(double theta){
        double sin = Math.sin(theta);

        return sin*sin;
    }

    /**
     * 角度转换为弧度
     * @param degrees
     * @return
     */
    private static double covertDegreesToRadians(double degrees){
        return degrees * Math.PI / 180;
    }

    /**
     * 将弧度转换为角度
     * @param radian
     * @return
     */
    private static double coverRadiansToDegrees(double radian){
        return radian * 180.0 / Math.PI;
    }





    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 距离
     */
    public static BigDecimal getDistance(BigDecimal lat1, BigDecimal lng1, BigDecimal lat2,
                                         BigDecimal lng2) {

        return new BigDecimal(getDistance(lat1.doubleValue(),lng1.doubleValue(),
                lat2.doubleValue(),lng2.doubleValue()));
    }


    /**
     * 根据经纬度和半径计算经纬度范围
     *
     * @param raidus 单位米
     * @return minLat, minLng, maxLat, maxLng
     */
    public static double[] getAround(double lat, double lon, int raidus) {

        Double latitude = lat;
        Double longitude = lon;

        Double degree = (24901 * 1609) / 360.0;
        double raidusMile = raidus;

        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * raidusMile;
        Double minLat = latitude - radiusLat;
        Double maxLat = latitude + radiusLat;

        Double mpdLng = degree * Math.cos(latitude * (Math.PI / 180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * raidusMile;
        Double minLng = longitude - radiusLng;
        Double maxLng = longitude + radiusLng;
        return new double[]{minLat, minLng, maxLat, maxLng};
    }


    /**
     * 根据经纬度和半径计算经纬度范围
     *
     * @param raidus 单位米
     * @return minLat, minLng, maxLat, maxLng
     */
    public static BigDecimal[] getAround(BigDecimal lat, BigDecimal lon, BigDecimal raidus) {

        BigDecimal latitude = lat;
        BigDecimal longitude = lon;

        BigDecimal degree = div(mul(new BigDecimal(24901),new BigDecimal(1609)),new BigDecimal(360));

        BigDecimal raidusMile = raidus;

        BigDecimal dpmLat = div(BigDecimal.ONE,degree);
        BigDecimal radiusLat =mul(dpmLat,raidusMile);
        BigDecimal minLat = sub(latitude, radiusLat);
        BigDecimal maxLat = add(latitude , radiusLat);

        BigDecimal mpdLng = mul(degree,new BigDecimal(Math.cos(latitude.doubleValue() * (Math.PI / 180))));
        BigDecimal dpmLng = div(BigDecimal.ONE,mpdLng);
        BigDecimal radiusLng = mul(dpmLng , raidusMile);
        BigDecimal minLng =sub( longitude , radiusLng);
        BigDecimal maxLng = add(longitude , radiusLng);
        return new BigDecimal[]{minLat, minLng, maxLat, maxLng};
    }


    public static void main(String[] args){
        //测试， 计算bigDecimal类型计算和double类型计算是否有差异
        double distance = getDistance(25.01503D, 102.7437D, 25.00759D, 102.73889D);

        System.out.println(distance);
    }


}
