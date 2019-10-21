package com.keesur.tools.autocode.file.access.write;

import com.keesur.tools.autocode.file.access.dto.TableDto;
import com.keesur.tools.autocode.util.FieldUtil;

import java.io.Serializable;

public class MapperBaseJavaWriter implements Serializable {

    private final String className = "BaseMapper";
    private final String fileName = className+".java";
    private static  final String GSH_SP="    ";
    public MapperBaseJavaWriter() {
    }
    public String getFileName() {
        return fileName;
    }
    public String getContent(String basePackage){
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(basePackage).append(".dao;\n\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.io.Serializable;\n\n\n");
        sb.append("public interface BaseMapper<T extends Serializable> {\n\n");
        sb.append(GSH_SP + "int deleteByPrimaryKey(Long id);\n\n");
        sb.append(GSH_SP + "int insert(T data);\n\n");
        sb.append(GSH_SP + "int insertSelective(T data);\n\n");
        sb.append(GSH_SP + "T selectByPrimaryKey(Long id);\n\n");
        sb.append(GSH_SP + "int updateByPrimaryKeySelective(T data);\n\n");
        sb.append(GSH_SP + "int updateByPrimaryKey(T data);\n\n");
        sb.append(GSH_SP + "<Q extends Serializable>  List<T> findByCondition(Q query);\n\n");
        sb.append("}");
        return sb.toString();
    }
}
