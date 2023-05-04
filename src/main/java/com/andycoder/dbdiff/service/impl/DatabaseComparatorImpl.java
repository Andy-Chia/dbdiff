package com.andycoder.dbdiff.service.impl;

import cn.hutool.json.JSONUtil;
import com.andycoder.dbdiff.dto.Column;
import com.andycoder.dbdiff.dto.Index;
import com.andycoder.dbdiff.dto.Table;
import com.andycoder.dbdiff.service.DatabaseComparator;
import com.andycoder.dbdiff.service.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class DatabaseComparatorImpl implements DatabaseComparator {


    @Value("${diffresult.filepath}")
    private String outputFilePath;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DatabaseService databaseService;

    // Compare the two databases and generate the SQL script
    public void compare() {

        List<Table> standardTables = databaseService.getStandardTableList();
        List<Table> customTables = databaseService.getCustomTableList();
        List<String> sqlStatements = new ArrayList<>();
        logger.info(JSONUtil.toJsonStr(standardTables));
        logger.info("----------------------------------");
        // Compare the tables and their columns
        for (Table standardTable : standardTables) {
            /**
             * 通过表明获取旧库的表结构
             */
            Table customTable = getTableByName(customTables, standardTable.getTableName());
            logger.info("当前迭代：{}，是否获取到差异库中存在对应的表：{}", standardTable.getTableName(), customTable == null ? "不存在" : "存在");
            /**
             * 旧库缺表，则应当创建
             */
            if (customTable == null) {

                sqlStatements.add("-- 客户端库中缺少表：" + standardTable.getTableName() + "，创建语句如下：");
                //sqlStatements.add("-- DROP TABLE IF EXISTS " + standardTable.getTableName() + ";\n");
                logger.info("创建语句：{}", JSONUtil.toJsonStr(standardTable));
                logger.info("generateCreateTableStatement:{}", databaseService.generateCreateTableSql(standardTable));
                sqlStatements.add(databaseService.generateCreateTableSql(standardTable));
                // 生成表结构创建语句
                // 生成建表语句。


                continue;
            }
            /**
             * 获取两库字段
             */
            List<Column> standardColumns = standardTable.getColumns();
            List<Column> customColumns = customTable.getColumns();

            /**
             * 通过标准库轮询比对当前表的字段。
             */
            for (Column standardColumn : standardColumns) {
                Column customColumn = getColumnByName(customColumns, standardColumn.getName());

                if (customColumn == null) {

                    sqlStatements.add("-- 客户端库中表：" + standardTable.getTableName() + "，缺少字段：" + standardColumn.getName() + "，创建语句如下：");
                    sqlStatements.add(databaseService.generateAddColumnSql(standardColumn));

                } else {
                    String columnDifference = getColumnDifference(standardColumn, customColumn);

                    if (!columnDifference.isEmpty()) {

                        sqlStatements.add("-- 客户端库中表：" + standardTable.getTableName() + "，字段不一致,字段：" + standardColumn.getName() + "，修改语句如下：");
                        sqlStatements.add(databaseService.generateDiffColumnSql(standardColumn, customColumn));

                    }

//                    String indexDifference = getIndexDifference(standardColumn, customColumn);
//
//                    if (!indexDifference.isEmpty()) {
//                        sqlStatements.add(indexDifference);
//                    }
                }
            }

            // Add new columns to the custom table
            for (Column customColumn : customColumns) {
                Column standardColumn = getColumnByName(standardColumns, customColumn.getName());

                if (standardColumn == null) {

                    sqlStatements.add("-- 客户端库中表：" + standardTable.getTableName() + "，多余如下字段：" + customColumn.getName() + "，删除语句如下：");
                    // sqlStatements.add("ALTER TABLE " + standardTable.getTableName() + " ADD COLUMN " + getColumnDefinition(customColumn) + ";");
                    sqlStatements.add(databaseService.generateDropColumnSql(customColumn));


                }
            }

            // Compare table comments
            if (!standardTable.getComment().equals(customTable.getComment())) {
                sqlStatements.add("-- 客户端库中表：" + standardTable.getTableName() + "，修改表注释：");
                sqlStatements.add(databaseService.generateUpdateTableCommentSql(standardTable));
            }
        }

        // Add new tables to the custom database
//        for (Table customTable : customTables) {
//            Table standardTable = getTableByName(standardTables, customTable.getTableName());
//
//            if (standardTable == null) {
//                String tableDefinition = getTableDefinition(customTable);
//                sqlStatements.add(tableDefinition);
//            }
//        }

        // Write the SQL script to the output file
        try (PrintWriter writer = new PrintWriter(outputFilePath)) {
            for (String sqlStatement : sqlStatements) {
                writer.println(sqlStatement);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 通过表名获取字段
     *
     * @param tables
     * @param name
     * @return
     */
    private Table getTableByName(List<Table> tables, String name) {
        for (Table table : tables) {
            if (table.getTableName().equals(name)) {
                return table;
            }
        }

        return null;
    }


    /**
     * 通过字段名获取字段
     *
     * @param columns
     * @param name
     * @return
     */
    private Column getColumnByName(List<Column> columns, String name) {
        for (Column column : columns) {
            if (column.getName().equals(name)) {
                return column;
            }
        }

        return null;
    }


    // Get the difference between the indexes of two columns as a SQL statement
//    private String getIndexDifference(Column standardColumn, Column customColumn) {
//        List<Index> standardIndexes = standardColumn.getTableIndexes(standardColumn.getId());
//        List<Index> customIndexes = customDatabaseService.getTableIndexes(customColumn.getId());
//
//        for (Index standardIndex : standardIndexes) {
//            if (!hasIndex(customIndexes, standardIndex)) {
//                return "ALTER TABLE " + standardColumn.getTable().getName() + " DROP INDEX " + standardIndex.getName() + ";";
//            }
//        }
//
//        for (Index customIndex : customIndexes) {
//            if (!hasIndex(standardIndexes, customIndex)) {
//                return "ALTER TABLE " + standardColumn.getTable().getName() + " ADD " + customIndex.getType() + " INDEX " + customIndex.getName() + " (" + customColumn.getName() + ");";
//            }
//        }
//
//        return "";
//    }

    // Check if a list of indexes contains a specific index
    private boolean hasIndex(List<Index> indexes, Index index) {
        for (Index i : indexes) {
            if (i.getName().equals(index.getName())) {
                return true;
            }
        }
        return false;
    }

    private String getColumnDifference(Column standardColumn, Column customColumn) {
        if (!standardColumn.getType().equals(customColumn.getType()) || !standardColumn.getLength().equals(customColumn.getLength())) {
            return customColumn.getName() + " " + customColumn.getType() + "(" + customColumn.getLength() + ")";
        }

        if (!standardColumn.isNullable().equals(customColumn.isNullable())) {
            if (customColumn.isNullable()) {
                return customColumn.getName() + " NULL";
            } else {
                return customColumn.getName() + " NOT NULL";
            }
        }

        return "";
    }
}
