package com.java110.code.relationship;


import com.alibaba.fastjson.JSONObject;
import com.java110.code.back.BaseGenerator;
import com.java110.code.web.GeneratorStart;

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

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/relationship/flow.html").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceBindingTemplateContext(fileContext, data);


        String writePath = this.getClass().getResource("/").getPath()
                + "out/relationship/" + data.getString("templateCode") + "/" + data.getString("templateCode") + "Flow.html";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);


    }




}
