package com.java110.code.newBack;

import com.java110.code.util.FileUtilBase;

public class GeneratorServiceDaoImplListener extends BaseGenerator {


    /**
     * 生成代码
     *
     * @param data
     */
    public void generator(Data data) throws Exception {
        StringBuffer sb = readFile(this.getClass().getResource("/newTemplate/ServiceDaoImpl.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("@@shareName@@",data.getShareName())
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("商户", data.getDesc());
        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/dao/impl/" + toUpperCaseFirstOne(data.getName()) + "ServiceDaoImpl.java";
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-" + data.getShareName() + "\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\dao\\impl\\" + toUpperCaseFirstOne(data.getName()) + "ServiceDaoImpl.java");
        }
    }
}
