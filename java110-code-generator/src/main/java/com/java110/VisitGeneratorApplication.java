package com.java110;


import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class VisitGeneratorApplication {

    protected VisitGeneratorApplication() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    /**
     * 代码生成器 入口方法
     *  此处生成的mapper文件包含过程表和实例表的sql,所以要求两张表的特殊字段也要写上
     *   BusinessTypeCd 在java110\common\constant\BusinessTypeConstant.java
     * @param args 参数
     */
    public static void main(String[] args) {
        Data data = new Data();
        data.setId("vId");
        data.setName("visit");
        data.setDesc("访客信息");
        data.setShareParam("vId");
        data.setShareColumn("v_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_VISIT");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_VISIT");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_VISIT");
        data.setNewBusinessTypeCdValue("110100030001");
        data.setUpdateBusinessTypeCdValue("110100040001");
        data.setDeleteBusinessTypeCdValue("110100050001");
        data.setBusinessTableName("business_visit_info");
        data.setTableName("visit_info");
        Map<String, String> param = new HashMap<String, String>();
        param.put("vId", "v_id");       //map的key为你自定义的字段名就是驼峰命名法的那个，value为数据库表的字段名
        param.put("name", "name");
        param.put("visitGender", "visit_gender");
        param.put("phoneNumber", "phoneNumber");
        param.put("userId", "userId");
        param.put("ownerId", "owner_id");
        param.put("visitCase", "visit_case");
        param.put("visitTime", "visit_time");
        param.put("departureTime", "departure_time");
        param.put("statusCd", "status_cd");
        param.put("operate", "operate");
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
