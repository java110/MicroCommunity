package com.java110;


import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class OwnerAppUserGeneratorApplication {

    protected OwnerAppUserGeneratorApplication() {
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
        data.setId("appUserId");
        data.setName("ownerAppUser");
        data.setDesc("绑定业主");
        data.setShareParam("communityId");
        data.setShareColumn("community_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_OWNER_APP_USER");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_OWNER_APP_USER");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_OWNER_APP_USER");
        data.setNewBusinessTypeCdValue("280200030001");
        data.setUpdateBusinessTypeCdValue("280200040001");
        data.setDeleteBusinessTypeCdValue("280200050001");
        data.setBusinessTableName("business_owner_app_user");
        data.setTableName("owner_app_user");
        Map<String, String> param = new HashMap<String, String>();
        param.put("appUserId", "app_user_id");       //map的key为你自定义的字段名就是驼峰命名法的那个，value为数据库表的字段名
        param.put("memberId", "member_id");
        param.put("communityId", "community_id");
        param.put("communityName", "community_name");
        param.put("appUserName", "app_user_name");
        param.put("idCard", "id_card");
        param.put("link", "link");
        param.put("openId", "open_id");
        param.put("appTypeCd", "app_type_cd");
        param.put("state", "state");
        param.put("remark", "remark");
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
