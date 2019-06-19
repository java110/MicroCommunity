package com.java110;


import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class OwnerCarGeneratorApplication {

    protected OwnerCarGeneratorApplication() {
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
        data.setId("carId");
        data.setName("ownerCar");
        data.setDesc("车辆管理");
        data.setShareParam("ownerId");
        data.setShareColumn("owner_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_OWNER_CAR");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_OWNER_CAR");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_OWNER_CAR");
        data.setNewBusinessTypeCdValue("111200030001");
        data.setUpdateBusinessTypeCdValue("111200050001");
        data.setDeleteBusinessTypeCdValue("111200040001");
        data.setBusinessTableName("business_owner_car");
        data.setTableName("owner_car");
        Map<String, String> param = new HashMap<String, String>();
        param.put("carId", "car_id");
        param.put("ownerId", "owner_id");
        param.put("bId", "b_id");
        param.put("carNum", "car_num");
        param.put("carBrand", "car_brand");
        param.put("carType", "car_type");
        param.put("carColor", "car_color");
        param.put("psId", "ps_id");
        param.put("userId", "user_id");
        param.put("remark", "remark");
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
