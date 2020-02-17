package com.java110;


import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class InspectionPlanGeneratorApplication {

    protected InspectionPlanGeneratorApplication() {
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
        data.setId("inspectionPlanId");
        data.setName("inspectionPlan");
        data.setDesc("巡检计划");
        data.setShareParam("communityId");
        data.setShareColumn("community_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_INSPECTION_PLAN");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_INSPECTION_PLAN");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_INSPECTION_PLAN");
        data.setNewBusinessTypeCdValue("520200030001");
        data.setUpdateBusinessTypeCdValue("520200040001");
        data.setDeleteBusinessTypeCdValue("520200050001");
        data.setBusinessTableName("business_inspection_plan");
        data.setTableName("businessInspectionPlan");
        Map<String, String> param = new HashMap<String, String>();
        param.put("inspectionPlanId", "inspection_plan_id");       //map的key为你自定义的字段名就是驼峰命名法的那个，value为数据库表的字段名
        param.put("inspectionPlanName", "inspection_plan_name");
        param.put("inspectionRouteId", "inspection_route_id");
        param.put("communityId", "community_id");
        param.put("startTime", "start_time");
        param.put("entTime", "ent_time");
        param.put("inspectionPlanPeriod", "inspection_plan_period");
        param.put("staffId", "staff_id");
        param.put("staffName", "staff_name");
        param.put("signType", "sign_type");
        param.put("createUser", "create_user");
        param.put("remark", "remark");
        param.put("state", "state");
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
