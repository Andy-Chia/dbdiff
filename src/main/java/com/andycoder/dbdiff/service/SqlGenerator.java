package com.andycoder.dbdiff.service;

import com.andycoder.dbdiff.dto.Column;
import com.andycoder.dbdiff.dto.Index;
import com.andycoder.dbdiff.dto.Table;

/**
 * sql生成sql接口类
 * 这个是sql生成基本类，后续准备各种数据库的语句都通过这个基本接口类生成。
 */
public interface SqlGenerator {
    /**
     * 通过传入的 表结构生成表结构创建语句
     *
     * @param table 表结构详细信息
     * @return 返回表结构创建语句
     */
    public String generateCreateTableSql(Table table);

    /**
     * 生成修改表注释的sql.
     *
     * @param table 表结构详细信息
     * @return 仅返回表结构备注修改语句
     */
    public String generateUpdateTableCommentSql(Table table);

    /**
     * 生成新增列信息
     *
     * @param column 列字段
     * @return 返回列的新增语句
     */
    public String generateAddColumnSql(Column column);

    /**
     * 生成列的修改语句
     *
     * @param standardColumn 标准库的列字段信息
     * @param customColumn   客户端库的列字段信息
     * @return 返回列的修改语句
     */
    public String generateModifyColumnSql(Column standardColumn, Column customColumn);

    /**
     * 生成删除列的语句
     *
     * @param column 列字段信息
     * @return 返回列的删除信息
     */
    public String generateDropColumnSql(Column column);

    /**
     * 生成创建索引语句
     *
     * @param index 索引信息
     * @return 返回索引创建语句
     */
    String generateAddIndexSql(Index index);

    /**
     * 生成索引重命名语句
     *
     * @param standardIndex 标准库索引
     * @param customIndex   客户库索引
     * @return 返回索引重命名语句
     */

    String generateRenameIndexSql(Index standardIndex, Index customIndex);

    /**
     * 删除索引语句
     *
     * @param index 删除索引
     * @return 返回删除索引语句
     */
    String generateDeleteIndexSql(Index index);
}
