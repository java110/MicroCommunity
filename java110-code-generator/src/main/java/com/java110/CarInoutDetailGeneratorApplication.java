package com.java110;


import com.java110.code.Data;
import com.java110.code.GeneratorAbstractBussiness;
import com.java110.code.GeneratorDeleteInfoListener;
import com.java110.code.GeneratorDtoBean;
import com.java110.code.GeneratorIInnerServiceSMO;
import com.java110.code.GeneratorIServiceDaoListener;
import com.java110.code.GeneratorInnerServiceSMOImpl;
import com.java110.code.GeneratorSaveInfoListener;
import com.java110.code.GeneratorServiceDaoImplListener;
import com.java110.code.GeneratorServiceDaoImplMapperListener;
import com.java110.code.GeneratorUpdateInfoListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class CarInoutDetailGeneratorApplication {

    protected CarInoutDetailGeneratorApplication() {
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
        data.setId("detailId");
        data.setName("carInoutDetail");
        data.setDesc("进出场详情");
        data.setShareParam("communityId");
        data.setShareColumn("community_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_CAR_INOUT_DETAIL");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_CAR_INOUT_DETAIL");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_CAR_INOUT_DETAIL");
        data.setNewBusinessTypeCdValue("541120030001");
        data.setUpdateBusinessTypeCdValue("541120040001");
        data.setDeleteBusinessTypeCdValue("541120050001");
        data.setBusinessTableName("business_car_inout_detail");
        data.setTableName("car_inout_detail");
        Map<String, String> param = new HashMap<String, String>();
        param.put("detailId", "detail_id");       //map的key为你自定义的字段名就是驼峰命名法的那个，value为数据库表的字段名
        param.put("inoutId", "inout_id");       //map的key为你自定义的字段名就是驼峰命名法的那个，value为数据库表的字段名
        param.put("carNum", "car_num");
        param.put("communityId", "community_id");
        param.put("machineId", "machine_id");
        param.put("machineCode", "machine_code");
        param.put("carInout", "car_inout");
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
