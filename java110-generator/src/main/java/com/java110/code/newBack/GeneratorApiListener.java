package com.java110.code.newBack;

import com.java110.code.util.FileUtilBase;
import com.java110.code.web.GeneratorStart;

import java.util.Map;

public class GeneratorApiListener extends BaseGenerator {


    /**
     * 生成代码
     *
     * @param data
     */
    public void generator(Data data) throws Exception {
        genneratorPo(data);             //API DataVo对象
        genneratorISaveBmo(data);               //API  IBmo
        genneratorSaveBmoImpl(data);            //Api BmoImpl
        genneratorIUpdateBmo(data);               //API  IBmo
        genneratorUpdateBmoImpl(data);            //Api BmoImpl
        genneratorIDeleteBmo(data);               //API  IBmo
        genneratorDeleteBmoImpl(data);            //Api BmoImpl
        genneratorIGetBmo(data);               //API  IBmo
        genneratorGetBmoImpl(data);            //Api BmoImpl
        genneratorApi(data);
    }


    private void genneratorPo(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/newTemplate/Po.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);
        Map columns = data.getParams();
        String variable = "";
        String variableGetSet = "";

        for (Object key : columns.keySet()) {
            //JSONObject column = columns.get(columnIndex);
            //String key = column.getString("code");
            if ("operate".equals(key) || "bId".equals(key) || "createTime".equals(key)) {
                continue;
            }

            if ("statusCd".equals(key)) {
                variable += "private String " + key.toString() + " = \"0\";\n";
            } else {
                variable += "private String " + key.toString() + ";\n";
            }

            variableGetSet += "public String get" + toUpperCaseFirstOne(key.toString()) + "() {\n"
                    + "        return " + key + ";\n"
                    + "    }\n";
            variableGetSet += "public void set" + toUpperCaseFirstOne(key.toString()) + "(String " + key + ") {\n"
                    + "        this." + key + " = " + key + ";\n"
                    + "    }\n";
        }

        fileContext = fileContext.replace("@@templateColumns@@", variable + variableGetSet);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/po/" + data.getName() + "/" + toUpperCaseFirstOne(data.getName()) + "Po.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "java110-bean\\src\\main\\java\\com\\java110\\po\\" + data.getName() + "/" + toUpperCaseFirstOne(data.getName()) + "Po.java");
        }
    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorISaveBmo(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/newTemplate/ISaveBMO.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/bmo/" + data.getName() + "/ISave" + toUpperCaseFirstOne(data.getName()) + "BMO.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-"+data.getShareName()+"\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\bmo\\" + data.getName() + "/ISave" + toUpperCaseFirstOne(data.getName()) + "BMO.java");
        }
    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorSaveBmoImpl(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/newTemplate/SaveBMOImpl.java").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("@@shareName@@", data.getShareName())
                .replace("@@ShareName@@", toUpperCaseFirstOne(data.getShareName()));

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/bmo/" + data.getName() + "/impl/Save" + toUpperCaseFirstOne(data.getName()) + "BMOImpl.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-"+data.getShareName()+"\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\bmo\\" + data.getName() + "/impl/Save" + toUpperCaseFirstOne(data.getName()) + "BMOImpl.java");
        }
    }


    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorIUpdateBmo(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/newTemplate/IUpdateBMO.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/bmo/" + data.getName() + "/IUpdate" + toUpperCaseFirstOne(data.getName()) + "BMO.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-"+data.getShareName()+"\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\bmo\\" + data.getName() + "/IUpdate" + toUpperCaseFirstOne(data.getName()) + "BMO.java");
        }
    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorUpdateBmoImpl(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/newTemplate/UpdateBMOImpl.java").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("@@shareName@@", data.getShareName())
                .replace("@@ShareName@@", toUpperCaseFirstOne(data.getShareName()));

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/bmo/" + data.getName() + "/impl/Update" + toUpperCaseFirstOne(data.getName()) + "BMOImpl.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-"+data.getShareName()+"\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\bmo\\" + data.getName() + "/impl/Update" + toUpperCaseFirstOne(data.getName()) + "BMOImpl.java");
        }
    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorIDeleteBmo(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/newTemplate/IDeleteBMO.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/bmo/" + data.getName() + "/IDelete" + toUpperCaseFirstOne(data.getName()) + "BMO.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-"+data.getShareName()+"\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\bmo\\" + data.getName() + "/IDelete" + toUpperCaseFirstOne(data.getName()) + "BMO.java");
        }
    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorDeleteBmoImpl(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/newTemplate/DeleteBMOImpl.java").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("@@shareName@@", data.getShareName())
                .replace("@@ShareName@@", toUpperCaseFirstOne(data.getShareName()));

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/bmo/" + data.getName() + "/impl/Delete" + toUpperCaseFirstOne(data.getName()) + "BMOImpl.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-"+data.getShareName()+"\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\bmo\\" + data.getName() + "/impl/Delete" + toUpperCaseFirstOne(data.getName()) + "BMOImpl.java");
        }
    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorIGetBmo(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/newTemplate/IGetBMO.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/bmo/" + data.getName() + "/IGet" + toUpperCaseFirstOne(data.getName()) + "BMO.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-"+data.getShareName()+"\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\bmo\\" + data.getName() + "/IGet" + toUpperCaseFirstOne(data.getName()) + "BMO.java");
        }
    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorGetBmoImpl(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/newTemplate/GetBMOImpl.java").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("@@shareName@@", data.getShareName())
                .replace("@@ShareName@@", toUpperCaseFirstOne(data.getShareName()));

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/bmo/" + data.getName() + "/impl/Get" + toUpperCaseFirstOne(data.getName()) + "BMOImpl.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-"+data.getShareName()+"\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\bmo\\" + data.getName() + "/impl/Get" + toUpperCaseFirstOne(data.getName()) + "BMOImpl.java");
        }
    }

    private void genneratorApi(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/newTemplate/Api.java").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("@@shareName@@", data.getShareName())
                .replace("@@ShareName@@", toUpperCaseFirstOne(data.getShareName()));

        fileContext = super.replaceTemplateContext(fileContext, data);
        //替换保存
        StringBuffer validateStr = new StringBuffer();
        for (String key : data.getRequiredParam()) {
            //JSONObject column = columns.getJSONObject(columnIndex);
            validateStr.append("Assert.hasKeyAndValue(reqJson, \"" + key + "\", \"请求报文中未包含" + key + "\");\n");
        }
        fileContext = fileContext.replace("@@validateSaveTemplateColumns@@", validateStr.toString());

        //替换 修改
        validateStr.append("Assert.hasKeyAndValue(reqJson, \"" + data.getId() + "\", \"" + data.getId() + "不能为空\");\n");
        fileContext = fileContext.replace("@@validateUpdateTemplateColumns@@", validateStr.toString());

        validateStr = new StringBuffer();
        validateStr.append("Assert.hasKeyAndValue(reqJson, \"" + data.getId() + "\", \"" + data.getId() + "不能为空\");\n");
        fileContext = fileContext.replace("@@validateDeleteTemplateColumns@@", validateStr.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/api/" + toUpperCaseFirstOne(data.getName()) + "Api.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-"+data.getShareName()+"\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\api\\" + toUpperCaseFirstOne(data.getName()) + "Api.java");
        }
    }




}
