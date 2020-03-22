package com.java110.code.back;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.util.Map;

public class GeneratorSaveInfoListener extends BaseGenerator {


    /**
     * 生成代码
     *
     * @param data
     */
    public void generator(JSONObject data) {
        StringBuffer sb = readFile(this.getClass().getResource("/template/SaveListener.txt").getFile());
        String fileContext = sb.toString();
        if (StringUtils.isEmpty(data.getString("shareParam"))) {
            data.put("shareParam",data.getString("id"));
        }

        if (StringUtils.isEmpty(data.getShareColumn())) {
            data.setShareColumn(data.getParams().get(data.getShareColumn()).toString());
        }
        fileContext = fileContext.replace("store", toLowerCaseFirstOne(data.getName()))
                .replace("Store", toUpperCaseFirstOne(data.getName()))
                .replace("商户", data.getDesc())
                .replace("BUSINESS_TYPE_SAVE_STORE_INFO", data.getNewBusinessTypeCd())
                .replace("stare_id", data.getShareColumn())
                .replace("shareId", data.getShareParam())
                .replace(data.getName()+"Id", data.getId())
                .replace(data.getName()+"_id", data.getParams().get(data.getId()).toString());
        System.out.println(this.getClass().getResource("/listener").getPath());
        String writePath = this.getClass().getResource("/listener").getPath() + "/Save" + toUpperCaseFirstOne(data.getName()) + "InfoListener.java";
        writeFile(writePath,
                fileContext);

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
        String writePathDoc = this.getClass().getResource("/listener").getPath() + "/Save" + toUpperCaseFirstOne(data.getName()) + "Info.md";
        writeFile(writePathDoc,
                fileContextDoc);
    }
}
