package com.keesur.tools.autocode.file.access.dto;

import java.io.Serializable;
import java.util.List;

public class TableDto implements Serializable {

    private String tableName;

    private String tableComment;

    private ColumnDto primaryColumn;

    private List<ColumnDto> columns;

    public TableDto() {
    }

    public TableDto(TableDto tableDto){
        this.tableName = tableDto.getTableName();
        this.columns = tableDto.getColumns();
        this.primaryColumn = tableDto.getPrimaryColumn();
        this.tableComment = tableDto.getTableComment();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public ColumnDto getPrimaryColumn() {
        return primaryColumn;
    }

    public void setPrimaryColumn(ColumnDto primaryColumn) {
        this.primaryColumn = primaryColumn;
    }

    public List<ColumnDto> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDto> columns) {
        this.columns = columns;
    }
}
