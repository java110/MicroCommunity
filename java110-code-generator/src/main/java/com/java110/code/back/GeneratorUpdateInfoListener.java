package com.java110.code.back;

import com.java110.code.util.FileUtilBase;

import java.util.Map;

/**
 * 更新类 代码生成器
 */
public class GeneratorUpdateInfoListener extends BaseGenerator {


    /**
     * 生成代码
     *
     * @param data 数据分装对象@Data
     */
    public void generator(Data data) throws Exception {
        StringBuffer sb = readFile(this.getClass().getResource("/template/UpdateInfoListener.txt").getFile());
        String fileContext = sb.toString();
        fileContext = fileContext.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("@@templateCode@@", data.getName())
                .replace("@@shareName@@", data.getShareName())
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("商户", data.getDesc())
                .replace("BUSINESS_TYPE_UPDATE_STORE_INFO", data.getUpdateBusinessTypeCd())
                .replace(data.getName() + "Id", data.getId())
                .replace(data.getName() + "_id", data.getParams().get(data.getId()).toString());

        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/listener/" + data.getName() + "/Update" + toUpperCaseFirstOne(data.getName()) + "InfoListener.java";
        writeFile(writePath,
                fileContext);

        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, toUpperCaseFirstOne(data.getShareName()).toString() + "Service\\src\\main\\java\\com\\java110\\"+data.getShareName()+"\\listener\\" + data.getName() + "/Update" + toUpperCaseFirstOne(data.getName()) + "InfoListener.java");
            //生成协议
        }
        /**
         * |businessstoreMember|memberTypeCd|1|String|30|成员类型|成员类型|
         */
        StringBuffer sbDoc = readFile(this.getClass().getResource("/template/serviceDoc.txt").getFile());
        String fileContextDoc = sbDoc.toString();
        fileContextDoc = fileContextDoc.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("商户", data.getDesc())
                .replace("保存", "修改")
                .replace("$businessTypeCd$", data.getUpdateBusinessTypeCdValue());

        Map<String, String> tmpParams = data.getParams();
        String tmpLine = "";
        String _tmpLine = "";
        for (String key : tmpParams.keySet()) {
            if ("operate".equals(key) || "statusCd".equals(key) || "bId".equals(key)) {
                continue;
            }
            tmpLine += "|business" + toUpperCaseFirstOne(data.getName()) + "Info|" + key + "|1|String|30|-|-|\n";
            _tmpLine += "        \"" + key + "\":\"填写具体值\",\n";
        }
        _tmpLine = _tmpLine.substring(0, _tmpLine.lastIndexOf(","));
        fileContextDoc = fileContextDoc.replace("$busienssInfo$", tmpLine);
        fileContextDoc = fileContextDoc.replace("$businessInfoJson$", _tmpLine);
        System.out.println(this.getClass().getResource("/listener").getPath());
        String writePathDoc = this.getClass().getResource("/").getPath()
                + "out/back/docs/" + data.getName() + "/Update" + toUpperCaseFirstOne(data.getName()) + "Info.md";
        writeFile(writePathDoc,
                fileContextDoc);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "docs\\document\\services\\" + data.getName() + "/Update" + toUpperCaseFirstOne(data.getName()) + "Info.md");
            //生成协议
        }
    }
}
