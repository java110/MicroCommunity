package com.java110.code;

/**
 * 更新类 代码生成器
 */
public class GeneratorUpdateInfoListener extends BaseGenerator {





    /**
     * 生成代码
     * @param data 数据分装对象@Data
     */
    public void generator(Data data) {
        StringBuffer sb = readFile(this.getClass().getResource("/template/UpdateInfoListener.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("商户", data.getDesc())
                .replace("BUSINESS_TYPE_UPDATE_STORE_INFO", data.getUpdateBusinessTypeCd());
        String writePath = this.getClass().getResource("/listener").getPath()
                + "/Update" + toUpperCaseFirstOne(data.getName()) + "InfoListener.java";
        writeFile(writePath,
                fileContext);
    }
}
