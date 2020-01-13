package com.java110;


import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class ActivitiesGeneratorApplication {

    protected ActivitiesGeneratorApplication() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    /**
     * 代码生成器 入口方法
     *  此处生成的mapper文件包含过程表和实例表的sql,所以要求两张表的特殊字段也要写上
     *   BusinessTypeCd
     * @param args 参数
     */
    public static void main(String[] args) {
        Data data = new Data();
        data.setId("activitiesId");
        data.setName("activities");
        data.setDesc("活动");
        data.setShareParam("communityId");
        data.setShareColumn("community_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_ACTIVITIES");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_ACTIVITIES");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_ACTIVITIES");
        data.setNewBusinessTypeCdValue("290200030001");
        data.setUpdateBusinessTypeCdValue("290200040001");
        data.setDeleteBusinessTypeCdValue("290200050001");
        data.setBusinessTableName("business_activities");
        data.setTableName("activities");
        Map<String, String> param = new HashMap<String, String>();
        param.put("activitiesId", "activities_id");       //map的key为你自定义的字段名就是驼峰命名法的那个，value为数据库表的字段名
        param.put("title", "title");
        param.put("typeCd", "type_cd");
        param.put("headerImg", "header_img");
        param.put("context", "context");
        param.put("communityId", "community_id");
        param.put("readCount", "read_count");
        param.put("likeCount", "like_count");
        param.put("collectCount", "collect_count");
        param.put("userId", "user_id");
        param.put("userName", "user_name");
        param.put("startTime", "start_time");
        param.put("endTime", "end_time");
        param.put("statusCd", "status_cd");
        param.put("operate", "operate");
        param.put("bId", "b_id");
        data.setParams(param);
        GeneratorSaveInfoListener generatorSaveInfoListener = new GeneratorSaveInfoListener();
        generatorSaveInfoListener.generator(data);

        GeneratorAbstractBussiness generatorAbstractBussiness = new GeneratorAbstractBussiness();
        generatorAbstractBussiness.generator(data);

        GeneratorIServiceDaoListener generatorIServiceDaoListener = new GeneratorIServiceDaoListener();
        generatorIServiceDaoListener.generator(data);

        GeneratorServiceDaoImplListener generatorServiceDaoImplListener = new GeneratorServiceDaoImplListener();
        generatorServiceDaoImplListener.generator(data);

        GeneratorServiceDaoImplMapperListener generatorServiceDaoImplMapperListener = null;
        generatorServiceDaoImplMapperListener = new GeneratorServiceDaoImplMapperListener();
        generatorServiceDaoImplMapperListener.generator(data);

        GeneratorUpdateInfoListener generatorUpdateInfoListener = new GeneratorUpdateInfoListener();
        generatorUpdateInfoListener.generator(data);

        GeneratorDeleteInfoListener generatorDeleteInfoListener = new GeneratorDeleteInfoListener();
        generatorDeleteInfoListener.generator(data);

        GeneratorInnerServiceSMOImpl generatorInnerServiceSMOImpl = new GeneratorInnerServiceSMOImpl();
        generatorInnerServiceSMOImpl.generator(data);

        GeneratorDtoBean generatorDtoBean = new GeneratorDtoBean();
        generatorDtoBean.generator(data);

        GeneratorIInnerServiceSMO generatorIInnerServiceSMO = new GeneratorIInnerServiceSMO();
        generatorIInnerServiceSMO.generator(data);
    }
}
