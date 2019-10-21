package com.keesur.tools.autocode.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static final String UTF8 = "UTF-8";
    public static final String NEXT_LINE = "\r\n";
    /**
     * 创建文件夹
     *
     * @param folderName
     */
    public static void createFolder(String folderName) {
        File file = new File(folderName);
        if (file.exists()) {
            return;
        }
        file.mkdirs();
    }
    /**
     * 写文件
     */
    public static void write(String fileName, String txt, String charset) {
        OutputStreamWriter osw = null;
        try {
            if (charset == null) {
                charset = UTF8;
            }
            osw = new OutputStreamWriter(new FileOutputStream(fileName), charset);
            osw.write(txt);
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void write(String fileName, String txt) {
        write(fileName, txt, UTF8);
    }

    public static List<String> readFileLines(String filePath) throws FileNotFoundException {
        BufferedReader br = null;
        BufferedReader br2 = null;
        List<String> contents = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(filePath));
            System.out.println("Reading the file using readLine() method: ");
            String contentLine;
            while ((contentLine = br.readLine()) != null) {
                //将每一行追加到arr1
                if (null != contentLine) {
                    contentLine = contentLine.trim();
                    if (contentLine.length() > 0) {
                        contents.add(contentLine);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (br2 != null) {
                    br2.close();
                }
            } catch (IOException e) {
                System.out.println("Error in closing the BufferedReader");
            }
        }
        return contents;
    }
}
