package com.java110.code.back;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.code.web.GeneratorStart;

import java.util.*;

/**
 * Hello world!
 */
public class    BackCoderGeneratorStart extends BaseGenerator {


    protected BackCoderGeneratorStart() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    /**
     * 代码生成器 入口方法
     * 此处生成的mapper文件包含过程表和实例表的sql,所以要求两张表的特殊字段也要写上
     * BusinessTypeCd
     *
     * @param args 参数
     */
    public static void main(String[] args) throws Exception {

        //加载配置
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/back/template_1.json").getFile());

        JSONObject dataJson = JSONObject.parseObject(sb.toString());

        Data data = new Data();
        if(dataJson.containsKey("autoMove")){
            data.setAutoMove(dataJson.getBoolean("autoMove"));
        }
        data.setId(dataJson.getString("id"));
        data.setName(dataJson.getString("name"));
        data.setDesc(dataJson.getString("desc"));
        data.setShareParam(dataJson.getString("shareParam"));
        data.setShareColumn(dataJson.getString("shareColumn"));
        data.setShareName(dataJson.getString("shareName"));
        data.setNewBusinessTypeCd(dataJson.getString("newBusinessTypeCd"));
        data.setUpdateBusinessTypeCd(dataJson.getString("updateBusinessTypeCd"));
        data.setDeleteBusinessTypeCd(dataJson.getString("deleteBusinessTypeCd"));
        data.setNewBusinessTypeCdValue(dataJson.getString("newBusinessTypeCdValue"));
        data.setUpdateBusinessTypeCdValue(dataJson.getString("updateBusinessTypeCdValue"));
        data.setDeleteBusinessTypeCdValue(dataJson.getString("deleteBusinessTypeCdValue"));
        data.setBusinessTableName(dataJson.getString("businessTableName"));
        data.setTableName(dataJson.getString("tableName"));
        data.setParams(dataJson.getJSONObject("param"));

        JSONArray required = dataJson.getJSONArray("required");

        List<String> paramList = new ArrayList<String>();
        for (int reqIndex = 0; reqIndex < required.size(); reqIndex++) {
            JSONObject require = required.getJSONObject(reqIndex);
            paramList.add(require.getString("code"));
        }
        data.setRequiredParam(paramList.toArray(new String[required.size()]));

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

        GeneratorApiListener generatorApiListener = new GeneratorApiListener();
        generatorApiListener.generator(data);
    }
}
