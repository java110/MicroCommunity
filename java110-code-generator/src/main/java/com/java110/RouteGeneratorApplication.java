package com.java110;


import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class RouteGeneratorApplication {

    protected RouteGeneratorApplication() {
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
        data.setId("id");
        data.setName("route");
        data.setDesc("路由");
        data.setShareParam("id");
        data.setShareColumn("id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_NOTICE");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_NOTICE");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_NOTICE");
        data.setNewBusinessTypeCdValue("550100030001");
        data.setUpdateBusinessTypeCdValue("550100040001");
        data.setDeleteBusinessTypeCdValue("550100050001");
        data.setBusinessTableName("business_route");
        data.setTableName("c_route");
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", "id");
        param.put("appId", "app_id");
        param.put("serviceId", "service_id");
        param.put("orderTypeCd", "order_type_cd");
        param.put("invokeLimitTimes", "invoke_limit_times");
        param.put("invokeModel", "invoke_model");
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
