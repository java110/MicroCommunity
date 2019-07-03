package com.java110.code.web;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.code.BaseGenerator;
import org.springframework.util.StringUtils;

public class GeneratorFlow extends BaseGenerator {

    public void generator(JSONObject data) {

        //处理组件
        generatorComponentHtml(data);




    }

    /**
     * 生成 html js java 类
     *
     * @param data
     */
    private void generatorComponentHtml(JSONObject data) {

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/flow.html").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);


        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/" + data.getString("templateCode") + "/" + data.getString("templateCode") + "Flow.html";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);


    }




}
