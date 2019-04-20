package com.java110.code;

import java.util.Map;

public class GeneratorServiceDaoImplMapperListener extends BaseGenerator {


    /**
     *
     * @param data
     * @param fileContext
     * @return
     */
    //insert into business_store(store_id,b_id,user_id,name,address,tel,store_type_cd,nearby_landmarks,map_x,map_y,month,operate)
    //        values(#{storeId},#{bId},#{userId},#{name},#{address},#{tel},#{storeTypeCd},#{nearbyLandmarks},#{mapX},#{mapY},#{month},#{operate})

    private String dealSaveBusinessInfo(Data data,String fileContext){
        String sql = "insert info "+ data.getBusinessTableName() + "(\n";
        String sqlValue = "\n) values (\n";

        Map<String,String> params = data.getParams();

        for(String key:params.keySet()){
            if("statusCd".equals(key)){
                continue;
            }
            sql += params.get(key)+",";
            sqlValue += "#{"+key+"},";
        }

        sql = sql.endsWith(",")?sql.substring(0,sql.length()-1):sql;
        sqlValue = sqlValue.endsWith(",")?sqlValue.substring(0,sqlValue.length()-1):sqlValue;

        sql += (sqlValue +"\n)");

        fileContext = fileContext.replace("$saveBusinessInfo$",sql);

        return fileContext;

    }

    /**
     * select s.store_id,s.b_id,s.user_id,s.name,s.address,s.tel,s.store_type_cd,s.nearby_landmarks,s.map_x,s.map_y,s.operate
     *         from business_store s where 1 = 1
     *         <if test="operate != null and operate != ''">
     *             and s.operate = #{operate}
     *         </if>
     *         <if test="bId != null and bId !=''">
     *             and s.b_id = #{bId}
     *         </if>
     *         <if test="storeId != null and storeId != ''">
     *             and s.store_id = #{storeId}
     *         </if>
     * @param data
     * @param fileContext
     * @return
     */
    private String dealGetBusinessInfo(Data data,String fileContext){
        String sql = "select  ";
        String sqlValue = " \nfrom "+data.getBusinessTableName()+" t \nwhere 1 =1 \n";

        Map<String,String> params = data.getParams();

        for(String key:params.keySet()){
            if("statusCd".equals(key)){
                continue;
            }
            sql += ("t."+params.get(key)+",");
            sqlValue += "<if test=\""+key+" !=null and "+key+" != ''\">\n";
            sqlValue += "   and t."+params.get(key)+"= #{"+key +"}\n";
            sqlValue += "</if> \n";

        }

        sql = sql.endsWith(",")?sql.substring(0,sql.length()-1):sql;

        sql += sqlValue;

        fileContext = fileContext.replace("$getBusinessInfo$",sql);

        return fileContext;

    }



    /**
     * 生成代码
     * @param data
     */
    public void generator(Data data){
        StringBuffer sb = readFile(this.getClass().getResource("/template/ServiceDaoImplMapper.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store",toLowerCaseFirstOne(data.getName()))
                .replace("Store",toUpperCaseFirstOne(data.getName()))
                .replace("商户",data.getDesc());
        fileContext = dealSaveBusinessInfo(data,fileContext);
        fileContext = dealGetBusinessInfo(data,fileContext);

        System.out.println(this.getClass().getResource("/listener").getPath());
        String writePath = this.getClass().getResource("/listener").getPath()+"/"+toUpperCaseFirstOne(data.getName())+"ServiceDaoImplMapper.xml";
        writeFile(writePath,
                fileContext);
    }
}
