package com.java110;


import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class MachineRecordGeneratorApplication {

    protected MachineRecordGeneratorApplication() {
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
        data.setId("machineRecordId");
        data.setName("machineRecord");
        data.setDesc("设备上报");
        data.setShareParam("communityId");
        data.setShareColumn("community_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_MACHINE_RECORD");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_MACHINE_RECORD");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_MACHINE_RECORD");
        data.setNewBusinessTypeCdValue("230200030001");
        data.setUpdateBusinessTypeCdValue("230200040001");
        data.setDeleteBusinessTypeCdValue("230200050001");
        data.setBusinessTableName("business_machine_record");
        data.setTableName("machine_record");
        Map<String, String> param = new HashMap<String, String>();
        param.put("machineRecordId", "machine_record_id");       //map的key为你自定义的字段名就是驼峰命名法的那个，value为数据库表的字段名
        param.put("machineId", "machine_id");
        param.put("machineCode", "machine_code");
        param.put("name", "name");
        param.put("openTypeCd", "open_type_cd");
        param.put("communityId", "community_id");
        param.put("tel", "tel");
        param.put("idCard", "id_card");
        param.put("fileId", "file_id");
        param.put("fileTime", "file_time");
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
