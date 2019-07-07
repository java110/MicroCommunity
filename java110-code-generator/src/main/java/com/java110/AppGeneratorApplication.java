package com.java110;


import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class AppGeneratorApplication {

    protected AppGeneratorApplication() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    /**
     * 代码生成器 入口方法
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        Data data = new Data();
        data.setId("appId");
        data.setName("app");
        data.setDesc("应用");
        data.setShareParam("appId");
        data.setShareColumn("app_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_NOTICE");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_NOTICE");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_NOTICE");
        data.setNewBusinessTypeCdValue("550100030001");
        data.setUpdateBusinessTypeCdValue("550100040001");
        data.setDeleteBusinessTypeCdValue("550100050001");
        data.setBusinessTableName("business_app");
        data.setTableName("c_app");
        Map<String, String> param = new HashMap<String, String>();
        param.put("appId", "app_id");
        param.put("name", "name");
        param.put("securityCode", "security_code");
        param.put("whileListIp", "while_list_ip");
        param.put("blackListIp", "black_list_ip");
        param.put("remark", "remark");
        param.put("userId", "user_id");
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
