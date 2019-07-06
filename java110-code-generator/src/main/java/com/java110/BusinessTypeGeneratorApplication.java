package com.java110;


import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class BusinessTypeGeneratorApplication {

    protected BusinessTypeGeneratorApplication() {
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
        data.setId("id");//主键ID
        data.setName("c_business_type");//表名
        data.setDesc("BusinessType");//中文表名
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_BUSINESSTYPE_INFO"); //业务类型
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_BUSINESSTYPE_INFO");//修改业务类型
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_BUSINESSTYPE_INFO");//删除业务类型
        data.setNewBusinessTypeCdValue("900100030002");//业务类型编码
        data.setUpdateBusinessTypeCdValue("900100040002");//修修改业务类型编码
        data.setDeleteBusinessTypeCdValue("900100050002");//删除业务类型编码
        data.setBusinessTableName("business_bus");//表模型过程表名称
        data.setTableName("c_business_type");//表模型实列表名称
        data.setShareParam("id");
        data.setShareColumn("id");
        Map<String, String> param = new HashMap<String, String>();
        /**表模型字段绑定**/
        param.put("id", "id");
        param.put("businessTypeCd", "business_type_cd");
        param.put("name", "name");
        param.put("description", "description");
        param.put("userId", "user_id");
        param.put("operate", "operate");
        /**表模型字段绑定end  创建时间CREATE_DATE不需要绑定系统默认自带**/
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
