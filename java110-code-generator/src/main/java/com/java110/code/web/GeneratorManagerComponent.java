package com.java110.code.web;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.code.BaseGenerator;

public class GeneratorManagerComponent extends BaseGenerator {

    public void generator(JSONObject data) {

        //处理组件
        generatorComponentHtml(data);
        generatorComponentJs(data);
        generatorComponentJava(data);
        genneratorIListSmo(data);
        genneratorListSmoImpl(data);
        genneratorListListener(data);
        genneratorVo(data);

        genneratorDataVo(data);


    }

    /**
     * 生成 html js java 类
     *
     * @param data
     */
    private void generatorComponentHtml(JSONObject data) {

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/manage/manage.html").getFile());
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
        tdSb.append("                            <td class=\"text-center\"><div class=\"btn-group\">\n" +
                "                                    <button class=\"btn-white btn btn-xs\" v-on:click=\"_openEdit"+toUpperCaseFirstOne(data.getString("templateCode"))+"Model("+data.getString("templateCode")+")\">修改</button>\n" +
                "                                </div>\n" +
                "                                <div class=\"btn-group\">\n" +
                "                                    <button class=\"btn-white btn btn-xs\" v-on:click=\"_openDelete"+toUpperCaseFirstOne(data.getString("templateCode"))+"Model("+data.getString("templateCode")+")\">删除</button>\n" +
                "                                </div></td>\n");

        fileContext = fileContext.replace("@@columnsCnCode@@", thSb.toString())
                                .replace("@@columnsName@@", tdSb.toString());



        String writePath = this.getClass().getResource("/").getPath()
                +"out/web/component/"+data.getString("templateCode")+"Package/"+data.getString("templateCode")+"-manage/"+data.getString("templateCode")+"Manage.html";
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

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/manage/manage.js").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                +"out/web/component/"+data.getString("templateCode")+"Package/"+data.getString("templateCode")+"-manage/"+data.getString("templateCode")+"Manage.js";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);


    }

    /**
     * 生成 html js java 类
     *
     * @param data
     */
    private void generatorComponentJava(JSONObject data) {

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/manage/ManageComponent.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                +"out/web/component/java/"+data.getString("templateCode")+"/"+toUpperCaseFirstOne(data.getString("templateCode"))+"ManageComponent.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);


    }

    /**
     * 生成接口类
     * @param data
     */
    private void genneratorIListSmo(JSONObject data){
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/manage/IListSMO.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                +"out/web/smo/"+data.getString("templateCode")+"/IList"+toUpperCaseFirstOne(data.getString("templateCode"))+"sSMO.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }

    /**
     * 生成接口类
     * @param data
     */
    private void genneratorListSmoImpl(JSONObject data){
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/manage/ListSMOImpl.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                +"out/web/smo/"+data.getString("templateCode")+"/impl/List"+toUpperCaseFirstOne(data.getString("templateCode"))+"sSMOImpl.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }

    /**
     * 生成API 侦听处理类
     * @param data
     */
    private void genneratorListListener(JSONObject data){
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/manage/ListListener.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                +"out/api/listener/"+data.getString("templateCode")+"/List"+toUpperCaseFirstOne(data.getString("templateCode"))+"sListener.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }

    private void genneratorVo(JSONObject data){
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/manage/ApiVo.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                +"out/api/vo/"+data.getString("templateCode")+"/Api"+toUpperCaseFirstOne(data.getString("templateCode"))+"Vo.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }

    private void genneratorDataVo(JSONObject data){
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/manage/ApiDataVo.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);
        JSONArray columns = data.getJSONArray("columns");
        String variable = "";
        String variableGetSet = "";

        variable += "private String " + data.getString("templateKey") + ";\n";

        variableGetSet += "public String get" + toUpperCaseFirstOne(data.getString("templateKey")) + "() {\n"
                + "        return " + data.getString("templateKey") + ";\n"
                + "    }\n";
        variableGetSet += "public void set" + toUpperCaseFirstOne(data.getString("templateKey")) + "(String " + data.getString("templateKey") + ") {\n"
                + "        this." + data.getString("templateKey") + " = " + data.getString("templateKey") + ";\n"
                + "    }\n";

        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            JSONObject column = columns.getJSONObject(columnIndex);
            String key = column.getString("code");
            variable += "private String " + key + ";\n";

            variableGetSet += "public String get" + toUpperCaseFirstOne(key) + "() {\n"
                    + "        return " + key + ";\n"
                    + "    }\n";
            variableGetSet += "public void set" + toUpperCaseFirstOne(key) + "(String " + key + ") {\n"
                    + "        this." + key + " = " + key + ";\n"
                    + "    }\n";
        }

        fileContext = fileContext.replace("@@templateColumns@@", variable + variableGetSet);

        String writePath = this.getClass().getResource("/").getPath()
                +"out/api/vo/"+data.getString("templateCode")+"/Api"+toUpperCaseFirstOne(data.getString("templateCode"))+"DataVo.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }

}
