package com.andycoder.dbdiff.dto;

public class Column {
    /**
     * 字段名
     */
    private String name;
    /**
     * 字段类型
     */
    private String type;
    /**
     * 字段长度
     */
    private String length = "";
    /**
     * 是否为空
     */
    private Boolean isNullable;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 字段
     */
    private String comment;
    /**
     * 表明
     */
    private String tableName;
    /**
     * 字符集
     */
    private String charset;
    /**
     * 排序规则
     */
    private String collate;
    /**
     * 扩展信息，例如根据修改自动更新等等。
     */
    private String extend;

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCollate() {
        return collate;
    }

    public void setCollate(String collate) {
        this.collate = collate;
    }

    public Boolean getNullable() {
        return isNullable;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Column() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type == null) {
            this.type = "";
        } else {
            this.type = type;
        }
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        if (length == null) {
            this.length = "";
        } else {
            this.length = length;
        }

    }

    public Boolean isNullable() {
        return isNullable;
    }

    public void setNullable(Boolean nullable) {
        isNullable = nullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
