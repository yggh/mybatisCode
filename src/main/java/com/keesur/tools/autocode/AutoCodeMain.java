package com.keesur.tools.autocode;

import com.keesur.tools.autocode.file.access.dto.ColumnDto;
import com.keesur.tools.autocode.file.access.dto.TableDto;
import com.keesur.tools.autocode.file.access.read.ReadCreateSql;
import com.keesur.tools.autocode.file.access.write.EntityJavaWriter;
import com.keesur.tools.autocode.file.access.write.MapperBaseJavaWriter;
import com.keesur.tools.autocode.file.access.write.MapperJavaWriter;
import com.keesur.tools.autocode.file.access.write.MapperXmlWriter;
import com.keesur.tools.autocode.util.FieldUtil;
import com.keesur.tools.autocode.util.FileUtil;
import com.keesur.tools.autocode.util.XmlFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class AutoCodeMain {

    public static void main(String[] args) {
        System.out.println(" welcome ! ");
        Scanner scanner = new Scanner(System.in);
        String sqlPah = null;
        String packageName = null;
        String path = null;
        while (true){
            System.out.println();
            System.out.println("Please enter the full path of the file where the table creation statement is located :");
            sqlPah = scanner.nextLine();
            System.out.println();
            System.out.println("  your file path :");
            System.out.println("    "+sqlPah);
            System.out.println("confirm (y/n):");
            if("y".equals(scanner.nextLine())){
                File file = new File(sqlPah);
                if(file.exists() && file.isFile()){
                    path = file.getParentFile().getAbsolutePath();
                    break;
                }else{
                    System.out.println("  The file not exists! Please re-enter");
                }
            }
        }
        while (true){
            System.out.println();
            System.out.println("Please enter the base package name of your project :");
            packageName = scanner.nextLine();
            System.out.println();
            System.out.println("  your package :");
            System.out.println("    "+ packageName);
            System.out.println("confirm (y/n):");
            if("y".equals(scanner.nextLine())){
                break;
            }
        }


        try {
            List<TableDto> tables = ReadCreateSql.readTables(sqlPah);
            FileUtil.createFolder(path + File.separator + "dao");
            MapperBaseJavaWriter mapperBaseJavaWriter = new MapperBaseJavaWriter();
            FileUtil.write(path + File.separator + "dao"+File.separator +mapperBaseJavaWriter.getFileName(),mapperBaseJavaWriter.getContent(packageName));
            FileUtil.createFolder(path + File.separator + "entity");
            for (TableDto dto : tables) {
                System.out.println("表"+dto.getTableName()+"的文件生成中。。。");
                MapperXmlWriter mapperXmlWriter = new MapperXmlWriter(dto);
                FileUtil.write(path + File.separator + "dao"+File.separator +mapperXmlWriter.getFileName(),mapperXmlWriter.getContent(packageName));
                MapperJavaWriter mapperJavaWriter = new MapperJavaWriter(dto);
                FileUtil.write(path + File.separator + "dao"+File.separator +mapperJavaWriter.getFileName(),mapperJavaWriter.getContent(packageName));
                EntityJavaWriter entityJavaWriter = new EntityJavaWriter(dto);
                FileUtil.write(path + File.separator + "entity"+File.separator +entityJavaWriter.getFileName(),entityJavaWriter.getContent(packageName));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
