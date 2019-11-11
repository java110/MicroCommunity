package com.java110;


import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class FileRelGeneratorApplication {

    protected FileRelGeneratorApplication() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    /**
     * 代码生成器 入口方法
     *  此处生成的mapper文件包含过程表和实例表的sql,所以要求两张表的特殊字段也要写上
     *   BusinessTypeCd
     *   `file_rel_id` varchar(30) NOT NULL COMMENT '文件关系ID，主键',
     *   `b_id` varchar(30) NOT NULL COMMENT '业务Id',
     *   `rel_type_cd` varchar(30) NOT NULL COMMENT '文件类型,用来做分区,业主照片，商户照片，具体查看t_dict表',
     *   `save_way` varchar(12) NOT NULL COMMENT '存放方式，ftp table,fastdfs 具体查看t_dict表',
     *   `obj_id` varchar(30) NOT NULL COMMENT '对象ID，及说明这个文件归宿于谁，业主则填写业主ID',
     *   `file_real_name` varchar(200) NOT NULL COMMENT '文件真实名称',
     *   `file_save_name` varchar(200) NOT NULL COMMENT '文件存储名称',
     * @param args 参数
     */
    public static void main(String[] args) {
        Data data = new Data();
        data.setId("fileRelId");
        data.setName("fileRel");
        data.setDesc("文件存放");
        data.setShareParam("relTypeCd");
        data.setShareColumn("rel_type_cd");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_FILE_REL");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_FILE_REL");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_FILE_REL");
        data.setNewBusinessTypeCdValue("220200030001");
        data.setUpdateBusinessTypeCdValue("220200040001");
        data.setDeleteBusinessTypeCdValue("220200050001");
        data.setBusinessTableName("business_file_rel");
        data.setTableName("file_rel");
        Map<String, String> param = new HashMap<String, String>();
        param.put("fileRelId", "file_rel_id");       //map的key为你自定义的字段名就是驼峰命名法的那个，value为数据库表的字段名
        param.put("relTypeCd", "rel_type_cd");
        param.put("saveWay", "save_way");
        param.put("objId", "obj_id");
        param.put("fileRealName", "file_real_name");
        param.put("fileSaveName", "file_save_name");
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
