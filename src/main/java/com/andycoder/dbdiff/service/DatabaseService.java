package com.andycoder.dbdiff.service;

import com.andycoder.dbdiff.dto.Column;
import com.andycoder.dbdiff.dto.Table;

import java.util.List;

public interface DatabaseService {


    public List<Table> getStandardTableList();

    public List<Table> getCustomTableList();

    /**
     * 通过table对象生成创建语句
     *
     * @param table
     * @return
     */
    public String generateCreateTableSql(Table table);

    /**
     * 通过table对象生成创建语句
     *
     * @param table
     * @return
     */
    public String generateUpdateTableCommentSql(Table table);

    /**
     * 通过table对象生成创建语句
     *
     * @param table
     * @return
     */
    public String generateAddColumnSql(Column column);

    /**
     * 通过table对象生成创建语句
     *
     * @param table
     * @return
     */
    public String generateDiffColumnSql(Column standardColumn, Column customColumn);

    /**
     * 通过table对象生成创建语句
     *
     * @param table
     * @return
     */
    public String generateDropColumnSql(Column column);
}
