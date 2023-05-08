package com.andycoder.dbdiff.service.impl;

import cn.hutool.json.JSONUtil;
import com.andycoder.dbdiff.constants.ColumTypeConstant;
import com.andycoder.dbdiff.dto.Column;
import com.andycoder.dbdiff.dto.Index;
import com.andycoder.dbdiff.dto.Table;
import com.andycoder.dbdiff.service.SqlGenerator;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MysqlSqlGeneratorImpl implements SqlGenerator {
    @Override
    public String generateCreateTableSql(Table table) {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + table.getTableName() + " (\n");

        // Add columns
        List<Column> columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);

            String columnDefinition = getColumnDefinition(column);

            sql.append(columnDefinition);


            // Add comma separator between columns
            if (i < columns.size() - 1) {
                sql.append(", \n");
            }
        }

        // Add primary key
        Index primaryKey = table.getPrimaryKey();
        if (primaryKey != null) {
            sql.append(", PRIMARY KEY (").append(primaryKey.getColumnStr()).append(")");
        }

        // Add indexes
        List<Index> indexes = table.getIndexes();
        for (Index index : indexes) {
            if (ColumTypeConstant.mysqlIngnoreIndexList().contains(index.getName())) {
                continue;
            }
            sql.append(", \n\t");
            String indexDefinition = getIndexDefinition(index);
            sql.append(indexDefinition);
        }

        // Add table options
        if (!table.getComment().equals("")) {
            sql.append("\n) ENGINE=").append(table.getEngine())
                    .append(" DEFAULT CHARSET=").append(table.getCharset())
                    .append(" COLLATE=").append(table.getCollate())
                    .append(" COMMENT='").append(table.getComment()).append("'");
        } else {
            sql.append("\n) ENGINE=").append(table.getEngine())
                    .append(" DEFAULT CHARSET=").append(table.getCharset())
                    .append(" COLLATE=").append(table.getCollate());
        }

        return sql.toString();


    }

    @Override
    public String generateUpdateTableCommentSql(Table table) {

        return "ALTER TABLE " + table.getTableName() + " COMMENT '" + table.getComment() + "';";
    }

    @Override
    public String generateAddColumnSql(Column column) {
        return "ALTER TABLE " + column.getTableName() + " ADD COLUMN " + getColumnDefinition(column) + ";";
    }

    @Override
    public String generateModifyColumnSql(Column standardColumn, Column customColumn) {
        return "ALTER TABLE " + standardColumn.getTableName() + " MODIFY COLUMN " + getColumnDefinition(standardColumn) + ";";
    }

    @Override
    public String generateDropColumnSql(Column column) {


        return "ALTER TABLE " + column.getTableName() + " DROP COLUMN " + column.getName() + ";";
    }

    @Override
    public String generateAddIndexSql(Index index) {
        return "ALTER TABLE " + index.getTableName() + " ADD   " + getIndexDefinition(index) + ";";
    }

    @Override
    public String generateRenameIndexSql(Index standardIndex, Index customIndex) {
        return "ALTER TABLE " + customIndex.getTableName() + "  RENAME INDEX `" + customIndex.getName() + "` TO `" + standardIndex.getName() + "`;";
    }

    @Override
    public String generateDeleteIndexSql(Index index) {
        return "ALTER TABLE " + index.getTableName() + " DROP INDEX `" + index.getName() + "`;";
    }

    private String getColumnDefinition(Column column) {
        StringBuilder sql = new StringBuilder();
        sql.append("\t`").append(column.getName()).append("` ").append(column.getType());
        if (StringUtils.isNotEmpty(column.getLength())) {
            sql.append("(").append(column.getLength()).append(") ");
        }

        // Add charset and collate if specified
        if (column.getCharset() != null && column.getCollate() != null) {
            sql.append("CHARACTER SET ").append(column.getCharset())
                    .append(" COLLATE ").append(column.getCollate()).append(" ");
        }

        if (!column.isNullable()) {
            sql.append(" NOT NULL ");
        }
        // 默认值
        if (column.getDefaultValue() != null) {
            // 此处应当判断是否为空，另外，如果是时间类型，默认值需要判断是否是自动生成的相关关键字
            if (ColumTypeConstant.mysqlDateTypeList().contains(column.getType())
                    && (ColumTypeConstant.MYSQL_CURRENT_TIMESTAMP.equals(column.getDefaultValue()))) {
                sql.append(" DEFAULT ").append(column.getDefaultValue()).append(" ");
            } else {
                sql.append(" DEFAULT '").append(column.getDefaultValue()).append("' ");
            }
        }
        // 扩展操作
        if (StringUtils.isNotEmpty(column.getExtend())
                && !ColumTypeConstant.mysqlIngnoreExtraList().contains(column.getExtend())) {
            String extend = column.getExtend();
            sql.append(" ").append(ColumTypeConstant.mysqlIngnoreExtraList(extend)).append(" ");
        }


        if (StringUtils.isNotEmpty(column.getComment())) {
            sql.append(" COMMENT '").append(column.getComment()).append("' ");
        }
        return sql.toString();
    }

    private String getIndexDefinition(Index index) {
        StringBuilder sql = new StringBuilder();
        if (ColumTypeConstant.mysqlIngnoreIndexList().contains(index.getName())) {
            return "";
        }
        if (index.getNonUnique()) {
            sql.append("INDEX ");
        } else {
            sql.append("UNIQUE INDEX ");
        }
        // 拼接索引字段，避免多列索引的情况。
        sql.append("`").append(index.getName()).append("` (");
        List<String> cnames = new ArrayList();
        for (String cicolumn : index.getColumns()) {
            cnames.add("`" + cicolumn + "`");
        }
        sql.append(String.join(",", cnames)).append(")");
        if (StringUtils.isNotEmpty(index.getIndexType())) {
            sql.append(" USING ").append(index.getIndexType());
        }
        if (StringUtils.isNotEmpty(index.getComment())) {
            sql.append(" COMMENT '").append(index.getComment()).append("'");
        }
        return sql.toString();
    }

    public static void main(String[] args) {
        Table table = JSONUtil.toBean("{\"tableName\":\"gomain_role\",\"columns\":[{\"name\":\"role_id\",\"type\":\"varchar\",\"length\":\"64\",\"isNullable\":false,\"defaultValue\":\"0\",\"comment\":\"角色主键id\",\"charset\":\"utf8mb4\",\"collate\":\"utf8mb4_bin\",\"extend\":\"\"},{\"name\":\"role_name\",\"type\":\"varchar\",\"length\":\"100\",\"isNullable\":false,\"defaultValue\":\"\",\"comment\":\"角色名称\",\"charset\":\"utf8mb4\",\"collate\":\"utf8mb4_bin\",\"extend\":\"\"},{\"name\":\"role_desc\",\"type\":\"varchar\",\"length\":\"255\",\"isNullable\":false,\"defaultValue\":\"\",\"comment\":\"角色描述，前端展示用字段。\",\"charset\":\"utf8mb4\",\"collate\":\"utf8mb4_bin\",\"extend\":\"\"},{\"name\":\"role_type\",\"type\":\"tinyint\",\"length\":\"\",\"isNullable\":false,\"defaultValue\":\"-1\",\"comment\":\"角色类型，０：操作员，1：初始化管理员，2：系统管理员，3：安全管理员，4：业务管理员，5：安全审计员，6：审批员，7:普通业务管理员，后续扩展，各个业务自己加\",\"extend\":\"\"},{\"name\":\"dept_id\",\"type\":\"varchar\",\"length\":\"64\",\"isNullable\":false,\"defaultValue\":\"\",\"comment\":\"部门id\",\"charset\":\"utf8mb4\",\"collate\":\"utf8mb4_bin\",\"extend\":\"\"},{\"name\":\"priority\",\"type\":\"int\",\"length\":\"\",\"isNullable\":false,\"defaultValue\":\"-1\",\"comment\":\"排序\",\"extend\":\"\"},{\"name\":\"remark\",\"type\":\"varchar\",\"length\":\"200\",\"isNullable\":false,\"defaultValue\":\"\",\"comment\":\"备注\",\"charset\":\"utf8mb4\",\"collate\":\"utf8mb4_bin\",\"extend\":\"\"},{\"name\":\"status\",\"type\":\"tinyint\",\"length\":\"\",\"isNullable\":false,\"defaultValue\":\"0\",\"comment\":\"状态0:启用，1停用\",\"extend\":\"\"},{\"name\":\"operator_id\",\"type\":\"varchar\",\"length\":\"64\",\"isNullable\":false,\"defaultValue\":\"\",\"comment\":\"修改用户id\",\"charset\":\"utf8mb4\",\"collate\":\"utf8mb4_bin\",\"extend\":\"\"},{\"name\":\"operate_time\",\"type\":\"datetime\",\"length\":\"\",\"isNullable\":false,\"defaultValue\":\"1970-01-01 00:00:00\",\"comment\":\"修改时间\",\"extend\":\"on update CURRENT_TIMESTAMP\"},{\"name\":\"creator_id\",\"type\":\"varchar\",\"length\":\"64\",\"isNullable\":false,\"defaultValue\":\"\",\"comment\":\"创建用户id\",\"charset\":\"utf8mb4\",\"collate\":\"utf8mb4_bin\",\"extend\":\"\"},{\"name\":\"create_time\",\"type\":\"datetime\",\"length\":\"\",\"isNullable\":false,\"defaultValue\":\"CURRENT_TIMESTAMP\",\"comment\":\"创建时间\",\"extend\":\"DEFAULT_GENERATED\"},{\"name\":\"is_deleted\",\"type\":\"tinyint\",\"length\":\"\",\"isNullable\":false,\"defaultValue\":\"0\",\"comment\":\"是否删除 0 未删除 1已删除\",\"extend\":\"\"},{\"name\":\"is_show\",\"type\":\"tinyint\",\"length\":\"\",\"isNullable\":false,\"defaultValue\":\"1\",\"comment\":\"是否显示 0 不显示 1 显示\",\"extend\":\"\"},{\"name\":\"is_update\",\"type\":\"tinyint\",\"length\":\"\",\"isNullable\":false,\"defaultValue\":\"0\",\"comment\":\"是否允许页面修改 0 不允许 1 允许\",\"extend\":\"\"},{\"name\":\"is_default\",\"type\":\"tinyint\",\"length\":\"\",\"isNullable\":false,\"defaultValue\":\"0\",\"comment\":\"是否为默认角色 0 不是 1 是\",\"extend\":\"\"},{\"name\":\"have_full_permission\",\"type\":\"tinyint\",\"length\":\"\",\"isNullable\":false,\"defaultValue\":\"0\",\"comment\":\"是否拥有全部权限 0 不是  1 是\",\"extend\":\"\"},{\"name\":\"role_platform\",\"type\":\"tinyint\",\"length\":\"\",\"isNullable\":false,\"defaultValue\":\"-1\",\"comment\":\"角色所属平台或分类，1 运营端  2 web端  默认-1\",\"extend\":\"\"},{\"name\":\"aaaa\",\"type\":\"bigint\",\"length\":\"20\",\"isNullable\":false,\"defaultValue\":\"\",\"comment\":\"dasdfa\",\"extend\":\"auto_increment\"}],\"primaryKey\":{\"name\":\"PRIMARY\",\"columns\":[\"aaaa\"],\"tableName\":\"gomain_role\",\"columnStr\":\"aaaa\"},\"indexes\":[{\"name\":\"idx_name\",\"columns\":[\"role_name\"],\"tableName\":\"gomain_role\",\"nonUnique\":true,\"columnStr\":\"role_name\",\"indexType\":\"BTREE\",\"comment\":\"索引roleName\"},{\"name\":\"idx_role_name_id\",\"columns\":[\"role_id\",\"role_name\"],\"tableName\":\"gomain_role\",\"nonUnique\":true,\"columnStr\":\"role_id,role_name\",\"indexType\":\"BTREE\",\"comment\":\"索引roleNameid\"},{\"name\":\"PRIMARY\",\"columns\":[\"aaaa\"],\"tableName\":\"gomain_role\",\"nonUnique\":false,\"columnStr\":\"aaaa\",\"indexType\":\"BTREE\",\"comment\":\"\"}],\"comment\":\"角色基础信息表\",\"charset\":\"utf8mb4\",\"collate\":\"utf8mb4_bin\",\"engine\":\"InnoDB\"}"
                , Table.class);
        MysqlSqlGeneratorImpl mysqlSqlGenerator = new MysqlSqlGeneratorImpl();
        System.out.println(mysqlSqlGenerator.generateCreateTableSql(table));
    }
}
