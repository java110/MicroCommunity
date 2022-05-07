package com.java110.code.back;

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
        genneratorSaveCmd(data);            //Api BmoImpl
        genneratorUpdateBmoImpl(data);            //Api BmoImpl
        genneratorDeleteBmoImpl(data);            //Api BmoImpl
        genneratorGetBmoImpl(data);            //Api BmoImpl

    }


    private void genneratorPo(Data data) throws Exception {
        StringBuffer sb = readFile(this.getClass().getResource("/newTemplate/Po.txt").getFile());
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
    private void genneratorSaveCmd(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/newTemplate/SaveCmd.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("@@shareName@@", data.getShareName())
                .replace("@@ShareName@@", toUpperCaseFirstOne(data.getShareName()));

        fileContext = super.replaceTemplateContext(fileContext, data);

        StringBuffer validateStr = new StringBuffer();
        for (String key : data.getRequiredParam()) {
            //JSONObject column = columns.getJSONObject(columnIndex);
            validateStr.append("Assert.hasKeyAndValue(reqJson, \"" + key + "\", \"请求报文中未包含" + key + "\");\n");

        }

        fileContext = fileContext.replace("@@validateTemplateColumns@@", validateStr.toString());

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/cmd/" + data.getName() + "/Save" + toUpperCaseFirstOne(data.getName()) + "Cmd.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-"+data.getShareName()+"\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\cmd\\" + data.getName() + "/Save" + toUpperCaseFirstOne(data.getName()) + "Cmd.java");
        }
    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorUpdateBmoImpl(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/newTemplate/UpdateCmd.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("@@shareName@@", data.getShareName())
                .replace("@@ShareName@@", toUpperCaseFirstOne(data.getShareName()));

        fileContext = super.replaceTemplateContext(fileContext, data);

        StringBuffer validateStr = new StringBuffer();
        validateStr.append("Assert.hasKeyAndValue(reqJson, \"" + data.getId() + "\", \"" + data.getId() + "不能为空\");\n");
        validateStr.append("Assert.hasKeyAndValue(reqJson, \"" + data.getShareParam() + "\", \"" + data.getShareParam() + "不能为空\");\n");

        fileContext = fileContext.replace("@@validateTemplateColumns@@", validateStr.toString());

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/cmd/" + data.getName() + "/Update" + toUpperCaseFirstOne(data.getName()) + "Cmd.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-"+data.getShareName()+"\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\cmd\\" + data.getName() + "/Update" + toUpperCaseFirstOne(data.getName()) + "Cmd.java");
        }
    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorDeleteBmoImpl(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/newTemplate/DeleteCmd.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("@@shareName@@", data.getShareName())
                .replace("@@ShareName@@", toUpperCaseFirstOne(data.getShareName()));

        fileContext = super.replaceTemplateContext(fileContext, data);
        StringBuffer validateStr = new StringBuffer();
        validateStr.append("Assert.hasKeyAndValue(reqJson, \"" + data.getId() + "\", \"" + data.getId() + "不能为空\");\n");
        validateStr.append("Assert.hasKeyAndValue(reqJson, \"" + data.getShareParam() + "\", \"" + data.getShareParam() + "不能为空\");\n");

        fileContext = fileContext.replace("@@validateTemplateColumns@@", validateStr.toString());

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/cmd/" + data.getName() + "/Delete" + toUpperCaseFirstOne(data.getName()) + "Cmd.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-"+data.getShareName()+"\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\cmd\\" + data.getName() + "/Delete" + toUpperCaseFirstOne(data.getName()) + "Cmd.java");
        }
    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorGetBmoImpl(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/newTemplate/ListCmd.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("@@shareName@@", data.getShareName())
                .replace("@@ShareName@@", toUpperCaseFirstOne(data.getShareName()));

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/cmd/" + data.getName() + "/List" + toUpperCaseFirstOne(data.getName()) + "Cmd.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-"+data.getShareName()+"\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\cmd\\" + data.getName() + "/List" + toUpperCaseFirstOne(data.getName()) + "Cmd.java");
        }
    }


}
