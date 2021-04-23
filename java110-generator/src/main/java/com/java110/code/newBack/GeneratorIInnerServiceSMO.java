package com.java110.code.newBack;

import com.java110.code.util.FileUtilBase;

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
        StringBuffer sb = readFile(this.getClass().getResource("/newTemplate/IInnerServiceSMO.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("@@shareName@@",data.getShareName())
                .replace("商户", data.getDesc());
        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/intf/" + data.getShareName() + "/I" + toUpperCaseFirstOne(data.getName()) + "InnerServiceSMO.java";
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "java110-interface\\src\\main\\java\\com\\java110\\intf\\" + data.getShareName() + "/I" + toUpperCaseFirstOne(data.getName()) + "InnerServiceSMO.java");
        }
    }
}
