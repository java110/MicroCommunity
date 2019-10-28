package com.java110;


import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class AuditMessageGeneratorApplication {

    protected AuditMessageGeneratorApplication() {
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
        data.setId("auditMessageId");
        data.setName("auditMessage");
        data.setDesc("审核原因");
        data.setShareParam("storeId");
        data.setShareColumn("store_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_AUDIT_MESSAGE");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_AUDIT_MESSAGE");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_AUDIT_MESSAGE");
        data.setNewBusinessTypeCdValue("170200030001");
        data.setUpdateBusinessTypeCdValue("170200040001");
        data.setDeleteBusinessTypeCdValue("170200050001");
        data.setBusinessTableName("business_audit_message");
        data.setTableName("audit_message");
        Map<String, String> param = new HashMap<String, String>();
        param.put("auditMessageId", "audit_message_id");       //map的key为你自定义的字段名就是驼峰命名法的那个，value为数据库表的字段名
        param.put("storeId", "store_id");
        param.put("userId", "user_id");
        param.put("userName", "user_name");
        param.put("auditOrderId", "audit_order_id");
        param.put("auditOrderType", "audit_order_type");
        param.put("state", "state");
        param.put("message", "message");
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
