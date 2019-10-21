package com.keesur.tools.autocode.file.access.read;

import com.keesur.tools.autocode.enums.DataTypeEnum;
import com.keesur.tools.autocode.file.access.dto.ColumnDto;
import com.keesur.tools.autocode.file.access.dto.TableDto;
import com.keesur.tools.autocode.util.FieldUtil;
import com.keesur.tools.autocode.util.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ReadCreateSql {
    private static final String CREATE_TABLE_START = "CREATETABLE";
    private static final String CREATE_TABLE_END = ")";
    private static final String COMMENT = "COMMENT";
    private static final String PRIMARY_KEY_START = "PRIMARYKEY";
    private static final String UNIQUE_KEY_START = "UNIQUEKEY";
    private static final String KEY_START = "KEY";

    public static List<TableDto> readTables(String filePath) throws FileNotFoundException {
        List<String> fileContents = FileUtil.readFileLines(filePath);
        List<TableDto> tableDtos = new ArrayList<>();

        if (null != fileContents && fileContents.size() > 0) {
            boolean readAble = false;
            String lineFlagStr = null;
            TableDto tableDto = null;
            String tableName = null;

            String primaryKeyName = null;
            List<ColumnDto> columnDtos = null;
            for (String fileLine : fileContents) {
                fileLine = fileLine.toUpperCase().trim();
                lineFlagStr = fileLine.replaceAll("\\s*", "");
                if (lineFlagStr.trim().length() < 2) {
                    continue;
                }
                if (lineFlagStr.indexOf(CREATE_TABLE_START) >= 0) {
                    readAble = true;
                    tableDto = new TableDto();
                    columnDtos = new ArrayList<>();
                    tableName = lineFlagStr.substring(lineFlagStr.indexOf(CREATE_TABLE_START) + CREATE_TABLE_START.length(), lineFlagStr.indexOf("("));
                    if (null == tableName) {
                        throw new RuntimeException("未找到建表表名：" + fileLine);
                    }
                    tableName = tableName.replaceAll("`", "");
                    tableDto.setTableName(tableName);
                    continue;
                } else if (lineFlagStr.startsWith(CREATE_TABLE_END)) {
                    readAble = false;
                    if (null == tableDto) {
                        throw new RuntimeException("未找到‘)’前面的建表关键字：" + fileLine);
                    }
                    tableDto.setTableComment(getComment(fileLine));
                    if(null != primaryKeyName){
                        if(null == columnDtos || columnDtos.size() == 0){
                            throw new RuntimeException("无效的：primaryKey->" + primaryKeyName);
                        }
                        for(ColumnDto columnDto : columnDtos){
                            if(primaryKeyName.equals(columnDto.getColumnName())){
                                tableDto.setPrimaryColumn(columnDto);
                            }
                        }
                        if(null == tableDto.getPrimaryColumn()){
                            throw new RuntimeException("无效的：primaryKey->" + primaryKeyName);
                        }
                    }
                    tableDto.setColumns(new ArrayList(columnDtos));
                    tableDtos.add(new TableDto(tableDto));
                    columnDtos = null;
                    tableDto = null;
                    continue;
                } else if (lineFlagStr.startsWith(PRIMARY_KEY_START)) {
                    primaryKeyName = lineFlagStr.substring(lineFlagStr.indexOf("(") + 1, lineFlagStr.indexOf(")"));
                    if (null != primaryKeyName && primaryKeyName.length() > 0) {
                        primaryKeyName = primaryKeyName.replaceAll("`", "");
                    }
                    continue;
                } else if (lineFlagStr.startsWith(UNIQUE_KEY_START) || lineFlagStr.startsWith(KEY_START)) {
                    continue;
                }

                if (readAble) {
                    if (null == tableDto) {
                        throw new RuntimeException("未找到前面的建表关键字：" + fileLine);
                    }
                    DataTypeEnum typeEnum = null;
                    ColumnDto columnDto = new ColumnDto();
                    int typeStart = 0;
                    int typeStartW = 0;
                    int start = 0;
                    String columnName = null;
                    for (DataTypeEnum dataTypeEnum : DataTypeEnum.values()) {
                        typeStart = fileLine.indexOf(" " + dataTypeEnum.getJdbcType().toUpperCase() + " ");
                        if (typeStart > 0) {
                            start = typeStart;
                        } else {
                            typeStartW = fileLine.indexOf(" " + dataTypeEnum.getJdbcType().toUpperCase() + "(");
                            if (typeStartW > 0) {
                                start = typeStartW;
                            } else {
                                if (lineFlagStr.endsWith(dataTypeEnum.getJdbcType().toUpperCase()) || lineFlagStr.endsWith(dataTypeEnum.getJdbcType().toUpperCase() + ",")) {
                                    start = fileLine.indexOf(" " + dataTypeEnum.getJdbcType().toUpperCase());
                                }
                            }
                        }
                        if (start > 0) {
                            typeEnum = dataTypeEnum;
                            columnName = fileLine.substring(fileLine.indexOf(lineFlagStr.substring(0, 1)), start);
                            break;
                        }
                    }
                    if (null == typeEnum || null == columnName) {
                        continue;
                    }
                    columnName = columnName.replaceAll("`", "");
                    columnDto.setColumnType(typeEnum);
                    columnDto.setColumnName(columnName);
                    columnDto.setJavaName(FieldUtil.underlineFilter(columnName));
                    columnDto.setComment(getComment(fileLine));
                    columnDtos.add(columnDto);
                }
            }
        }
        return tableDtos;
    }

    private static String getComment(String fileLine) {
        String commentStr = " " + COMMENT + " ";
        String commentStrW = " " + COMMENT + "=";
        String comment = null;
        int commentStart = 0;
        int commentEnd = 0;
        if (fileLine.indexOf(commentStr) > 0) {
            commentStart = fileLine.indexOf("'", fileLine.indexOf(commentStr))+1;
            commentEnd = fileLine.indexOf("'", commentStart);
        } else if (fileLine.indexOf(commentStrW) > 0) {
            commentStart = fileLine.indexOf("'", fileLine.indexOf(commentStrW))+1;
            commentEnd = fileLine.indexOf("'", commentStart);
        }
        if (commentStart > 0) {
            comment = fileLine.substring(commentStart, commentEnd);
        }
        return comment;
    }

    public static void main(String[] args) {
        System.out.println(" Hello World! ");
        try {
            List<TableDto> tables = ReadCreateSql.readTables("D:\\study-code\\111.sql");
            for (TableDto dto : tables) {
                System.out.println("-------------------------------------------------------");
                System.out.println(dto.getTableName() + "  -   " + dto.getTableComment());
                if(null != dto.getPrimaryColumn()){
                    System.out.println( " primary: " + dto.getPrimaryColumn().getColumnName());
                }
                for(ColumnDto columnDto : dto.getColumns()){
                    System.out.println(columnDto.getColumnName() + " " + columnDto.getColumnType().getJdbcType() + " " + columnDto.getComment());
                }
                System.out.println("-------------------------------------------------------");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
