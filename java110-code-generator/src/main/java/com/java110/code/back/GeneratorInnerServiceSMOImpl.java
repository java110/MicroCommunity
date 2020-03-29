package com.java110.code.back;

/**
 * 内部服务类实现类
 */
public class GeneratorInnerServiceSMOImpl extends BaseGenerator {

    /**
     * 生成代码
     *
     * @param data 数据
     */
    public void generator(Data data) {
        StringBuffer sb = readFile(this.getClass().getResource("/template/InnerServiceSMOImpl.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("商户", data.getDesc());
        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/smo/impl/" + data.getName() + "/" + toUpperCaseFirstOne(data.getName()) + "InnerServiceSMOImpl.java";
        writeFile(writePath,
                fileContext);
    }
}
