package com.java110.code.newBack;

import com.java110.code.util.FileUtilBase;

import java.util.Map;

/**
 *
 */
public class GeneratorDtoBean extends BaseGenerator {


    /**
     * 拼装 查询数量
     *
     * @param data        数据
     * @param fileContext 文件内容
     * @return filContext 数据
     */
    private String dealVariableAndGetSet(Data data, String fileContext) {

        Map<String, String> params = data.getParams();

        String variable = "";
        String variableGetSet = "";

        for (String key : params.keySet()) {
            if ("operate".equals(key) || "bId".equals(key) || "statusCd".equals(key)|| "createTime".equals(key)) {
                continue;
            }
            variable += "private String " + key + ";\n";

            variableGetSet += "public String get" + toUpperCaseFirstOne(key) + "() {\n"
                    + "        return " + key + ";\n"
                    + "    }\n";
            variableGetSet += "public void set" + toUpperCaseFirstOne(key) + "(String " + key + ") {\n"
                    + "        this." + key + " = " + key + ";\n"
                    + "    }\n";


        }


        fileContext = fileContext.replace("$beanVariable$", variable);
        fileContext = fileContext.replace("$beanVariableGetSet$", variableGetSet);

        return fileContext;
    }


    /**
     * 生成代码
     *
     * @param data 数据
     */
    public void generator(Data data) throws Exception {
        StringBuffer sb = readFile(this.getClass().getResource("/template/dto.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("@@templateCode@@",data.getName())
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("商户", data.getDesc());

        fileContext = dealVariableAndGetSet(data, fileContext);
        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/dto/" + data.getName() + "/" + toUpperCaseFirstOne(data.getName()) + "Dto.java";

        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "java110-bean\\src\\main\\java\\com\\java110\\dto\\" + data.getName() + "/" + toUpperCaseFirstOne(data.getName()) + "Dto.java");
        }
    }
}
