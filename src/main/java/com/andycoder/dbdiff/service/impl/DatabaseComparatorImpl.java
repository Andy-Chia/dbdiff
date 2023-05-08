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
//                logger.info("创建语句：{}", JSONUtil.toJsonStr(standardTable));
//                logger.info("generateCreateTableStatement:{}", databaseService.generateCreateTableSql(standardTable));
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
                    Boolean columnDifference = getColumnDifference(standardColumn, customColumn);
                    if (!columnDifference) {
                        sqlStatements.add("-- 客户端库中表：" + standardTable.getTableName() + "，字段不一致,字段：" + standardColumn.getName() + "，修改语句如下：");
                        sqlStatements.add(databaseService.generateDiffColumnSql(standardColumn, customColumn));
                    }
                }
            }

            List<Index> standardIndexes = standardTable.getIndexes();
            List<Index> customIndexes = customTable.getIndexes();
            for (Index standardIndex : standardIndexes) {
                Index indexByName = getIndexByName(standardIndex, customIndexes);
                if (indexByName == null) {
                    // 索引根据名字没找到两种情况，
                    // 第一种，这个索引真的没有，
                    // 第二种，这个索引在，但是索引名称不对。
                    // 所以先进行根据索引内容查找。
                    Index indexByIndexInfo = getIndexByIndexInfo(standardIndex, customIndexes);
                    if (indexByIndexInfo == null) {
                        sqlStatements.add("-- 客户端库中表：" + standardTable.getTableName() + "，缺少索引：" + standardIndex.getName() + "，创建语句如下：");
                        sqlStatements.add(databaseService.generateAddIndexSql(standardIndex));
                    } else {
                        // 判断索引除了名字，之外的信息是否一致
                        boolean indexDifference = getIndexDifference(standardIndex, indexByIndexInfo);
                        if (indexDifference) {
                            // 重命名索引

                            sqlStatements.add("-- 客户端库中表：" + standardTable.getTableName() + "，存在索引,列值为：" + standardIndex.getColumnStr() + "，重命名索引：");
                            sqlStatements.add(databaseService.generateRenameIndexSql(standardIndex, indexByIndexInfo));

                        } else {
                            // 修改索引。
                            sqlStatements.add("-- 客户端库中表：" + standardTable.getTableName() + "，存在索引,列值为：" + standardIndex.getColumnStr() + "，重建索引：");
                            sqlStatements.add(databaseService.generateDeleteIndexSql( indexByIndexInfo));
                            sqlStatements.add(databaseService.generateAddIndexSql(standardIndex));
                        }

                    }

                } else {
                    // 比对索引
                    boolean indexDifference = getIndexDifference(standardIndex, indexByName);
                    if (!indexDifference) {
                        // 修改索引。
                        sqlStatements.add("-- 客户端库中表：" + standardTable.getTableName() + "，存在索引,列值为：" + standardIndex.getColumnStr() + "，重建索引：");
                        sqlStatements.add(databaseService.generateDeleteIndexSql( indexByName));
                        sqlStatements.add(databaseService.generateAddIndexSql(standardIndex));
                    }
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
//                return "ALTER TABLE " + standardColumn.getTableName() + " ADD " + customIndex.getType() + " INDEX " + customIndex.getName() + " (" + customColumn.getName() + ");";
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

    private Index getIndexByName(Index index, List<Index> indexes) {
        for (Index i : indexes) {
            if (i.getName().equals(index.getName())) {
                return i;
            }
        }
        return null;
    }

    private Index getIndexByIndexInfo(Index index, List<Index> indexes) {
        for (Index i : indexes) {
            if (i.getColumnStr().equals(index.getColumnStr())) {
                return i;
            }
        }
        return null;
    }

    private boolean getColumnDifference(Column standardColumn, Column customColumn) {
        if (compareString(standardColumn.getType(), customColumn.getType())
                && compareString(standardColumn.getLength(), customColumn.getLength())
                && compareString(standardColumn.getComment(), customColumn.getComment())
                && compareString(standardColumn.getCharset(), customColumn.getCharset())
                && compareString(standardColumn.getCollate(), customColumn.getCollate())
                && compareString(standardColumn.getExtend(), customColumn.getExtend())
                && standardColumn.isNullable().equals(customColumn.isNullable())) {
            return true;
        }

        return false;
    }

    private boolean getIndexDifference(Index standardIndex, Index customIndex) {
        if (compareString(standardIndex.getColumnStr(), customIndex.getColumnStr())
                && compareString(standardIndex.getComment(), customIndex.getComment())
                && compareString(standardIndex.getIndexType(), customIndex.getIndexType())
                && standardIndex.getNonUnique().equals(customIndex.getNonUnique())) {
            return true;
        }

        return false;
    }

    private boolean compareString(String a, String b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null && b != null) {
            return false;
        }
        if (a.equals(b)) {
            return true;
        }
        return false;
    }


}
