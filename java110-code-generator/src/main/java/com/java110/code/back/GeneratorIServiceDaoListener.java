package com.java110.code.back;

import com.java110.code.util.FileUtilBase;

public class GeneratorIServiceDaoListener extends BaseGenerator {





    /**
     * 生成代码
     * @param data
     */
    public void generator(Data data) throws Exception {
        StringBuffer sb = readFile(this.getClass().getResource("/template/IServiceDao.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store",toLowerCaseFirstOne(data.getName()))
                .replace("@@shareName@@",data.getShareName())
                .replace("Store",toUpperCaseFirstOne(data.getName()))
                .replace("商户",data.getDesc());

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/dao/I"+toUpperCaseFirstOne(data.getName())+"ServiceDao.java";
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, toUpperCaseFirstOne(data.getShareName().toString()) + "Service\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\dao\\" + "I" + toUpperCaseFirstOne(data.getName()) + "ServiceDao.java");
        }
    }
}
