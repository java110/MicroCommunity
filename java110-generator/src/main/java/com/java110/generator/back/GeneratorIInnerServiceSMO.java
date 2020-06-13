package com.java110.generator.back;

import com.java110.generator.util.FileUtilBase;

/**
 * 内部服务类实现类
 */
public class GeneratorIInnerServiceSMO extends BaseGenerator {

    /**
     * 生成代码
     *
     * @param data 数据
     */
    public void generator(Data data) throws Exception {
        StringBuffer sb = readFile(this.getClass().getResource("/template/IInnerServiceSMO.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("@@shareName@@",data.getShareName())
                .replace("商户", data.getDesc());
        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/core/smo/" + data.getName() + "/I" + toUpperCaseFirstOne(data.getName()) + "InnerServiceSMO.java";
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "java110-core\\src\\main\\java\\com\\java110\\core\\smo\\" + data.getName() + "/I" + toUpperCaseFirstOne(data.getName()) + "InnerServiceSMO.java");
        }
    }
}
