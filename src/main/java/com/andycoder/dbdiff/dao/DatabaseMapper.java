package com.andycoder.dbdiff.dao;

import com.andycoder.dbdiff.dto.Column;
import com.andycoder.dbdiff.dto.Index;
import com.andycoder.dbdiff.dto.Table;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface DatabaseMapper {
    @Select({"SELECT",
            "    table_name AS tableName, ",
            "    TABLE_COMMENT AS 'comment', ",
            "    ENGINE AS 'engine', ",
            "    TABLE_COLLATION AS 'collate', ",
            "    SUBSTRING_INDEX( TABLE_COLLATION, '_', 1 ) AS 'charset'  ",
            "FROM ",
            "    information_schema.TABLES  ",
            "WHERE  table_schema = #{databaseName}"})
    @Results({
            @Result(property = "name", column = "name"),
            @Result(property = "comment", column = "comment")
    })
    List<Table> getTableList(@Param("databaseName") String databaseName);

    @Select({"SELECT ",
            "    table_name as tableName,",
            "    column_name AS NAME, ",
            "    data_type AS type, ",
            "IF ",
            "    ( is_nullable = 'NO', 0, 1 ) AS nullable, ",
            "IF ",
            "    ( column_default IS NOT NULL, column_default, null ) AS defaultValue, ",
            "    ( ",
            "CASE ",
            "     ",
            "    WHEN data_type = 'float'  ",
            "    OR data_type = 'double'  ",
            "    OR data_type = 'TINYINT'  ",
            "    OR data_type = 'SMALLINT'  ",
            "    OR data_type = 'MEDIUMINT'  ",
            "    OR data_type = 'INT'  ",
            "    OR data_type = 'INTEGER'  ",
            "    OR data_type = 'decimal'  ",
            "    OR data_type = 'bigint' THEN ",
            "    NUMERIC_PRECISION ELSE CHARACTER_MAXIMUM_LENGTH  ",
            "END  ",
            ") AS length, ",
            "    column_comment AS 'COMMENT' , ",
            "    character_set_name  as 'charset' ,  ",
            "    collation_name as 'collate', ",
            "    extra as 'extend' ",
            "FROM ",
            "    information_schema.COLUMNS ",
            "WHERE table_schema = #{databaseName} AND table_name = #{tableName} ",
            "order by CONVERT(ordinal_position,UNSIGNED) asc ; "})
    @Results({
            @Result(property = "name", column = "name"),
            @Result(property = "type", column = "type"),
            @Result(property = "nullable", column = "nullable"),
            @Result(property = "defaultValue", column = "defaultValue"),
            @Result(property = "comment", column = "comment"),
            @Result(property = "length", column = "length")
    })
    List<Column> getColumnList(@Param("databaseName") String databaseName,
                               @Param("tableName") String tableName);

    @Select({"SELECT " +
            "    table_name AS tableName, " +
            "    index_name AS NAME, " +
            "    non_unique AS nonUnique, " +
            "    GROUP_CONCAT( column_name ORDER BY seq_in_index SEPARATOR ',' ) AS columnStr, " +
            "    index_comment AS COMMENT, " +
            "    index_type AS indexType  " +
            "FROM " +
            "    information_schema.statistics  " +
            "WHERE table_schema = #{databaseName} AND table_name = #{tableName} " +
            "GROUP BY index_name, " +
            "    non_unique, " +
            "    index_type, " +
            "    index_comment"})
    @Results({
            @Result(property = "name", column = "name"),
            @Result(property = "nonUnique", column = "nonUnique"),
            @Result(property = "columnStr", column = "columnStr", jdbcType = JdbcType.VARCHAR)
    })
    List<Index> getIndexList(@Param("databaseName") String databaseName,
                             @Param("tableName") String tableName);


    @Select("SELECT table_name as tableName,index_name AS name, GROUP_CONCAT(column_name ORDER BY seq_in_index SEPARATOR ',') AS columnStr " +
            "FROM information_schema.statistics " +
            "WHERE table_schema = #{databaseName} AND table_name = #{tableName} " +
            "AND index_name = 'PRIMARY'")
    @Results({
            @Result(property = "name", column = "name"),
            @Result(property = "columnStr", column = "columnStr", jdbcType = JdbcType.VARCHAR)
    })
    Index getPrimaryKey(@Param("databaseName") String databaseName,
                        @Param("tableName") String tableName);

}
