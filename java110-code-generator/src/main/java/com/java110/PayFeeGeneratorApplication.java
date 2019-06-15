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
public class PayFeeGeneratorApplication {

    protected PayFeeGeneratorApplication() {
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
        data.setId("feeId");
        data.setName("fee");
        data.setDesc("费用");
        data.setShareParam("communityId");
        data.setShareColumn("community_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_FEE_INFO");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_FEE_INFO");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_FEE_INFO");
        data.setNewBusinessTypeCdValue("600100030001");
        data.setUpdateBusinessTypeCdValue("600100040001");
        data.setDeleteBusinessTypeCdValue("600100050001");
        data.setBusinessTableName("business_pay_fee");
        data.setTableName("pay_fee");
        Map<String, String> param = new HashMap<String, String>();
        param.put("feeId", "fee_id");
        param.put("communityId", "community_id");
        param.put("bId", "b_id");
        param.put("feeTypeCd", "fee_type_cd");
        param.put("payerObjId", "payer_obj_id");
        param.put("incomeObjId", "income_obj_id");
        param.put("startTime", "start_time");
        param.put("endTime", "end_time");
        param.put("amount", "amount");
        param.put("userId", "user_id");
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
