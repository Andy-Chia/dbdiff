package com.andycoder.dbdiff.dto;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Index {
    /**
     * 索引名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 列
     */
    private List<String> columns;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 唯一索引
     */
    private Boolean nonUnique;
    /**
     * 索引字符串
     */
    private String columnStr;
    /**
     * 索引类型
     */
    private String indexType;
    /**
     * 注释
     */
    private String comment;

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getColumnStr() {
        return columnStr;
    }

    public void setColumnStr(String columnStr) {
        this.columnStr = columnStr;
        if (StringUtils.isEmpty(columnStr)) {
            this.columns = new ArrayList<>();
        }
        String[] split = columnStr.split(",");
        this.columns = Arrays.asList(split);
    }

    public void setColumns(String columns) {
        if (StringUtils.isEmpty(columns)) {
            this.columns = new ArrayList<>();
        }
        String[] split = columns.split(",");
        this.columns = Arrays.asList(split);
    }


    public Boolean getNonUnique() {
        return nonUnique;
    }

    public void setNonUnique(Boolean nonUnique) {
        this.nonUnique = nonUnique;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<String> getColumns() {
        return columns;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String toDefinition() {
        return String.format("%s %s (%s)", type, name, String.join(", ", columns));
    }

    public boolean isEqual(Index other) {
        return this.type.equals(other.type) && this.columns.equals(other.columns);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return Objects.equals(name, index.name) && Objects.equals(type, index.type) && Objects.equals(columns, index.columns) && Objects.equals(tableName, index.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, columns, tableName);
    }
}
