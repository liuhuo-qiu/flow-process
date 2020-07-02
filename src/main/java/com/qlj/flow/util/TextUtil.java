/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.qlj.flow.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 *
 * @author 49796
 * @version :  com.wj.updater.util.TextUtil.java,  v  0.1  2020/5/27  14:47  49796  Exp  $$
 */
public class TextUtil {


    private static final String dateFormat="yyyyMMdd";


    /**
     *  main 函数
     * @param args
     */
    public static void main(String[] args) {
        String filePath="C:\\Users\\49796\\Desktop\\0060060000028120625200281206252019060220200529160831_copy.xls";
        String split="^";
        int skipLine=0;

        String[] filePaths=new String[]{
                "C:\\Users\\49796\\Desktop\\0060060000028120625200281206252019060220191129160831_1.xls",
                "C:\\Users\\49796\\Desktop\\0060060000028120625200281206252019112920200529160828_1.xls",
        };

        try{
            String fileName=StringUtils.substring(filePath,StringUtils.lastIndexOf(filePath,"\\")+1);

            String beginDateStr=StringUtils.substring(fileName,30,38);
            String endDateStr=StringUtils.substring(fileName,38,46);

            Date begin=DateUtil.parse(beginDateStr,dateFormat);
            Date end=DateUtil.parse(endDateStr,dateFormat);

            List<Date> dates = DateUtil.findDates(begin, end);



            /**
             * 读取原数据文件
             */

//            List<List<List<String>>> excel = ExcelUtil.getExcel(new FileInputStream(new File(filePath)), filePath);
//            List<List<String>> sourceLists=excel.get(0);
//
//            List<List<String>> lists=new ArrayList<>();
//            for(int i=skipLine;i<sourceLists.size();i++){
//                    lists.add(sourceLists.get(i));
//            }

            List<List<String>> lists=new ArrayList<>();
            for(String p:filePaths){
                List<List<List<String>>> excel = ExcelUtil.getExcel(new FileInputStream(new File(p)), p);
                List<List<String>> sourceLists=excel.get(0);
                for(int i=skipLine;i<sourceLists.size();i++){
                    lists.add(sourceLists.get(i));
                }
            }

//            List<List<String>> sourceLists = readCVS(filePath, split, "gb2312");
//            String excelFileName= StringUtils.substring(filePath,0,StringUtils.lastIndexOf(filePath,"."));
//            excelFileName=excelFileName+"_copy.xlsx";
//            ExcelUtil.createExcel(excelFileName,sourceLists);

//            List<List<String>> lists=new ArrayList<>();
//            for(String p:filePaths){
//                List<List<String>> sourceLists = readCVS(p, split, "utf-8");
//                for(int i=skipLine;i<sourceLists.size();i++){
//                    lists.add(sourceLists.get(i));
//                }
//            }
//
//            /**
//             * 将数据处理成时间连续的数据并且存入excel中，文件为 filePath更换后缀为xlsx
//             */
            covertDataAndCreateExcel(filePath, dates, lists);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 将数据处理为时间连续型数据，并且生成excel
     * @param filePath
     * @param dates
     * @param lists
     */
    private static void covertDataAndCreateExcel(String filePath, List<Date> dates, List<List<String>> lists) {

        List<List<String>> collect = lists.stream().map(list -> {
            List<String> collect1 = list.stream().map(item -> {
                if (StringUtils.startsWith(item, "\"")) {
                    item = StringUtils.substring(item, 1);
                    item = StringUtils.substring(item, 0, item.length() - 1);
                }
                item = StringUtils.replace(item, "\t", "");
                item = StringUtils.replace(item, "^", "");
                return item;
            }).collect(Collectors.toList());
//            collect1.remove(0);
            return collect1;

        }).collect(Collectors.toList());

        /**
         * 转换为时间连续的数据
         */
        List<List<String>> newList = covertData(dates, collect);

        /**
         * 生成excel文件
         */
        String excelFileName= StringUtils.substring(filePath,0,StringUtils.lastIndexOf(filePath,"."));
        excelFileName=excelFileName+"_copy.xlsx";

        Map<String,List<List<String>>> excelData=new HashMap<>();

        excelData.put("原数据",collect);
        newList.add(0,collect.get(0));
        excelData.put("填充后的数据",newList);
        ExcelUtil.createExcel(excelFileName,excelData);
    }

    /**
     * 转换数据，将非连续时间数据转换为时间连续的数据
     * @param dates
     * @param lists
     * @return
     */
    private static List<List<String>> covertData(List<Date> dates, List<List<String>> lists) {
        List<List<String>> collect=new ArrayList<>();
        collect.addAll(lists);

        //删除title
        collect.remove(0);

//        Collections.reverse(collect);

        List<String> start=new ArrayList<>();
        Map<String,List<String>> dateCacheMap=new HashMap<>();

//        start.addAll(collect.get(0));
        start.addAll(collect.get(collect.size()-1));
        collect.forEach(item -> {
            if (CollectionUtils.isEmpty(item)) {
                return;
            }
            String key=item.get(0);
            if(StringUtils.isBlank(key)){
                return;
            }
            Date parse = DateUtil.parse(key, "yyyy-MM-dd HH:mm:SS");
            if(null==parse){
                System.out.println(JSONObject.toJSONString(item));
                return;
            }
            key=DateUtil.format(parse,dateFormat);
            if(StringUtils.isBlank(key)){
                return;
            }
            List<String> strings = dateCacheMap.get(key);
            if(null!=strings){
                return;
            }
            dateCacheMap.put(key,item);
        });


        List<List<String>> newList=new ArrayList<>();
        List<String> datesList=new ArrayList<>();
        for(int i=0;i<dates.size();i++){
            Date date = dates.get(i);
            String dateStr= DateUtil.format(date,dateFormat);
            List<String> endStrings = dateCacheMap.get(dateStr);
            if(null==endStrings){
                datesList.add(dateStr);
                continue;
            }
            List<String> startStrings;
            if(newList.size()==0){
                startStrings=start;
            }else{
                startStrings=newList.get(newList.size()-1);
            }
            List<String> finalStartStrings = startStrings;
            datesList.forEach(item->{
                List<String> newStrList=new ArrayList<>();
                finalStartStrings.forEach(str->{
                    if(CollectionUtils.isEmpty(newStrList)){
                        newStrList.add(item);
                        return;
                    }
                    if(newStrList.size()!=8){
                        newStrList.add("");
                        return;
                    }
                    newStrList.add(str);
                });
                newList.add(newStrList);
            });
            newList.add(endStrings);
            datesList=new ArrayList<>();
        }
        return newList;
    }

    /**
     * 读取cvs文件
     * @param filePath
     * @param split
     * @param charset
     * @return
     * @throws IOException
     */
    public static List<List<String>> readCVS(String filePath,String split,String charset) throws IOException {
        List<List<String>> result=new ArrayList<>();
        List<String> lines = readLine(filePath,charset);
        AtomicInteger index= new AtomicInteger();
        lines.forEach(line->{
            List<String> list = Arrays.asList(StringUtils.split(line,split));
            result.add(list);
        });
        return result;
    }



    /**
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static List<String> readLine(String filePath) throws IOException {
        return readLine(filePath,"utf-8");
    }


    /**
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static List<String> readLine(String filePath,String charset) throws IOException {
        File file=new File(filePath);
        return readLine(new FileInputStream(file),charset);
    }

    /**
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static List<String> readLine(InputStream inputStream) throws IOException{
        return readLine(inputStream,"utf-8");
    }
    /**
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static List<String> readLine(InputStream inputStream,String charset) throws IOException {
        List<String> textLines=new ArrayList<>();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,charset);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String lineTxt = null;
        while((lineTxt = bufferedReader.readLine()) != null){
            textLines.add(lineTxt);
        }
        inputStreamReader.close();

        return textLines;
    }

}
