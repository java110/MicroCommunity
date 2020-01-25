package com.java110.code;

import java.util.Map;

public class GeneratorServiceDaoImplMapperListener extends BaseGenerator {


    /**
     * @param data
     * @param fileContext
     * @return
     */
    //insert into business_store(store_id,b_id,user_id,name,address,tel,store_type_cd,nearby_landmarks,map_x,map_y,month,operate)
    //        values(#{storeId},#{bId},#{userId},#{name},#{address},#{tel},#{storeTypeCd},#{nearbyLandmarks},#{mapX},#{mapY},#{month},#{operate})
    private String dealSaveBusinessInfo(Data data, String fileContext) {
        String sql = "insert into " + data.getBusinessTableName() + "(\n";
        String sqlValue = "\n) values (\n";

        Map<String, String> params = data.getParams();

        for (String key : params.keySet()) {
            if ("statusCd".equals(key)) {
                continue;
            }
            sql += params.get(key) + ",";
            sqlValue += "#{" + key + "},";
        }

        sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;
        sqlValue = sqlValue.endsWith(",") ? sqlValue.substring(0, sqlValue.length() - 1) : sqlValue;

        sql += (sqlValue + "\n)");

        fileContext = fileContext.replace("$saveBusinessInfo$", sql);

        return fileContext;

    }

    /**
     * select s.store_id,s.b_id,s.user_id,s.name,s.address,s.tel,s.store_type_cd,s.nearby_landmarks,s.map_x,s.map_y,s.operate
     * from business_store s where 1 = 1
     * <if test="operate != null and operate != ''">
     * and s.operate = #{operate}
     * </if>
     * <if test="bId != null and bId !=''">
     * and s.b_id = #{bId}
     * </if>
     * <if test="storeId != null and storeId != ''">
     * and s.store_id = #{storeId}
     * </if>
     *
     * @param data
     * @param fileContext
     * @return
     */
    private String dealGetBusinessInfo(Data data, String fileContext) {
        String sql = "select  ";
        String sqlValue = " \nfrom " + data.getBusinessTableName() + " t \nwhere 1 =1 \n";

        Map<String, String> params = data.getParams();

        for (String key : params.keySet()) {
            if ("statusCd".equals(key)) {
                continue;
            }
            sql += ("t." + params.get(key) + ",");
            if (!key.equals(params.get(key))) {
                sql += ("t." + params.get(key) + " " + key + ",");
            }
            sqlValue += "<if test=\"" + key + " !=null and " + key + " != ''\">\n";
            sqlValue += "   and t." + params.get(key) + "= #{" + key + "}\n";
            sqlValue += "</if> \n";

        }

        sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;

        sql += sqlValue;

        fileContext = fileContext.replace("$getBusinessInfo$", sql);

        return fileContext;

    }

    /**
     * insert into s_store(store_id,b_id,user_id,name,address,tel,store_type_cd,nearby_landmarks,map_x,map_y,status_cd)
     * select s.store_id,s.b_id,s.user_id,s.name,s.address,s.tel,s.store_type_cd,s.nearby_landmarks,s.map_x,s.map_y,'0'
     * from business_store s where
     * s.operate = 'ADD' and s.b_id=#{bId}
     */
    private String dealSaveInfoInstance(Data data, String fileContext) {
        String sql = "insert into " + data.getTableName() + "(\n";
        String sqlValue = "select ";
        String sqlWhere = " from " + data.getBusinessTableName() + " t where 1=1\n";

        Map<String, String> params = data.getParams();

        for (String key : params.keySet()) {
            if ("operate".equals(key)) {
                continue;
            }
            sql += params.get(key) + ",";

            if ("statusCd".equals(key)) {
                sqlValue += "'0',";
                continue;
            }
            sqlValue += "t." + params.get(key) + ",";
        }

        for (String key : params.keySet()) {
            if ("statusCd".equals(key)) {
                continue;
            }

            if ("operate".equals(key)) {
                sqlWhere += "   and t." + params.get(key) + "= 'ADD'\n";
            } else {
                sqlWhere += "<if test=\"" + key + " !=null and " + key + " != ''\">\n";
                sqlWhere += "   and t." + params.get(key) + "= #{" + key + "}\n";
                sqlWhere += "</if> \n";

            }

        }

        sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;
        sqlValue = sqlValue.endsWith(",") ? sqlValue.substring(0, sqlValue.length() - 1) : sqlValue;

        sql += ("\n) " + sqlValue + sqlWhere);

        fileContext = fileContext.replace("$saveInfoInstance$", sql);

        return fileContext;

    }

    /**
     * select s.store_id,s.b_id,s.user_id,s.name,s.address,s.tel,s.store_type_cd,s.nearby_landmarks,s.map_x,s.map_y,s.status_cd
     * from s_store s
     * where 1=1
     * <if test="statusCd != null and statusCd != ''">
     * and s.status_cd = #{statusCd}
     * </if>
     *
     * <if test="bId != null and bId !=''">
     * and s.b_id = #{bId}
     * </if>
     * <if test="storeId != null and storeId !=''">
     * and s.store_id = #{storeId}
     * </if>
     *
     * @param data
     * @param fileContext
     * @return
     */
    private String dealGetInfo(Data data, String fileContext) {
        String sql = "select  ";
        String sqlValue = " \nfrom " + data.getTableName() + " t \nwhere 1 =1 \n";

        Map<String, String> params = data.getParams();

        for (String key : params.keySet()) {
            if ("operate".equals(key)) {
                continue;
            }
            sql += ("t." + params.get(key) + ",");
            if (!key.equals(params.get(key))) {
                sql += ("t." + params.get(key) + " " + key + ",");
            }
            sqlValue += "<if test=\"" + key + " !=null and " + key + " != ''\">\n";
            sqlValue += "   and t." + params.get(key) + "= #{" + key + "}\n";
            sqlValue += "</if> \n";


        }

        //加入分页功能<if test="page != -1">
        //            limit page,row
        //        </if>
        sqlValue += "order by t.create_time desc\n";
        sqlValue += "<if test=\"page != -1 and page != null \">\n";
        sqlValue += "   limit #{page}, #{row}\n";
        sqlValue += "</if> \n";

        sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;

        sql += sqlValue;

        fileContext = fileContext.replace("$getInfo$", sql);

        return fileContext;
    }

    /**
     * update s_store s set s.status_cd = #{statusCd}
     * <if test="newBId != null and newBId != ''">
     * ,s.b_id = #{newBId}
     * </if>
     * <if test="userId != null and userId != ''">
     * ,s.user_id = #{userId}
     * </if>
     * <if test="name != null and name != ''">
     * ,s.name = #{name}
     * </if>
     * <if test="address != null and address != ''">
     * ,s.address = #{address}
     * </if>
     * <if test="tel != null and tel != ''">
     * ,s.tel = #{tel}
     * </if>
     * <if test="storeTypeCd != null and storeTypeCd != ''">
     * ,s.store_type_cd = #{storeTypeCd}
     * </if>
     * <if test="nearbyLandmarks != null and nearbyLandmarks != ''">
     * ,s.nearby_landmarks = #{nearbyLandmarks}
     * </if>
     * <if test="mapX != null and mapX != ''">
     * ,s.map_x = #{mapX}
     * </if>
     * <if test="mapY != null and mapY != ''">
     * ,s.map_y = #{mapY}
     * </if>
     * where 1=1
     * <if test="bId != null and bId !=''">
     * and s.b_id = #{bId}
     * </if>
     * <if test="storeId != null and storeId !=''">
     * and s.store_id = #{storeId}
     * </if>
     */
    private String dealUpdateInfoInstance(Data data, String fileContext) {
        String sql = "update  " + data.getTableName() + " t set t.status_cd = #{statusCd}\n";
        String sqlWhere = " where 1=1 ";

        Map<String, String> params = data.getParams();

        sql += "<if test=\"newBId != null and newBId != ''\">\n";
        sql += ",t.b_id = #{newBId}\n";
        sql += "</if> \n";

        for (String key : params.keySet()) {
            if ("operate".equals(key)) {
                continue;
            }

            if ("statusCd".equals(key)) {
                continue;
            }
            if (!"bId".equals(key) && !data.getId().equals(key)) {
                sql += "<if test=\"" + key + " !=null and " + key + " != ''\">\n";
                sql += ", t." + params.get(key) + "= #{" + key + "}\n";
                sql += "</if> \n";
            }

            if ("bId".equals(key) || data.getId().equals(key)) {
                sqlWhere += "<if test=\"" + key + " !=null and " + key + " != ''\">\n";
                sqlWhere += "and t." + params.get(key) + "= #{" + key + "}\n";
                sqlWhere += "</if> \n";
            }
        }

        sql += sqlWhere;

        fileContext = fileContext.replace("$updateInfoInstance$", sql);

        return fileContext;


    }

    /**
     * 拼装 查询数量
     * @param data 数据
     * @param fileContext 文件内容
     * @return
     */
    private String dealGetCount(Data data, String fileContext) {
        String sql = "select  count(1) count";
        String sqlValue = " \nfrom " + data.getTableName() + " t \nwhere 1 =1 \n";

        Map<String, String> params = data.getParams();

        for (String key : params.keySet()) {
            if ("operate".equals(key)) {
                continue;
            }
            sqlValue += "<if test=\"" + key + " !=null and " + key + " != ''\">\n";
            sqlValue += "   and t." + params.get(key) + "= #{" + key + "}\n";
            sqlValue += "</if> \n";


        }

        sql = sql.endsWith(",") ? sql.substring(0, sql.length() - 1) : sql;

        sql += sqlValue;

        fileContext = fileContext.replace("$queryCount$", sql);

        return fileContext;
    }


    /**
     * 生成代码
     *
     * @param data
     */
    public void generator(Data data) {
        StringBuffer sb = readFile(this.getClass().getResource("/template/ServiceDaoImplMapper.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("商户", data.getDesc());
        fileContext = dealSaveBusinessInfo(data, fileContext);
        fileContext = dealGetBusinessInfo(data, fileContext);
        fileContext = dealSaveInfoInstance(data, fileContext);
        fileContext = dealGetInfo(data, fileContext);
        fileContext = dealUpdateInfoInstance(data, fileContext);
        fileContext = dealGetCount(data, fileContext);

        System.out.println(this.getClass().getResource("/listener").getPath());
        String writePath = this.getClass().getResource("/listener").getPath() + "/" + toUpperCaseFirstOne(data.getName()) + "ServiceDaoImplMapper.xml";
        writeFile(writePath,
                fileContext);
    }
}
