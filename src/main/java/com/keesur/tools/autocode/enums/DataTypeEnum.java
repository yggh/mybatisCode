package com.keesur.tools.autocode.enums;

public enum DataTypeEnum {

    STRING_V("VARCHAR", "java.lang.String"),
    STRING("CHAR", "java.lang.String"),
    LONG("BIGINT", "java.lang.Long"),
    INTEGER("INTEGER", "java.lang.Integer"),
    TIMESTAMP("TIMESTAMP", "java.util.Date"),
    DATE("DATE", "java.util.Date"),
    DATETIME("DATETIME", "java.util.Date"),
    DECIMAL("DECIMAL", "java.math.BigDecimal"),
    SHORT("TINYINT", "java.lang.Short"),
    ;
    private String jdbcType;
    private String javaType;

    private DataTypeEnum(String jdbcType, String javaType) {
        this.jdbcType = jdbcType;
        this.javaType = javaType;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public String getJavaType() {
        return javaType;
    }
}
