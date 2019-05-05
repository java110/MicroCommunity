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
 *
 */
public class RoomAttrGeneratorApplication {

    protected RoomAttrGeneratorApplication() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }
    /**
     *  代码生成器 入口方法
     * @param args 参数
     */
    public static void main(String[] args) {
        Data data = new Data();
        data.setId("attrId");
        data.setName("roomAttr");
        data.setDesc("小区房屋属性");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_ROOM_INFO");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_ROOM_INFO");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_ROOM_INFO");
        data.setNewBusinessTypeCdValue("530100030001");
        data.setUpdateBusinessTypeCdValue("530100040001");
        data.setDeleteBusinessTypeCdValue("530100050001");
        data.setBusinessTableName("business_building_room_attr");
        data.setTableName("building_room_attr");
        Map<String, String> param = new HashMap<String, String>();
        param.put("roomId", "room_id");
        param.put("bId", "b_id");
        param.put("attrId", "attr_id");
        param.put("specCd", "spec_cd");
        param.put("value", "value");
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
