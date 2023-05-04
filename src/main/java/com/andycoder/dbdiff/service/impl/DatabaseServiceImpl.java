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
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(table.getTableName());
        sb.append(" (\n\t");

        List<Column> columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            sb.append(column.getName());
            sb.append(" ");
            sb.append(column.getType());
            if (column.getLength() != null && !column.getLength().isEmpty()) {
                sb.append("(");
                sb.append(column.getLength());
                sb.append(")");
            }
            if (!column.isNullable()) {
                sb.append(" NOT NULL");
            }
            if (!column.getDefaultValue().isEmpty()) {
                sb.append(" DEFAULT ");
                sb.append(column.getDefaultValue());
            }
            if (!column.getComment().isEmpty()) {
                sb.append(" COMMENT '");
                sb.append(column.getComment());
                sb.append("'");
            }
            if (i < columns.size() - 1) {
                sb.append(", \n\t");
            }
        }

        if (table.getPrimaryKey() != null) {
            sb.append(", PRIMARY KEY (");
            sb.append(table.getPrimaryKey().getColumnStr());
            sb.append(")");
        }

        List<Index> indexes = table.getIndexes();
        for (Index index : indexes) {
            sb.append(", \n\t");
            if (index.getNonUnique()) {
                sb.append("INDEX ");
            } else {
                sb.append("UNIQUE INDEX ");
            }
            sb.append(index.getName());
            sb.append(" (");
            sb.append(index.getColumnStr());
            sb.append(")");
        }

        if (!table.getComment().isEmpty()) {
            sb.append("\n) COMMENT='");
            sb.append(table.getComment());
            sb.append("';");
        } else {
            sb.append("\n);");
        }

        return sb.toString();
    }

    @Override
    public String generateUpdateTableCommentSql(Table table) {

        return "ALTER TABLE " + table.getTableName() + " COMMENT '" + table.getComment() + "';";
    }

    @Override
    public String generateAddColumnSql( Column column) {
        return "ALTER TABLE " + column.getTableName() + " ADD COLUMN " + getColumnDefinition(column) + ";";
    }

    @Override
    public String generateDiffColumnSql(Column standardColumn, Column customColumn) {

        return "ALTER TABLE " + standardColumn.getTableName() + " MODIFY COLUMN ;";
    }

    @Override
    public String generateDropColumnSql(Column column) {
        return "ALTER TABLE " + column.getTableName() + " DROP COLUMN " + column.getName() + ";";
    }

    // Get the definition of a column as a SQL statement
    private String getColumnDefinition(Column column) {
        StringBuilder sb = new StringBuilder();
        sb.append(column.getName()).append(" ");
        sb.append(column.getType()).append("(").append(column.getLength()).append(") ");
        if (!column.isNullable()) {
            sb.append("NOT NULL ");
        }
        if (column.getDefaultValue() != null) {
            sb.append("DEFAULT '").append(column.getDefaultValue()).append("' ");
        }
        return sb.toString().trim();
    }

    // Get the difference between two columns as a SQL statement

}
