package com.keesur.tools.autocode.file.access.dto;

import com.keesur.tools.autocode.enums.DataTypeEnum;

import java.io.Serializable;

public class ColumnDto implements Serializable {

    private String columnName;

    private String javaName;

    private DataTypeEnum columnType;

    private String comment;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public DataTypeEnum getColumnType() {
        return columnType;
    }

    public void setColumnType(DataTypeEnum columnType) {
        this.columnType = columnType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getJavaName() {
        return javaName;
    }

    public void setJavaName(String javaName) {
        this.javaName = javaName;
    }
}
