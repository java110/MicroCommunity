package com.java110.code.web;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.code.BaseGenerator;
import org.springframework.util.StringUtils;

public class GeneratorViewComponent extends BaseGenerator {

    public void generator(JSONObject data) {

        //处理组件
        generatorComponentHtml(data);
        generatorComponentJs(data);

    }


    /**
     * 生成 html js java 类
     *
     * @param data
     */
    private void generatorComponentHtml(JSONObject data) {

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/view/viewInfo.html").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        // 处理 th 信息

        StringBuffer thSb = new StringBuffer();

        JSONArray columns = data.getJSONArray("columns");
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            JSONObject column = columns.getJSONObject(columnIndex);
            if(columnIndex % 3 == 0){
                thSb.append("<div class=\"row\">\n");
            }

            thSb.append("<div class=\"col-sm-4\">\n" +
                    "                        <div class=\"form-group\">\n" +
                    "                            <label class=\"col-form-label\" >"+column.getString("cnCode")+"：</label>\n" +
                    "                            <label class=\"\">{{view"+toUpperCaseFirstOne(data.getString("templateCode"))+"Info."+column.getString("code")+"}}</label>\n" +
                    "                        </div>\n" +
                    "</div>\n");

            if(columnIndex % 3 == 2 || columnIndex == columns.size() -1){
                thSb.append("</div>\n");
            }

        }

        fileContext = fileContext.replace("@@viewInfo@@", thSb.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/component/"+data.getString("templateCode")+"Package/view-" + data.getString("templateCode") + "-info/view" + toUpperCaseFirstOne(data.getString("templateCode")) + "Info.html";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);


    }

    /**
     * 生成 html js java 类
     *
     * @param data
     */
    private void generatorComponentJs(JSONObject data) {

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/view/viewInfo.js").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        //替换 变量@@templateCodeColumns@@
        JSONArray columns = data.getJSONArray("columns");

        StringBuffer variable = new StringBuffer();
        String defaultValue = "";

        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            JSONObject column = columns.getJSONObject(columnIndex);
            variable.append(column.getString("code") + ":'',\n");

        }
        fileContext =  fileContext.replace("@@templateCodeColumns@@", variable.toString());

        // 替换 数据校验部分代码


        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/component/"+data.getString("templateCode")+"Package/view-"
                + data.getString("templateCode") + "-info/view" + toUpperCaseFirstOne(data.getString("templateCode")) + "Info.js";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);


    }


}
