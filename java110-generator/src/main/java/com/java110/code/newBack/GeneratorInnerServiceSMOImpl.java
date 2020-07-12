package com.java110.code.newBack;

import com.java110.code.util.FileUtilBase;

/**
 * 内部服务类实现类
 */
public class GeneratorInnerServiceSMOImpl extends BaseGenerator {

    /**
     * 生成代码
     *
     * @param data 数据
     */
    public void generator(Data data) throws Exception {
        StringBuffer sb = readFile(this.getClass().getResource("/newTemplate/InnerServiceSMOImpl.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("@@Id@@",toUpperCaseFirstOne(data.getId()))
                .replace("@@shareName@@",data.getShareName())
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("商户", data.getDesc());
        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/smo/impl/" + toUpperCaseFirstOne(data.getName()) + "InnerServiceSMOImpl.java";
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath,"service-" + data.getShareName() + "\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\smo\\impl\\" + toUpperCaseFirstOne(data.getName()) + "InnerServiceSMOImpl.java");
        }
    }
}
