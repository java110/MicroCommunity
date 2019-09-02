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
        data.setId("serviceBusinessId");
        data.setName("serviceBusiness");
        data.setDesc("服务实现");
        data.setShareParam("serviceBusinessId");
        data.setShareColumn("service_business_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_NOTICE");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_NOTICE");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_NOTICE");
        data.setNewBusinessTypeCdValue("550100030001");
        data.setUpdateBusinessTypeCdValue("550100040001");
        data.setDeleteBusinessTypeCdValue("550100050001");
        data.setBusinessTableName("c_service_business");
        data.setTableName("c_service_business");
        Map<String, String> param = new HashMap<String, String>();
        param.put("serviceBusinessId", "service_business_id");
        param.put("businessTypeCd", "business_type_cd");
        param.put("invokeType", "invoke_type");
        param.put("url", "url");
        param.put("messageTopic", "message_topic");
        param.put("timeout", "timeout");
        param.put("retryCount", "retry_count");
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
