package com.java110.code.back;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.code.util.FileUtilBase;
import com.java110.code.web.GeneratorStart;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

public class GeneratorApiListener extends BaseGenerator {


    /**
     * 生成代码
     *
     * @param data
     */
    public void generator(Data data) throws Exception {
        genneratorListListener(data);       //API监听器
        genneratorVo(data);                //API VO对象
        genneratorDataVo(data);             //API DataVo对象
        genneratorIBmo(data);               //API  IBmo
        genneratorBmoImpl(data);            //Api BmoImpl
        genneratorSaveListener(data);
        genneratorEditListener(data);
        genneratorDeleteListener(data);
        genneratorServiceCodeConstant(data);
    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorListListener(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/template/ListListener.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/listener/" + data.getName() + "/List" + toUpperCaseFirstOne(data.getName()) + "sListener.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath,"Api\\src\\main\\java\\com\\java110\\api\\listener\\"+data.getName() + "/List" + toUpperCaseFirstOne(data.getName()) + "sListener.java");

    }

    private void genneratorVo(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/template/ApiVo.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/vo/" + data.getName() + "/Api" + toUpperCaseFirstOne(data.getName()) + "Vo.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath,"java110-bean\\src\\main\\java\\com\\java110\\vo\\api\\"+data.getName() + "/Api" + toUpperCaseFirstOne(data.getName()) + "Vo.java");

    }

    private void genneratorDataVo(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/template/ApiDataVo.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);
        Map columns = data.getParams();
        String variable = "";
        String variableGetSet = "";

//        variable += "private String " + data.getId() + ";\n";
//
//        variableGetSet += "public String get" + toUpperCaseFirstOne(data.getId()) + "() {\n"
//                + "        return " + data.getId() + ";\n"
//                + "    }\n";
//        variableGetSet += "public void set" + toUpperCaseFirstOne(data.getId()) + "(String " + data.getId() + ") {\n"
//                + "        this." + data.getId() + " = " + data.getId() + ";\n"
//                + "    }\n";

        for (Object key : columns.keySet()) {
            //JSONObject column = columns.get(columnIndex);
            //String key = column.getString("code");
            variable += "private String " + key.toString() + ";\n";

            variableGetSet += "public String get" + toUpperCaseFirstOne(key.toString()) + "() {\n"
                    + "        return " + key + ";\n"
                    + "    }\n";
            variableGetSet += "public void set" + toUpperCaseFirstOne(key.toString()) + "(String " + key + ") {\n"
                    + "        this." + key + " = " + key + ";\n"
                    + "    }\n";
        }

        fileContext = fileContext.replace("@@templateColumns@@", variable + variableGetSet);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/vo/" + data.getName() + "/Api" + toUpperCaseFirstOne(data.getName()) + "DataVo.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath,"java110-bean\\src\\main\\java\\com\\java110\\vo\\api\\"+data.getName() + "/Api" + toUpperCaseFirstOne(data.getName()) + "DataVo.java");

    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorIBmo(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/template/IBMO.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/bmo/" + data.getName() + "/I" + toUpperCaseFirstOne(data.getName()) + "BMO.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath,"Api\\src\\main\\java\\com\\java110\\api\\bmo\\"+data.getName() + "/I" + toUpperCaseFirstOne(data.getName()) + "BMO.java");

    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorBmoImpl(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/template/BMOImpl.java").getFile());
        String fileContext = sb.toString();
        fileContext=fileContext.replace("@@shareName@@",data.getShareName())
                                .replace("@@ShareName@@",toUpperCaseFirstOne(data.getShareName()));

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/bmo/" + data.getName() + "/impl/" + toUpperCaseFirstOne(data.getName()) + "BMOImpl.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath,"Api\\src\\main\\java\\com\\java110\\api\\bmo\\"+data.getName() + "/impl/" + toUpperCaseFirstOne(data.getName()) + "BMOImpl.java");

    }


    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorSaveListener(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/template/SaveListener.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        //替换校验部分代码 @@validateTemplateColumns@@
        Map columns = data.getParams();
        StringBuffer validateStr = new StringBuffer();
        for (String key : data.getRequiredParam()) {
            //JSONObject column = columns.getJSONObject(columnIndex);
            validateStr.append("Assert.hasKeyAndValue(reqJson, \"" + key + "\", \"请求报文中未包含" + key + "\");\n");

        }

        fileContext = fileContext.replace("@@validateTemplateColumns@@", validateStr.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/listener/" + data.getName() + "/Save" + toUpperCaseFirstOne(data.getName()) + "Listener.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath,"Api\\src\\main\\java\\com\\java110\\api\\listener\\"+data.getName() + "/Save" + toUpperCaseFirstOne(data.getName()) + "Listener.java");

    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorEditListener(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/template/UpdateListener.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        //替换校验部分代码 @@validateTemplateColumns@@
        StringBuffer validateStr = new StringBuffer();
        validateStr.append("Assert.hasKeyAndValue(reqJson, \"" + data.getId() + "\", \"" + data.getId() + "不能为空\");\n");
        for (String key : data.getRequiredParam()) {
            //JSONObject column = columns.getJSONObject(columnIndex);
            validateStr.append("Assert.hasKeyAndValue(reqJson, \"" + key + "\", \"请求报文中未包含" + key + "\");\n");

        }

        fileContext = fileContext.replace("@@validateTemplateColumns@@", validateStr.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/listener/" + data.getName() + "/Update" + toUpperCaseFirstOne(data.getName()) + "Listener.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath,"Api\\src\\main\\java\\com\\java110\\api\\listener\\"+data.getName() + "/Update" + toUpperCaseFirstOne(data.getName()) + "Listener.java");

    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorDeleteListener(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/template/DeleteListener.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        //替换校验部分代码 @@validateTemplateColumns@@
        StringBuffer validateStr = new StringBuffer();
        validateStr.append("Assert.hasKeyAndValue(reqJson, \"" + data.getId() + "\", \"" + data.getId() + "不能为空\");\n");

        fileContext = fileContext.replace("@@validateTemplateColumns@@", validateStr.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/listener/" + data.getName() + "/Delete" + toUpperCaseFirstOne(data.getName()) + "Listener.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath,"Api\\src\\main\\java\\com\\java110\\api\\listener\\"+data.getName() + "/Delete" + toUpperCaseFirstOne(data.getName()) + "Listener.java");

    }


    /**
     * 生成常量类
     *
     * @param data
     */
    private void genneratorServiceCodeConstant(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/template/ServiceCodeConstant.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/constant/" + data.getName() + "/ServiceCode" + toUpperCaseFirstOne(data.getName()) + "Constant.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath,"java110-utils\\src\\main\\java\\com\\java110\\utils\\constant\\" + "/ServiceCode" + toUpperCaseFirstOne(data.getName()) + "Constant.java");


    }

}
