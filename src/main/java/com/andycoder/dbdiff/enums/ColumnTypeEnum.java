package com.andycoder.dbdiff.enums;

public enum ColumnTypeEnum {

    MYSQL_BIG_COLUMN_TYPE("big", "1", DbType.MYSQL),
    MYSQL_INT_COLUMN_TYPE("int", "11", DbType.MYSQL),
    MYSQL_BIGINT_COLUMN_TYPE("bigint", "20", DbType.MYSQL),
    MYSQL_TINYINT_COLUMN_TYPE("tinyint", "1", DbType.MYSQL),
    MYSQL_FLOAT_COLUMN_TYPE("float", "", DbType.MYSQL),
    MYSQL_DOUBLE_COLUMN_TYPE("double", "", DbType.MYSQL),
    MYSQL_VARCHAR_COLUMN_TYPE("varchar", "255", DbType.MYSQL),
    MYSQL_DATETIME_COLUMN_TYPE("datetime", "", DbType.MYSQL),
    MYSQL_DATE_COLUMN_TYPE("date", "", DbType.MYSQL),
    MYSQL_TIMESTAMP_COLUMN_TYPE("timestamp", "", DbType.MYSQL),
    DEFAULT_MYSQL_UNKNOWN_TYPE("VARCHAR", "255", DbType.MYSQL);

    ColumnTypeEnum(String columnType, String defaultLength, DbType dbType) {
        this.columnType = columnType;
        this.defaultLength = defaultLength;
        this.dbType = dbType;
    }

    private String columnType;
    private String defaultLength;
    private DbType dbType;

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getDefaultLength() {
        return defaultLength;
    }

    public void setDefaultLength(String defaultLength) {
        this.defaultLength = defaultLength;
    }

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }


}
