package com.andycoder.dbdiff.constants;

import com.andycoder.dbdiff.dto.Column;
import com.andycoder.dbdiff.enums.ColumnTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class ColumTypeConstant {

    public static List<String> mysqlDateTypeList() {
        List<String> dateTypeList = new ArrayList<>();
        dateTypeList.add(ColumnTypeEnum.MYSQL_DATE_COLUMN_TYPE.getColumnType());
        dateTypeList.add(ColumnTypeEnum.MYSQL_DATETIME_COLUMN_TYPE.getColumnType());
        dateTypeList.add(ColumnTypeEnum.MYSQL_TIMESTAMP_COLUMN_TYPE.getColumnType());
        return dateTypeList;
    }

    public static List<String> mysqlIngnoreExtraList() {
        List<String> dateTypeList = new ArrayList<>();
        dateTypeList.add(MYSQL_EXTRA_AUTO_INCREMENT);
        dateTypeList.add(MYSQL_EXTRA_DEFAULT_GENERATED);
        return dateTypeList;
    }

    public static String mysqlIngnoreExtraList(String extraStr) {
        for (String ignore : mysqlIngnoreExtraList()) {
            extraStr = extraStr.replace(ignore, "");
        }
        return extraStr;
    }


    public static List<String> mysqlIngnoreIndexList() {
        List<String> dateTypeList = new ArrayList<>();
        dateTypeList.add(MYSQL_INDEX_PRIMARY);
        return dateTypeList;
    }

    public static String MYSQL_CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";

    public static String MYSQL_EXTRA_DEFAULT_GENERATED = "DEFAULT_GENERATED";
    public static String MYSQL_EXTRA_AUTO_INCREMENT = "auto_increment";

    public static String MYSQL_EXTRA_ON_UPDATE_CURRENT_TIMESTAMP = "on update CURRENT_TIMESTAMP";

    public static String MYSQL_INDEX_PRIMARY = "PRIMARY";

}
