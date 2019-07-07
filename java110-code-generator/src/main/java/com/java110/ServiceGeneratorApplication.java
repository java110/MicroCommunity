package com.java110;


import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class ServiceGeneratorApplication {

    protected ServiceGeneratorApplication() {
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
        data.setId("serviceId");
        data.setName("service");
        data.setDesc("服务");
        data.setShareParam("serviceId");
        data.setShareColumn("service_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_NOTICE");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_NOTICE");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_NOTICE");
        data.setNewBusinessTypeCdValue("550100030001");
        data.setUpdateBusinessTypeCdValue("550100040001");
        data.setDeleteBusinessTypeCdValue("550100050001");
        data.setBusinessTableName("business_service");
        data.setTableName("c_service");
        Map<String, String> param = new HashMap<String, String>();
        param.put("serviceId", "service_id");
        param.put("serviceCode", "service_code");
        param.put("businessTypeCd", "business_type_cd");
        param.put("name", "name");
        param.put("seq", "seq");
        param.put("messageQueueName", "messageQueueName");
        param.put("isInstance", "is_instance");
        param.put("url", "url");
        param.put("method", "method");
        param.put("timeout", "timeout");
        param.put("retryCount", "retry_count");
        param.put("provideAppId", "provide_app_id");
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
