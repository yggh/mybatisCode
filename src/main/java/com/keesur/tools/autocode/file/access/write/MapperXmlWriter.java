package com.keesur.tools.autocode.file.access.write;

import com.keesur.tools.autocode.file.access.dto.ColumnDto;
import com.keesur.tools.autocode.file.access.dto.TableDto;
import com.keesur.tools.autocode.util.FieldUtil;
import com.keesur.tools.autocode.util.XmlFormat;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class MapperXmlWriter implements Serializable {

    private TableDto tableDto;
    private String fileName;

    public MapperXmlWriter(TableDto tableDto) {
        this.tableDto = tableDto;
        String entityJavaName = FieldUtil.upperFirstLetter(FieldUtil.underlineFilter(tableDto.getTableName()));
        if(tableDto.getTableName().indexOf("T_") >= 0){
            entityJavaName = entityJavaName.substring(1,entityJavaName.length());
        }
        this.fileName =  entityJavaName+"Mapper.xml";
    }
    public String getFileName() {
        return fileName;
    }

    public String getContent(String basePackage) {
        String entityJavaName = FieldUtil.upperFirstLetter(FieldUtil.underlineFilter(tableDto.getTableName()));
        if(tableDto.getTableName().indexOf("T_") >= 0){
            entityJavaName = entityJavaName.substring(1,entityJavaName.length());
        }
        String mapperName = basePackage + ".dao."+ entityJavaName+"Mapper";
        String entityName = basePackage + ".entity."+ entityJavaName;
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n <!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\"> \n ";
        StringBuilder sb = new StringBuilder();
        sb.append("<mapper namespace=\"").append(mapperName).append("\">");//Dao.java

        ///////////////----------resultMap---------start
        sb.append("<resultMap id=\"BaseResultMap\" type=\"").append(entityName).append("\">");//entity.java
        ColumnDto primaryColumn = tableDto.getPrimaryColumn();
        List<ColumnDto> columnDtoList = tableDto.getColumns();
        boolean haveColumn = false;
        if (null != columnDtoList && columnDtoList.size() > 0) {
            haveColumn = true;
        }
        if (haveColumn) {
            for (ColumnDto columnDto : columnDtoList) {
                //<result column="phone" jdbcType="VARCHAR" property="phone" />
                if (null != primaryColumn && primaryColumn.getColumnName().equals(columnDto.getColumnName())) {
                    sb.append("<id column=\"");
                } else {
                    sb.append("<result column=\"");
                }
                sb.append(columnDto.getColumnName()).append("\" jdbcType=\"").append(columnDto.getColumnType().getJdbcType()).append("\" property=\"").append(columnDto.getJavaName()).append("\" />");
            }
        }
        sb.append("</resultMap>");
        ///////////////----------resultMap---------end

        ///////////////----------sql---------start

        //  <sql id="Base_Column_List">
        //    uid, phone, user_name, password
        //  </sql>
        sb.append("<sql id=\"Base_Column_List\">");
        if (haveColumn) {
            int i = 0;
            for (ColumnDto columnDto : columnDtoList) {
                if (i++ > 0) {
                    sb.append(",");
                }
                sb.append(columnDto.getColumnName());
            }
        }
        sb.append("</sql>");

        //  <sql id="query_condition">
        //    <if test="uid !=null">
        //      and uid=#{uid,jdbcType=BIGINT}
        //    </if>
        //  </sql>
        sb.append("<sql id=\"query_condition\">");
        if (haveColumn) {
            for (ColumnDto columnDto : columnDtoList) {
                sb.append("<if test=\"").append(columnDto.getJavaName()).append(" != null\">");
                sb.append("and ").append(columnDto.getColumnName()).append("=#{").append(columnDto.getJavaName()).append(",jdbcType=").append(columnDto.getColumnType().getJdbcType()).append("}");
                sb.append("</if>");
            }
        }
        sb.append("</sql>");
        ///////////////----------sql---------end

        ///////////////----------selectByPrimaryKey---------start
        //<select id="selectByPrimaryKey" parameterType="java.lang.Long" resultMap="BaseResultMap">
        //    select
        //    <include refid="Base_Column_List" />
        //    from t_user
        //    where uid = #{uid,jdbcType=BIGINT}
        //  </select>
        if (null != primaryColumn) {
            sb.append("<select id=\"selectByPrimaryKey\" parameterType=\"").append(primaryColumn.getColumnType().getJavaType()).append("\" resultMap=\"BaseResultMap\">");
            sb.append("select  <include refid=\"Base_Column_List\" />  from ").append(tableDto.getTableName()).append(" where ").append(primaryColumn.getColumnName()).append(" = #{").append(primaryColumn.getJavaName()).append(",jdbcType=").append(primaryColumn.getColumnType().getJdbcType()).append("}</select>");
        }
        ///////////////----------selectByPrimaryKey---------end

        ///////////////----------findByCondition---------start

//        <select id="findByCondition"
//          resultMap="BaseResultMap">
//    SELECT
//    <include refid="Base_Column_List"/>
//    FROM t_user t
//    <where>
//      <include refid="query_condition"/>
//    </where>
//  </select>
        sb.append("<select id=\"findByCondition\"  resultMap=\"BaseResultMap\">   SELECT  <include refid=\"Base_Column_List\"/>  FROM ").append(tableDto.getTableName()).append(" <where>  <include refid=\"query_condition\"/>  </where>  </select>");
        ///////////////----------findByCondition---------end

        ///////////////----------deleteByPrimaryKey---------start
        //<delete id="deleteByPrimaryKey" parameterType="java.lang.Long">
        //    <!--
        //      WARNING - @mbg.generated
        //      This element is automatically generated by MyBatis Generator, do not modify.
        //    -->
        //    delete from t_user
        //    where uid = #{uid,jdbcType=BIGINT}
        //  </delete>
        if(null != primaryColumn){
            sb.append("<delete id=\"deleteByPrimaryKey\" parameterType=\"").append(primaryColumn.getColumnType().getJavaType()).append("\"> ");
            sb.append(" delete from ").append(tableDto.getTableName()).append("   where ").append(primaryColumn.getColumnName()).append(" = #{").append(primaryColumn.getJavaName()).append(",jdbcType=").append(primaryColumn.getColumnType().getJdbcType()).append("} </delete>");
        }
        ///////////////----------deleteByPrimaryKey---------end

        ///////////////----------insert---------start
        //<insert id="insert" parameterType="com.cursiu.vmall.entity.User" useGeneratedKeys="true" keyColumn="uid" keyProperty="uid">
        //    <!--
        //      WARNING - @mbg.generated
        //      This element is automatically generated by MyBatis Generator, do not modify.
        //    -->
        //    insert into t_user (phone, user_name,
        //      password)
        //    values ( #{phone,jdbcType=VARCHAR}, #{userName,jdbcType=VARCHAR},
        //      #{password,jdbcType=VARCHAR})
        //  </insert>

        sb.append("<insert id=\"insert\" parameterType=\"").append(entityName).append("\" ");
        if(null != primaryColumn){
            sb.append("useGeneratedKeys=\"true\" keyColumn=\"").append(primaryColumn.getColumnName()).append("\" keyProperty=\"").append(primaryColumn.getJavaName()).append("\">");
        }else{
            sb.append(">");
        }
        sb.append("insert into ").append(tableDto.getTableName()).append(" (");
        int c = 0;
        for(ColumnDto columnDto : columnDtoList){
            if(c++ > 0){
                sb.append(",");
            }
            sb.append(columnDto.getColumnName());
        }
        sb.append(") values ( ");
        int d = 0;
        for(ColumnDto columnDto : columnDtoList){
            if(d++ > 0){
                sb.append(",");
            }
            sb.append(" #{").append(columnDto.getJavaName()).append(",jdbcType=").append(columnDto.getColumnType().getJdbcType()).append("}");
        }
        sb.append(")</insert>");
        ///////////////----------insert---------end

        ///////////////----------insertSelective---------start
        // <insert id="insertSelective" parameterType="com.cursiu.vmall.entity.User"  useGeneratedKeys="true" keyColumn="uid" keyProperty="uid">
        //    <!--
        //      WARNING - @mbg.generated
        //      This element is automatically generated by MyBatis Generator, do not modify.
        //    -->
        //    insert into t_user
        //    <trim prefix="(" suffix=")" suffixOverrides=",">
        //      <if test="phone != null">
        //        phone,
        //      </if>
        //      <if test="userName != null">
        //        user_name,
        //      </if>
        //      <if test="password != null">
        //        password,
        //      </if>
        //    </trim>
        //    <trim prefix="values (" suffix=")" suffixOverrides=",">
        //      <if test="phone != null">
        //        #{phone,jdbcType=VARCHAR},
        //      </if>
        //      <if test="userName != null">
        //        #{userName,jdbcType=VARCHAR},
        //      </if>
        //      <if test="password != null">
        //        #{password,jdbcType=VARCHAR},
        //      </if>
        //    </trim>
        //  </insert>
        sb.append("<insert id=\"insertSelective\" parameterType=\"").append(entityName).append("\"  ");
        if(null != primaryColumn){
            sb.append("useGeneratedKeys=\"true\" keyColumn=\"").append(primaryColumn.getColumnName()).append("\" keyProperty=\"").append(primaryColumn.getJavaName()).append("\">");
        }else{
            sb.append(">");
        }
        sb.append("insert into ").append(tableDto.getTableName()).append(" <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        if (haveColumn) {
            for (ColumnDto columnDto : columnDtoList) {
                sb.append("<if test=\"").append(columnDto.getJavaName()).append(" != null\">");
                sb.append(columnDto.getColumnName()).append(",");
                sb.append("</if>");
            }
        }
        sb.append("</trim> <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
        if (haveColumn) {
            for (ColumnDto columnDto : columnDtoList) {
                sb.append("<if test=\"").append(columnDto.getJavaName()).append(" != null\">");
                sb.append(" #{").append(columnDto.getJavaName()).append(",jdbcType=").append(columnDto.getColumnType().getJdbcType()).append("},");
                sb.append("</if>");
            }
        }
        sb.append("</trim></insert>");
        ///////////////----------insertSelective---------end

        ///////////////----------updateByPrimaryKeySelective---------start
        //<update id="updateByPrimaryKeySelective" parameterType="com.cursiu.vmall.entity.User">
        //    <!--
        //      WARNING - @mbg.generated
        //      This element is automatically generated by MyBatis Generator, do not modify.
        //    -->
        //    update t_user
        //    <set>
        //      <if test="phone != null">
        //        phone = #{phone,jdbcType=VARCHAR},
        //      </if>
        //      <if test="userName != null">
        //        user_name = #{userName,jdbcType=VARCHAR},
        //      </if>
        //      <if test="password != null">
        //        password = #{password,jdbcType=VARCHAR},
        //      </if>
        //    </set>
        //    where uid = #{uid,jdbcType=BIGINT}
        //  </update>
        if(null != primaryColumn){
            sb.append("<update id=\"updateByPrimaryKeySelective\" parameterType=\"").append(entityName).append("\"> update ").append(tableDto.getTableName()).append(" <set> ");
            if (haveColumn) {
                for (ColumnDto columnDto : columnDtoList) {
                    sb.append("<if test=\"").append(columnDto.getJavaName()).append(" != null\">");
                    sb.append(columnDto.getColumnName()).append(" = #{").append(columnDto.getJavaName()).append(",jdbcType=").append(columnDto.getColumnType().getJdbcType()).append("},");
                    sb.append("</if>");
                }
            }
            sb.append(" </set>  where ").append(primaryColumn.getColumnName()).append(" = #{").append(primaryColumn.getJavaName()).append(",jdbcType=").append(primaryColumn.getColumnType().getJdbcType()).append("} </update>");
        }
        ///////////////----------updateByPrimaryKeySelective---------end

        ///////////////----------updateByPrimaryKey---------start
        //<update id="updateByPrimaryKey" parameterType="com.cursiu.vmall.entity.User">
        //    <!--
        //      WARNING - @mbg.generated
        //      This element is automatically generated by MyBatis Generator, do not modify.
        //    -->
        //    update t_user
        //    set phone = #{phone,jdbcType=VARCHAR},
        //      user_name = #{userName,jdbcType=VARCHAR},
        //      password = #{password,jdbcType=VARCHAR}
        //    where uid = #{uid,jdbcType=BIGINT}
        //  </update>
        if(null != primaryColumn){
            sb.append("<update id=\"updateByPrimaryKey\" parameterType=\"").append(entityName).append("\"> update ").append(tableDto.getTableName()).append(" set ");
            if (haveColumn) {
                int e = 0;
                for (ColumnDto columnDto : columnDtoList) {
                    if(columnDto.getColumnName().equals(primaryColumn.getColumnName())){
                        continue;
                    }
                    if(e++ > 0){
                        sb.append(",");
                    }
                    sb.append(columnDto.getColumnName()).append(" = #{").append(columnDto.getJavaName()).append(",jdbcType=").append(columnDto.getColumnType().getJdbcType()).append("}");
                }
            }
            sb.append(" where ").append(primaryColumn.getColumnName()).append(" = #{").append(primaryColumn.getJavaName()).append(",jdbcType=").append(primaryColumn.getColumnType().getJdbcType()).append("} </update>");
        }
        ///////////////----------updateByPrimaryKey---------end
        sb.append("</mapper>");
        String content = XmlFormat.format(sb.toString());
        int xmlindex = content.indexOf("<?xml");
        if(xmlindex >= 0){
            content = content.substring(content.indexOf(">",xmlindex)+1,content.length());
        }

        return header + content;
    }

}


