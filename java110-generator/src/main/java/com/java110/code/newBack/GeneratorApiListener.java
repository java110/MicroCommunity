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
        //genneratorIBmo(data);               //API  IBmo
        //genneratorBmoImpl(data);            //Api BmoImpl
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
    private void genneratorIBmo(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/newTemplate/IBMO.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/bmo/" + data.getName() + "/I" + toUpperCaseFirstOne(data.getName()) + "BMO.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-api\\src\\main\\java\\com\\java110\\api\\bmo\\" + data.getName() + "/I" + toUpperCaseFirstOne(data.getName()) + "BMO.java");
        }
    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorBmoImpl(Data data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/template/BMOImpl.java").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("@@shareName@@", data.getShareName())
                .replace("@@ShareName@@", toUpperCaseFirstOne(data.getShareName()));

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/bmo/" + data.getName() + "/impl/" + toUpperCaseFirstOne(data.getName()) + "BMOImpl.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-api\\src\\main\\java\\com\\java110\\api\\bmo\\" + data.getName() + "/impl/" + toUpperCaseFirstOne(data.getName()) + "BMOImpl.java");
        }
    }


}
