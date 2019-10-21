package com.keesur.tools.autocode.file.access.write;

import com.keesur.tools.autocode.file.access.dto.ColumnDto;
import com.keesur.tools.autocode.file.access.dto.TableDto;
import com.keesur.tools.autocode.util.FieldUtil;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class EntityJavaWriter implements Serializable {

    private TableDto tableDto;
    private String fileName;

    private static final String KG = "    ";

    public EntityJavaWriter(TableDto tableDto) {
        this.tableDto = tableDto;
        this.tableDto = tableDto;
        String entityJavaName = FieldUtil.upperFirstLetter(FieldUtil.underlineFilter(tableDto.getTableName()));
        if(tableDto.getTableName().indexOf("T_") >= 0){
            entityJavaName = entityJavaName.substring(1,entityJavaName.length());
        }
        this.fileName = entityJavaName+".java";
    }

    public String getFileName() {
        return fileName;
    }

    public String getContent(String basePackage){

        String entityJavaName = FieldUtil.upperFirstLetter(FieldUtil.underlineFilter(tableDto.getTableName()));
        if(tableDto.getTableName().indexOf("T_") >= 0){
            entityJavaName = entityJavaName.substring(1,entityJavaName.length());
        }


        Set<String> classSet = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(basePackage).append(".entity;\n\n");
        sb.append("import java.io.Serializable;\n");
        if(null != tableDto.getColumns() && tableDto.getColumns().size() > 0){
            for(ColumnDto columnDto : tableDto.getColumns()){
                if(columnDto.getColumnType().getJavaType().indexOf("java.lang.") != 0 && !classSet.contains(columnDto.getColumnType().getJavaType())){
                    sb.append("import "+columnDto.getColumnType().getJavaType()+";\n");
                    classSet.add(columnDto.getColumnType().getJavaType());
                }
            }
        }
        sb.append("import lombok.Data;\n\n\n");
        sb.append("@Data\n");
        sb.append("public class ").append(entityJavaName).append(" implements Serializable {\n");
        if(null != tableDto.getColumns() && tableDto.getColumns().size() > 0){
            for(ColumnDto columnDto : tableDto.getColumns()){
                sb.append("\n");
                sb.append(KG).append("/**\n");
                if(null != columnDto.getComment()){
                    sb.append(KG).append(" *   ").append(columnDto.getComment()).append("\n");
                }
                sb.append(KG).append(" */\n");
                sb.append(KG).append("private ").append(columnDto.getColumnType().getJavaType().substring(columnDto.getColumnType().getJavaType().lastIndexOf(".")+1)).append(" ").append(columnDto.getJavaName()).append(";\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
