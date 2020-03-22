package com.java110.code.back;

/**
 * 内部服务类实现类
 */
public class GeneratorIInnerServiceSMO extends BaseGenerator {

    /**
     * 生成代码
     *
     * @param data 数据
     */
    public void generator(Data data) {
        StringBuffer sb = readFile(this.getClass().getResource("/template/IInnerServiceSMO.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("商户", data.getDesc());
        String writePath = this.getClass().getResource("/listener").getPath() + "/I"
                + toUpperCaseFirstOne(data.getName()) + "InnerServiceSMO.java";
        writeFile(writePath,
                fileContext);
    }
}
