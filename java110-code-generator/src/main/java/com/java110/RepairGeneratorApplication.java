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
public class RepairGeneratorApplication {

    protected RepairGeneratorApplication() {
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
        data.setId("repairId");
        data.setName("repair");
        data.setDesc("报修信息");
        data.setShareParam("communityId");
        data.setShareColumn("community_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_REPAIR");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_REPAIR");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_REPAIR");
        data.setNewBusinessTypeCdValue("130100030001");
        data.setUpdateBusinessTypeCdValue("130100040001");
        data.setDeleteBusinessTypeCdValue("130100050001");
        data.setBusinessTableName("business_repair_pool");
        data.setTableName("r_repair_pool");
        Map<String, String> param = new HashMap<String, String>();
        param.put("repairId", "repair_id");       //map的key为你自定义的字段名就是驼峰命名法的那个，value为数据库表的字段名
        param.put("repairType", "repair_type");
        param.put("communityId", "community_id");
        param.put("repairName", "repair_name");
        param.put("tel", "tel");
        param.put("roomId", "room_id");
        param.put("appointmentTime", "appointment_time");
        param.put("context", "context");
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
