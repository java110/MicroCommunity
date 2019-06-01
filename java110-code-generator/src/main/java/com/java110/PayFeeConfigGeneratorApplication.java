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
public class PayFeeConfigGeneratorApplication {

    protected PayFeeConfigGeneratorApplication() {
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
        data.setId("configId");
        data.setName("feeConfig");
        data.setDesc("费用配置");
        data.setShareParam("communityId");
        data.setShareColumn("community_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_FEE_CONFIG");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_FEE_CONFIG");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_FEE_CONFIG");
        data.setNewBusinessTypeCdValue("620100030001");
        data.setUpdateBusinessTypeCdValue("620100040001");
        data.setDeleteBusinessTypeCdValue("620100050001");
        data.setBusinessTableName("business_pay_fee_config");
        data.setTableName("pay_fee_config");
        Map<String, String> param = new HashMap<String, String>();
        param.put("configId", "config_id");
        param.put("communityId", "community_id");
        param.put("feeTypeCd", "fee_type_cd");
        param.put("bId", "b_id");
        param.put("squarePrice", "square_price");
        param.put("additionalAmount", "additional_amount");
        param.put("startTime", "start_time");
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
