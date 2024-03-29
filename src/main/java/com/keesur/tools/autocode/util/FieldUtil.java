package com.keesur.tools.autocode.util;


public class FieldUtil {

    /**
     * remove the underline. e.x. table_name change to: tableName
     *
     * @param field
     * @return
     */
    public static String underlineFilter(String field) {
        if (null != field && field.length() > 0) {
            field = field.toLowerCase();
            StringBuilder sb = new StringBuilder(field);
            while (sb.indexOf("_") > -1) {
                int index = sb.indexOf("_");
                String upperLetter = sb.substring(index + 1, index + 2)
                        .toUpperCase();
                sb.replace(index + 1, index + 2, upperLetter);
                sb.deleteCharAt(index);
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 过滤"."
     *
     * @param field
     * @return
     */
    public static String dotFilter(String field) {
        if (null != field && field.length() > 0) {
            if (field.indexOf(".") > -1) {
                String[] words = field.split("\\.");
                String ret = "";
                for (String str : words) {
                    ret += upperFirstLetter(str);
                }
                return ret;
            }
        }
        return field;
    }

    /**
     * 将第一个字母转换成大写
     *
     * @param str
     * @return
     */
    public static String upperFirstLetter(String str) {
        if (null != str && str.length() > 0) {
            String firstUpper = str.substring(0, 1).toUpperCase();
            str = firstUpper + str.substring(1, str.length());
        }
        return str;
    }

    /**
     * 将第一个字母转换成小写
     *
     * @param str
     * @return
     */
    public static String lowerFirstLetter(String str) {
        if (null != str && str.length() > 0) {
            String firstLower = str.substring(0, 1).toLowerCase();
            str = firstLower + str.substring(1, str.length());
        }
        return str;
    }


    public static void main(String[] args) {
        System.out.println(underlineFilter("table_name"));
        System.out.println(underlineFilter("tableName"));
        System.out.println(underlineFilter("username"));
    }

}
