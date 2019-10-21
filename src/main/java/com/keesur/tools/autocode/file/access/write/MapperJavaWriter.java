package com.keesur.tools.autocode.file.access.write;

import com.keesur.tools.autocode.file.access.dto.TableDto;
import com.keesur.tools.autocode.util.FieldUtil;

import java.io.File;
import java.io.Serializable;

public class MapperJavaWriter implements Serializable {

    private TableDto tableDto;
    private String fileName;
    public MapperJavaWriter(TableDto tableDto) {
        this.tableDto = tableDto;
        String entityJavaName = FieldUtil.upperFirstLetter(FieldUtil.underlineFilter(tableDto.getTableName()));
        if(tableDto.getTableName().indexOf("T_") >= 0){
            entityJavaName = entityJavaName.substring(1,entityJavaName.length());
        }
        this.fileName = entityJavaName + "Mapper.java";
    }
    public String getFileName() {
        return fileName;
    }
    public String getContent(String basePackage){
        String entityJavaName = FieldUtil.upperFirstLetter(FieldUtil.underlineFilter(tableDto.getTableName()));
        if(tableDto.getTableName().indexOf("T_") >= 0){
            entityJavaName = entityJavaName.substring(1,entityJavaName.length());
        }
        String entityName = basePackage + ".entity."+ entityJavaName;
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(basePackage).append(".dao;\n\n");
        sb.append("import ").append(entityName).append(";\n");
        sb.append("import org.apache.ibatis.annotations.Mapper;\n\n\n");
        sb.append("@Mapper\n");
        sb.append("public interface ").append(entityJavaName).append("Mapper").append(" extends BaseMapper<").append(entityJavaName).append(">{\n\n\n}");
        return sb.toString();
    }
}
