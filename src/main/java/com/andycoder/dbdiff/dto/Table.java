package com.andycoder.dbdiff.dto;

import java.util.List;
import java.util.Objects;

public class Table {
    /**
     * 表名
     */
    private String tableName;
    /**
     * 字段
     */
    private List<Column> columns;
    /**
     * 主键
     */
    private Index primaryKey;
    /**
     * 索引
     */
    private List<Index> indexes;
    /**
     * 注释
     */
    private String comment;
    /**
     * 字符集
     */
    private String charset;
    /**
     * 排序
     */
    private String collate;
    /**
     * 存储引擎
     */
    private String engine;

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public void setPrimaryKey(Index primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setIndexes(List<Index> indexes) {
        this.indexes = indexes;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public List<Column> getColumns() {
        return columns;
    }

    public Index getPrimaryKey() {
        return primaryKey;
    }

    public List<Index> getIndexes() {
        return indexes;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(tableName, table.tableName) && Objects.equals(columns, table.columns) && Objects.equals(primaryKey, table.primaryKey) && Objects.equals(indexes, table.indexes) && Objects.equals(comment, table.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, columns, primaryKey, indexes, comment);
    }


}