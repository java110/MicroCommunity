package com.java110.code.back;

import java.util.Map;

public class GeneratorAbstractBussiness extends BaseGenerator {

    /**
     * 生成代码
     *
     * @param data
     */
    public void generator(Data data) {
        StringBuffer sb = readFile(this.getClass().getResource("/template/AbstractBusinessServiceDataFlowListener.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("商户", data.getDesc())
                .replace(data.getName()+"Id", data.getId())
                .replace(data.getName()+"_id", data.getParams().get(data.getId()).toString());
        Map<String, String> param = data.getParams();
        String mappingContext = "";
        String autoMappingContext = "";
        for (String key : param.keySet()) {
            if ("statusCd".equals(key) || "bId".equals(key)) {
                continue;
            }
            mappingContext += "business" + toUpperCaseFirstOne(data.getName()) + "Info.put(\"" + key + "\",business" + toUpperCaseFirstOne(data.getName()) + "Info.get(\"" + param.get(key) + "\"));\n";
            autoMappingContext += "current" + toUpperCaseFirstOne(data.getName()) + "Info.put(\"" + key + "\",current" + toUpperCaseFirstOne(data.getName()) + "Info.get(\"" + param.get(key) + "\"));\n";

        }

        mappingContext += "business" + toUpperCaseFirstOne(data.getName()) + "Info.remove(\"bId\");";

        fileContext = fileContext.replace("$flushBusinessInfo$", mappingContext);
        fileContext = fileContext.replace("$autoSaveDelBusiness$", autoMappingContext);
        System.out.println(this.getClass().getResource("/listener").getPath());
        String writePath = this.getClass().getResource("/listener").getPath() + "/Abstract" + toUpperCaseFirstOne(data.getName()) + "BusinessServiceDataFlowListener.java";
        writeFile(writePath,
                fileContext);
    }
}
