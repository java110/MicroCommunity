package com.java110.generator.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.generator.back.BaseGenerator;
import com.java110.generator.util.FileUtilBase;

/**
 * @ClassName GeneratorChooseComponent
 * @Description TODO
 * @Author wuxw
 * @Date 2019/7/27 10:26
 * @Version 1.0
 * add by wuxw 2019/7/27
 **/
public class GeneratorChooseComponent extends BaseGenerator {
    public void generator(JSONObject data) throws Exception {

        //处理组件
        generatorComponentHtml(data);
        generatorComponentJs(data);
        //generatorComponentJava(data);

    }


    /**
     * 生成 html js java 类
     *
     * @param data
     */
    private void generatorComponentHtml(JSONObject data) {

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/choose/choose.html").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        // 处理 th 信息

        StringBuffer thSb = new StringBuffer();
        StringBuffer tdSb = new StringBuffer();
        thSb.append("                            <th class=\"text-center\">" + data.getString("templateKeyName") + "</th>\n");

        tdSb.append("                            <td class=\"text-center\">{{" + data.getString("templateCode") + "." + data.getString("templateKey") + "}}</td>\n");

        JSONArray columns = data.getJSONArray("columns");
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            JSONObject column = columns.getJSONObject(columnIndex);
            thSb.append("                            <th class=\"text-center\">" + column.getString("cnCode") + "</th>\n");

            tdSb.append("                            <td class=\"text-center\">{{" + data.getString("templateCode") + "." + column.getString("code") + "}}</td>\n");
        }
        thSb.append("                            <th class=\"text-center\">操作</th>\n");

        fileContext = fileContext.replace("@@columnsCnCode@@", thSb.toString())
                .replace("@@columnsName@@", tdSb.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/components/" + data.getString("directories") + "/choose" + toUpperCaseFirstOne(data.getString("templateCode")) + "/choose" + toUpperCaseFirstOne(data.getString("templateCode")) + ".html";
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

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/choose/choose.js").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/components/" + data.getString("directories") + "/choose" + toUpperCaseFirstOne(data.getString("templateCode")) + "/choose" + toUpperCaseFirstOne(data.getString("templateCode")) + ".js";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);


    }

    /**
     * 生成 html js java 类
     *
     * @param data
     */
    private void generatorComponentJava(JSONObject data) throws Exception {

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/choose/ChooseComponent.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/component/java/" + data.getString("templateCode") + "/Choose" + toUpperCaseFirstOne(data.getString("templateCode")) + "Component.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath, "FrontService\\src\\main\\java\\com\\java110\\front\\components\\" + data.getString("templateCode") + "/Choose" + toUpperCaseFirstOne(data.getString("templateCode")) + "Component.java");


    }


}
