package com.java110;




import com.java110.code.Data;
import com.java110.code.GeneratorAbstractBussiness;
import com.java110.code.GeneratorDeleteInfoListener;
import com.java110.code.GeneratorIServiceDaoListener;
import com.java110.code.GeneratorSaveInfoListener;
import com.java110.code.GeneratorServiceDaoImplListener;
import com.java110.code.GeneratorServiceDaoImplMapperListener;
import com.java110.code.GeneratorUpdateInfoListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class UnitGeneratorApplication {

    protected UnitGeneratorApplication() {
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
        data.setId("unitId");
        data.setName("unit");
        data.setDesc("小区单元");
        data.setShareParam("unitId");
        data.setShareColumn("unit_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_UNIT_INFO");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_UNIT_INFO");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_UNIT_INFO");
        data.setNewBusinessTypeCdValue("520100030001");
        data.setUpdateBusinessTypeCdValue("520100040001");
        data.setDeleteBusinessTypeCdValue("520100050001");
        data.setBusinessTableName("business_building_unit");
        data.setTableName("building_unit");
        Map<String, String> param = new HashMap<String, String>();
        param.put("unitId", "unit_id");
        param.put("bId", "b_id");
        param.put("unitNum", "unit_num");
        param.put("floorId", "floor_id");
        param.put("layerCount", "layer_count");
        param.put("lift", "lift");
        param.put("statusCd", "status_cd");
        param.put("userId", "user_id");
        param.put("remark", "remark");
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
    }
}
