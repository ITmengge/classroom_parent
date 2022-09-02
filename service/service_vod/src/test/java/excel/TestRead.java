package excel;

import com.alibaba.excel.EasyExcel;

/**
 * EasyExcel的读操作
 */
public class TestRead {
    public static void main(String[] args) {
        String fileName = "D:\\Java\\classroom\\classroom.xlsx";
        EasyExcel.read(fileName,User.class,new ExcelListener()).sheet().doRead();
    }
}
