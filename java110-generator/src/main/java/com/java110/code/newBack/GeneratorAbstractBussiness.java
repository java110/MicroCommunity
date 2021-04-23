package com.java110.code.newBack;

import com.java110.code.util.FileUtilBase;

import java.util.Map;

public class GeneratorAbstractBussiness extends BaseGenerator {

    /**
     * 生成代码
     *
     * @param data
     */
    public void generator(Data data) throws Exception {
        StringBuffer sb = readFile(this.getClass().getResource("/template/AbstractBusinessServiceDataFlowListener.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("@@shareName@@", data.getShareName())
                .replace("@@templateCode@@", data.getName())
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("商户", data.getDesc())
                .replace(data.getName() + "Id", data.getId())
                .replace(data.getName() + "_id", data.getParams().get(data.getId()).toString());
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
        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/listener/" + data.getName() + "/Abstract" + toUpperCaseFirstOne(data.getName()) + "BusinessServiceDataFlowListener.java";
        writeFile(writePath,
                fileContext);
//        System.out.println("看这里"+toUpperCaseFirstOne(data.getShareName()).toString());
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-" + data.getShareName() + "\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\listener\\" + data.getName() + "/Abstract" + toUpperCaseFirstOne(data.getName()) + "BusinessServiceDataFlowListener.java");
        }
    }
}
