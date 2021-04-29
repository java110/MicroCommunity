package com.java110.code.back;

import com.java110.code.util.FileUtilBase;
import org.springframework.util.StringUtils;

import java.util.Map;

public class GeneratorSaveInfoListener extends BaseGenerator {


    /**
     * 生成代码
     *
     * @param data
     */
    public void generator(Data data) throws Exception {
        StringBuffer sb = readFile(this.getClass().getResource("/template/SaveListener.txt").getFile());
        String fileContext = sb.toString();
        if (StringUtils.isEmpty(data.getShareParam())) {
            data.setShareParam(data.getId());
        }

        if (StringUtils.isEmpty(data.getShareColumn())) {
            data.setShareColumn(data.getParams().get(data.getShareColumn()).toString());
        }
        fileContext = fileContext.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("@@templateCode@@", data.getName())
                .replace("@@shareName@@", data.getShareName())
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("商户", data.getDesc())
                .replace("BUSINESS_TYPE_SAVE_STORE_INFO", data.getNewBusinessTypeCd())
                .replace("stare_id", data.getShareColumn())
                .replace("shareId", data.getShareParam())
                .replace(data.getName() + "Id", data.getId())
                .replace(data.getName() + "_id", data.getParams().get(data.getId()).toString());
        String writePath = this.getClass().getResource("/").getPath()
                + "out/back/listener/" + data.getName() + "/Save" + toUpperCaseFirstOne(data.getName()) + "InfoListener.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "service-" + data.getShareName() + "\\src\\main\\java\\com\\java110\\" + data.getShareName() + "\\listener\\" + data.getName() + "/Save" + toUpperCaseFirstOne(data.getName()) + "InfoListener.java");
        }
        //生成协议

        /**
         * |businessstoreMember|memberTypeCd|1|String|30|成员类型|成员类型|
         */
        StringBuffer sbDoc = readFile(this.getClass().getResource("/template/serviceDoc.txt").getFile());
        String fileContextDoc = sbDoc.toString();
        fileContextDoc = fileContextDoc.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("商户", data.getDesc())
                .replace("$businessTypeCd$", data.getNewBusinessTypeCdValue());

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
                + "out/back/docs/" + data.getName() + "/Save" + toUpperCaseFirstOne(data.getName()) + "Info.md";
        writeFile(writePathDoc,
                fileContextDoc);
        //复制生成的文件到对应分区目录下
        if (data.isAutoMove()) {
            FileUtilBase.copyfile(writePath, "docs\\document\\services\\" + data.getName() + "/Save" + toUpperCaseFirstOne(data.getName()) + "Info.md");

        }//生成协议
    }
}
