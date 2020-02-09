package com.java110;


import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 巡检路线设备关系维护代码生成器
 */
public class InspectionRoutePointRelGeneratorApplication {

    protected InspectionRoutePointRelGeneratorApplication() {
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
        data.setId("irmRelId");
        data.setName("inspection_route_point_rel");
        data.setDesc("巡检路线巡检点关系维护");
        data.setShareParam("communityId");
        data.setShareColumn("community_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_INSPECTION_ROUTE_MACHINE_REL");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_INSPECTION_ROUTE_MACHINE_REL");
        data.setNewBusinessTypeCdValue("500200030002");
        data.setDeleteBusinessTypeCdValue("500200050002");
        data.setBusinessTableName("business_inspection_route_machine_rel");
        data.setTableName("inspection_route_machine_rel");
        Map<String, String> param = new HashMap<String, String>();
        param.put("irmRelId", "irm_relId");       //map的key为你自定义的字段名就是驼峰命名法的那个，value为数据库表的字段名
        param.put("inspectionRouteId", "inspection_routeId");
        param.put("inspectionId", "inspection_id");
        param.put("communityId", "community_id");
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
