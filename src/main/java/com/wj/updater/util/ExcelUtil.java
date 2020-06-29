/**
 * Copyright (c) 2019-YEARALL rights Reserved
 */
package com.wj.updater.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 49796
 * @version :  com.wj.updater.util.ExcelUtil.java,  v  0.1  2020/5/27  17:21  49796  Exp  $$
 */
public class ExcelUtil {


    /**
     * 日志对象
     */
    private static final Logger logger= LoggerFactory.getLogger(ExcelUtil.class);


    /**
     * 创建单sheet的excel
     * @param filePath
     * @param data
     * @return
     */
    public static boolean createExcel(String filePath,List<List<String>> data){
        Map<String,List<List<String>>> excelData=new HashMap<>();
        excelData.put("sheet1",data);
        return createExcel(filePath,excelData);
    }


    /**
     * 创建excel
     * @param filePath
     * @param data
     * @return
     */
    public static boolean createExcel(String filePath,Map<String,List<List<String>>> data){
        try(XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream outputStream = new FileOutputStream(filePath)){

            for(String sheet:data.keySet()){
                XSSFSheet sheet1 = workbook.createSheet(sheet);
                List<List<String>> rows = data.get(sheet);

                for(int i=0;i<rows.size();i++){
                    List<String> rowData = rows.get(i);
                    XSSFRow row = sheet1.createRow(i);
                    for(int j=0;j<rowData.size();j++){
                        XSSFCell cell = row.createCell(j);
                        String cellData = rowData.get(j);
                        cell.setCellValue(cellData);
                    }
                }
            }
            workbook.write(outputStream);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取excel的内容
     * @param in
     * @param fileName
     * @return
     */
    public static List<List<List<String>>> getExcel(InputStream in,String fileName){

        List<List<List<String>>> excels=new ArrayList<>();
        try{
            Workbook workBook = getWorkBook(in, fileName);

            if(null==workBook){
                throw new RuntimeException("excel工作簿对象为空!");
            }

            int numberOfSheets = workBook.getNumberOfSheets();
            for(int i=0;i<numberOfSheets;i++){
                Sheet sheetAt = workBook.getSheetAt(i);
                if(null==sheetAt){
                    continue;
                }
                List<List<String>> rows = readSheet(sheetAt);

                if(!CollectionUtils.isEmpty(rows)){
                    excels.add(rows);
                }
            }
        }catch (Exception e){
            throw new RuntimeException("读取excel内容失败",e);
        }

        return excels;
    }



    /**
     * 根据输入流和文件名 获取excel文件对象
     * @param in
     * @param fileName
     * @return
     * @throws IOException
     */
    public static Workbook getWorkBook(InputStream in, String fileName) throws IOException {
        String fileType = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, "."));

        switch (fileType){
            case ".xls":
                return new HSSFWorkbook(in);
            case ".xlsx":
                return new XSSFWorkbook(in);
            default:
                throw new RuntimeException("目标文件不是xls或xlsx的，请检查");
        }
    }


    /**
     * 读取一个sheet
     * @param sheetAt
     * @return
     */
    public static List<List<String>> readSheet(Sheet sheetAt) {
        List<List<String>> rows=new ArrayList<>();
        for(int rowNum=sheetAt.getFirstRowNum();rowNum<=sheetAt.getLastRowNum();rowNum++){
            Row row = sheetAt.getRow(rowNum);
            if(null==row){
                continue;
            }
            List<String> cellList=new ArrayList<>();
            for(int celNum=row.getFirstCellNum();celNum<=row.getLastCellNum();celNum++){
                Cell cell = row.getCell(celNum);
                if(null==cell){
                    cellList.add(StringUtils.EMPTY);
                    continue;
                }
                CellType cellType = cell.getCellType();
                switch (cellType.name()){
                    case "STRING":
                        cellList.add(cell.getStringCellValue());
                        break;
                    case "NUMERIC":
                        double numericCellValue = cell.getNumericCellValue();
                        if(HSSFDateUtil.isCellDateFormatted(cell)){
                            Date javaDate = HSSFDateUtil.getJavaDate(numericCellValue);
                            cellList.add(DateUtil.format(javaDate));
                        }else{
                            cellList.add(new BigDecimal(String.valueOf(numericCellValue)).toPlainString());
                        }

                        break;
                    default:
                        cellList.add(cell.toString());
                        break;
                }
            }
            if(!CollectionUtils.isEmpty(cellList)){
                rows.add(cellList);
            }
        }
        return rows;
    }


    /**
     * 读取excel 把第一行的值作为key 构造为list 对象返回
     * @param sheet
     * @param in
     * @param fileName
     * @return
     */
    public static List<JSONObject> getExcel(int sheet, InputStream in, String fileName){
        return getExcel(sheet,null,in,fileName);
    }
    /**
     * 读取excel 把第一行的值对应到columnMap中的值作为key 构造为list 对象返回
     * @param sheet
     * @param in
     * @param fileName
     * @return
     */
    public static List<JSONObject> getExcel(int sheet,Map<String,String> columnMap, InputStream in, String fileName){
        List<JSONObject> result=new ArrayList<>();

        List<List<List<String>>> excel = getExcel(in, fileName);

        if(CollectionUtils.isEmpty(excel)||excel.size()<sheet){
            throw new RuntimeException("指定excel内容为空");
        }

        List<List<String>> rows = excel.get(sheet - 1);
        List<String> columns = rows.get(0);
        for(int rowNum=1;rowNum<rows.size();rowNum++){
            List<String> cel = rows.get(rowNum);
            JSONObject celObject=new JSONObject();
            for(int celNum=0;celNum<cel.size();celNum++){
                String key = columns.get(celNum);
                if(!CollectionUtils.isEmpty(columnMap)){
                    String rKey = columnMap.get(key);
                    if(StringUtils.isBlank(rKey)){
                        continue;
                    }
                    key =rKey;
                }
                celObject.put(key,cel.get(celNum));
            }
            result.add(celObject);
        }
        return result;
    }


    public static void main(String[] args) {

        File file = new File("C:\\Users\\Administrator\\Desktop\\考勤测试.xls");

        try{


        }catch (Exception e){
            logger.error("excel 读取异常",e);
        }
    }

}
