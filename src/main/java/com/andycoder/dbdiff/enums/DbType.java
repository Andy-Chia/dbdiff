package com.andycoder.dbdiff.enums;

public enum DbType {
    MYSQL("MYSQL"), ORACLE("ORACLE");
    private String dbType;

    DbType(String dbType) {
        this.dbType = dbType;
    }
}
