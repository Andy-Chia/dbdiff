package com.andycoder.dbdiff.service;

import com.andycoder.dbdiff.dto.Column;
import com.andycoder.dbdiff.dto.Table;

public interface SqlGenerator {
    public String generateCreateTableSql(Table table);

    public String generateUpdateTableCommentSql(Table table);


    public String generateAddColumnSql(Column column);


    public String generateModifyColumnSql(Column standardColumn, Column customColumn);

    public String generateDropColumnSql(Column column);
}
