package com.java110.code;

public class GeneratorServiceDaoImplListener extends BaseGenerator {





    /**
     * 生成代码
     * @param data
     */
    public void generator(Data data){
        StringBuffer sb = readFile(this.getClass().getResource("/template/ServiceDaoImpl.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store",toLowerCaseFirstOne(data.getName()))
                .replace("Store",toUpperCaseFirstOne(data.getName()))
                .replace("商户",data.getDesc());
        System.out.println(this.getClass().getResource("/listener").getPath());
        String writePath = this.getClass().getResource("/listener").getPath()+"/"+toUpperCaseFirstOne(data.getName())+"ServiceDaoImpl.java";
        writeFile(writePath,
                fileContext);
    }
}
