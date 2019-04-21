package com.java110.code;

public class GeneratorUpdateInfoListener extends BaseGenerator {





    /**
     * 生成代码
     * @param data
     */
    public void generator(Data data){
        StringBuffer sb = readFile(this.getClass().getResource("/template/UpdateInfoListener.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store",toLowerCaseFirstOne(data.getName()))
                .replace("Store",toUpperCaseFirstOne(data.getName()))
                .replace("商户",data.getDesc())
                .replace("BUSINESS_TYPE_UPDATE_STORE_INFO",data.getUpdateBusinessTypeCd());
        System.out.println(this.getClass().getResource("/listener").getPath());
        String writePath = this.getClass().getResource("/listener").getPath()+"/Update"+toUpperCaseFirstOne(data.getName())+"InfoListener.java";
        writeFile(writePath,
                fileContext);
    }
}
