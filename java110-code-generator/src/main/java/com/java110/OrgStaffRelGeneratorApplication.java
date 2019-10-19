package com.java110;


import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class OrgStaffRelGeneratorApplication {

    protected OrgStaffRelGeneratorApplication() {
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
        data.setId("relId");
        data.setName("orgStaffRel");
        data.setDesc("组织员工关系");
        data.setShareParam("storeId");
        data.setShareColumn("store_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_ORG_STAFF_REL");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_ORG_STAFF_REL");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_ORG_STAFF_REL");
        data.setNewBusinessTypeCdValue("140200030001");
        data.setUpdateBusinessTypeCdValue("140200040001");
        data.setDeleteBusinessTypeCdValue("140200050001");
        data.setBusinessTableName("business_org_staff_rel");
        data.setTableName("u_org_staff_rel");
        Map<String, String> param = new HashMap<String, String>();
        param.put("relId", "rel_id");       //map的key为你自定义的字段名就是驼峰命名法的那个，value为数据库表的字段名
        param.put("storeId", "store_id");
        param.put("orgId", "org_id");
        param.put("staffId", "staff_id");
        param.put("relCd", "rel_cd");
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
