package com.andycoder.dbdiff.service.impl;

import com.andycoder.dbdiff.dao.custom.CustomDatabaseMapper;
import com.andycoder.dbdiff.dao.standard.StandardDatabaseMapper;
import com.andycoder.dbdiff.dto.Column;
import com.andycoder.dbdiff.dto.Index;
import com.andycoder.dbdiff.dto.Table;
import com.andycoder.dbdiff.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseServiceImpl implements DatabaseService {

    @Value("${standarddb.dbname}")
    private String standarddbname;
    @Value("${customdb.dbname}")
    private String customdbname;
    @Autowired
    private CustomDatabaseMapper customDatabaseMapper;
    @Autowired
    private StandardDatabaseMapper standardDatabaseMapper;

    MysqlSqlGeneratorImpl sqlGenerator = new MysqlSqlGeneratorImpl();

    @Override
    public List<Table> getStandardTableList() {
        // 这里执行完应该只有 表名称和注释两个字段。
        List<Table> tableList = standardDatabaseMapper.getTableList(standarddbname);
        // 循环表，去对应字段补全。
        for (Table t : tableList) {
            // 获取字段信息
            List<Column> columnList = standardDatabaseMapper.getColumnList(standarddbname, t.getTableName());
            t.setColumns(columnList);
            Index primaryKey = standardDatabaseMapper.getPrimaryKey(standarddbname, t.getTableName());
            t.setPrimaryKey(primaryKey);
            List<Index> indexList = standardDatabaseMapper.getIndexList(standarddbname, t.getTableName());
            t.setIndexes(indexList);
        }
        return tableList;
    }

    @Override
    public List<Table> getCustomTableList() {
        // 这里执行完应该只有 表名称和注释两个字段。
        List<Table> tableList = customDatabaseMapper.getTableList(customdbname);
        // 循环表，去对应字段补全。
        for (Table t : tableList) {
            // 获取字段信息
            List<Column> columnList = customDatabaseMapper.getColumnList(customdbname, t.getTableName());
            t.setColumns(columnList);
            Index primaryKey = customDatabaseMapper.getPrimaryKey(customdbname, t.getTableName());
            t.setPrimaryKey(primaryKey);
            List<Index> indexList = customDatabaseMapper.getIndexList(customdbname, t.getTableName());
            t.setIndexes(indexList);
        }
        return tableList;
    }

    @Override
    public String generateCreateTableSql(Table table) {
        return sqlGenerator.generateCreateTableSql(table);
    }

    @Override
    public String generateUpdateTableCommentSql(Table table) {
        return sqlGenerator.generateUpdateTableCommentSql(table);
    }

    @Override
    public String generateAddColumnSql(Column column) {
        return sqlGenerator.generateAddColumnSql(column);
    }

    @Override
    public String generateDiffColumnSql(Column standardColumn, Column customColumn) {
        return sqlGenerator.generateModifyColumnSql(standardColumn, customColumn);
    }

    @Override
    public String generateDropColumnSql(Column column) {
        return sqlGenerator.generateDropColumnSql(column);
    }

    @Override
    public String generateAddIndexSql(Index index) {
        return sqlGenerator.generateAddIndexSql(index);
    }

    @Override
    public String generateRenameIndexSql(Index standardIndex, Index customIndex) {
        return sqlGenerator.generateRenameIndexSql(standardIndex, customIndex);

    }

    @Override
    public String generateDeleteIndexSql(Index index) {
        return sqlGenerator.generateDeleteIndexSql(index);
    }


}
