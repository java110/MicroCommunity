package com.java110;

import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class CodeGeneratorApp
{
    public static void main( String[] args )
    {
        Data data = new Data();
        data.setName("floor");
        data.setDesc("小区楼");
        data.setBusinessTypeCd("sdddfff");
        data.setBusinessTableName("business_floor");
        data.setTableName("f_floor");
        Map<String,String> param = new HashMap<String,String>();
        param.put("floorId","floor_id");
        param.put("bId","b_id");
        param.put("floorNum","floor_num");
        param.put("userId","user_id");
        param.put("statusCd","status_cd");
        param.put("userId","user_id");
        param.put("remark","remark");
        param.put("operate","operate");
        data.setParams(param);
        GeneratorSaveInfoListener  generatorSaveInfoListener = new GeneratorSaveInfoListener();
        generatorSaveInfoListener.generator(data);

        GeneratorAbstractBussiness generatorAbstractBussiness = new GeneratorAbstractBussiness();
        generatorAbstractBussiness.generator(data);

        GeneratorIServiceDaoListener generatorIServiceDaoListener = new GeneratorIServiceDaoListener();
        generatorIServiceDaoListener.generator(data);

        GeneratorServiceDaoImplListener generatorServiceDaoImplListener = new GeneratorServiceDaoImplListener();
        generatorServiceDaoImplListener.generator(data);

        GeneratorServiceDaoImplMapperListener generatorServiceDaoImplMapperListener = new GeneratorServiceDaoImplMapperListener();
        generatorServiceDaoImplMapperListener.generator(data);
    }
}
