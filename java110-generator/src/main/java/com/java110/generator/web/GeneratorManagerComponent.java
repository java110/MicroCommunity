package com.java110.generator.web;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.generator.back.BaseGenerator;
import com.java110.generator.util.FileUtilBase;

public class GeneratorManagerComponent extends BaseGenerator {

    public void generator(JSONObject data) throws Exception {

        //处理组件
        generatorComponentHtml(data);
        generatorComponentJs(data);
      //  generatorComponentJava(data);
      //  genneratorIListSmo(data);
      //  genneratorListSmoImpl(data);
//        genneratorVo(data);

//        genneratorDataVo(data);


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

        //处理查询条件
        JSONArray tmpConditions = data.getJSONArray("conditions");

        StringBuffer conditionInput = new StringBuffer();
        StringBuffer vcCreate = new StringBuffer();

        for (int condIndex = 0; condIndex < tmpConditions.size(); condIndex++) {

            JSONObject tmpCond = tmpConditions.getJSONObject(condIndex);

            if (condIndex % 3 == 0) {
                conditionInput.append("<div class=\"row\">\n");
            }

            if (condIndex % 3 == 0 || condIndex % 3 == 1) {
                conditionInput.append("<div class=\"col-sm-4\" ");
            } else if (condIndex % 3 == 2) {
                conditionInput.append("<div class=\"col-sm-3\" ");
            }

            if (condIndex > 2) {
                conditionInput.append("v-if=\"" + data.getString("templateCode") + "ManageInfo.moreCondition == true\">\n");
            } else {
                conditionInput.append(">\n");
            }

            if ("choose".equals(tmpCond.getString("inputType"))) {
                conditionInput.append("                            <div class=\"form-group input-group\">\n" +
                        "                                <input type=\"text\" placeholder=\"请选择" + tmpCond.getString("name") +
                        "\" v-model=\"" + data.getString("templateCode") + "ManageInfo.conditions." + tmpCond.getString("code") + "\" class=\" form-control\">\n" +
                        "                                <div class=\"input-group-prepend\">\n" +
                        "                                    <button type=\"button\" class=\"btn btn-primary btn-sm\" v-on:click=\"_open" + toUpperCaseFirstOne(tmpCond.getString("componentName")) + "Method()\"><i\n" +
                        "                                            class=\"glyphicon glyphicon-search\"></i> 选择\n" +
                        "                                    </button>\n" +
                        "                                </div>\n" +
                        "                            </div>\n");

                if (tmpCond.containsKey("componentName")) {
                    vcCreate.append("<vc:create path=\"" + tmpCond.getString("componentName") + "\"\n" +
                            "               emit" + toUpperCaseFirstOne(tmpCond.getString("componentName")) + "=\"" + data.getString("templateCode") + "Manage\"\n" +
                            "               emitLoadData=\"" + data.getString("templateCode") + "Manage\"\n" +
                            "    ></vc:create>\n");

                }

            } else if ("input".equals(tmpCond.getString("inputType"))) {
                conditionInput.append("<div class=\"form-group\">\n" +
                        "                                <input type=\"text\" placeholder=\"请输入" + tmpCond.getString("name") + "\" v-model=\"" + data.getString("templateCode") + "ManageInfo.conditions." + tmpCond.getString("code") + "\" class=\" form-control\">\n" +
                        "                            </div>");
            } else if ("select".equals(tmpCond.getString("inputType"))){

                String[] selectValues = tmpCond.getString("selectValue").split(",");
                String[] selectValueNames = tmpCond.getString("selectValueName").split(",");


                String option = "";
                for (int valueIndex = 0; valueIndex < selectValues.length; valueIndex++) {

                    String value = selectValues[valueIndex];

                    option += "<option  value=\"" + value + "\">" + selectValueNames[valueIndex] + "</option>\n";

                }

                conditionInput.append("<select class=\"custom-select\" v-model=\"" + data.getString("templateCode") + "ManageInfo.conditions." + tmpCond.getString("code") +"\">\n" +
                        "         <option selected  value=\"\">请选择" + tmpCond.getString("name") + "</option>\n" +
                        "         " +option+
                        "  </select>"
                );

            }

            conditionInput.append("                        </div>");
            if (condIndex == 2) {
                conditionInput.append("<div class=\"col-sm-1\">\n" +
                        "                            <button type=\"button\" class=\"btn btn-primary btn-sm\" v-on:click=\"_query" + toUpperCaseFirstOne(data.getString("templateCode")) + "Method()\">\n" +
                        "                                    <i class=\"glyphicon glyphicon-search\"></i> 查询\n" +
                        "                            </button>\n" +
                        "                        </div>");
            } else if (condIndex % 3 == 2) {
                conditionInput.append("<div class=\"col-sm-1\">\n" +
                        "                        </div>");

            }


            if (condIndex % 3 == 2 || condIndex == tmpConditions.size() - 1) {
                conditionInput.append("</div>\n");
            }
        }

        fileContext = fileContext.replace("@@conditionInput@@", conditionInput.toString());
        fileContext = fileContext.replace("@@vcCreate@@", vcCreate.toString());

        if (tmpConditions.size() > 3) {
            fileContext = fileContext.replace("@@moreCondition@@", "<button type=\"button\"  class=\"btn btn-link btn-sm\" style=\"margin-right:10px;\"  v-on:click=\"_moreCondition()\">{{"+ data.getString("templateCode") +"ManageInfo.moreCondition == true?'隐藏':'更多'}}</button>");
        } else {
            fileContext = fileContext.replace("@@moreCondition@@", "");

        }

        // 处理 th 信息

        StringBuffer thSb = new StringBuffer();
        StringBuffer tdSb = new StringBuffer();
        thSb.append("                            <th class=\"text-center\">" + data.getString("templateKeyName") + "</th>\n");

        tdSb.append("                            <td class=\"text-center\">{{" + data.getString("templateCode") + "." + data.getString("templateKey") + "}}</td>\n");

        JSONArray columns = data.getJSONArray("columns");
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            JSONObject column = columns.getJSONObject(columnIndex);
            if (column.getBoolean("show")) {
                thSb.append("                            <th class=\"text-center\">" + column.getString("cnCode") + "</th>\n");
                tdSb.append("                            <td class=\"text-center\">{{" + data.getString("templateCode") + "." + column.getString("code") + "}}</td>\n");
            }
        }
        thSb.append("                            <th class=\"text-center\">操作</th>\n");
        tdSb.append("                            <td class=\"text-center\"><div class=\"btn-group\">\n" +
                "                                    <button class=\"btn-white btn btn-xs\" v-on:click=\"_openEdit" + toUpperCaseFirstOne(data.getString("templateCode")) + "Model(" + data.getString("templateCode") + ")\">修改</button>\n" +
                "                                </div>\n" +
                "                                <div class=\"btn-group\">\n" +
                "                                    <button class=\"btn-white btn btn-xs\" v-on:click=\"_openDelete" + toUpperCaseFirstOne(data.getString("templateCode")) + "Model(" + data.getString("templateCode") + ")\">删除</button>\n" +
                "                                </div></td>\n");

        fileContext = fileContext.replace("@@columnsCnCode@@", thSb.toString())
                .replace("@@columnsName@@", tdSb.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/pages/" + data.getString("directories") + "/" + data.getString("templateCode") + "Manage/" + data.getString("templateCode") + "Manage.html";
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

        //处理查询条件
        JSONArray tmpConditions = data.getJSONArray("conditions");

        StringBuffer conditionInput = new StringBuffer();

        StringBuffer conditionMethod = new StringBuffer();

        StringBuffer conditionEvent = new StringBuffer();

        for (int condIndex = 0; condIndex < tmpConditions.size(); condIndex++) {

            JSONObject tmpCond = tmpConditions.getJSONObject(condIndex);
            conditionInput.append(tmpCond.getString("code") + ":'',\n");
            if(tmpCond.containsKey("key")){
                conditionInput.append(tmpCond.getString("key") + ":'',\n");
            }

            if (tmpCond.containsKey("componentName")) {
                conditionMethod.append(",\n _open" + toUpperCaseFirstOne(tmpCond.getString("componentName")) + "Method:function(){\n" +
                        "                vc.emit('" + tmpCond.getString("componentName") + "','open" + toUpperCaseFirstOne(tmpCond.getString("componentName")) + "Model',{});\n" +
                        "\n" +
                        "            }"
                );

                conditionEvent.append("vc.on('" + data.getString("templateCode") + "Manage','" + tmpCond.getString("componentName") + "',function(_param){\n" +
                        "              vc.copyObject(_param,vc.component." + data.getString("templateCode") + "ManageInfo.conditions);\n" +
                        "            });\n");
            }

        }

        fileContext = fileContext.replace("@@conditions@@", conditionInput.toString());
        fileContext = fileContext.replace("@@extendMethods@@", conditionMethod.toString());
        fileContext = fileContext.replace("@@extendEvent@@", conditionEvent.toString());

        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/pages/" + data.getString("directories") + "/" + data.getString("templateCode") + "Manage/" + data.getString("templateCode") + "Manage.js";
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

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/manage/ManageComponent.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/component/java/" + data.getString("templateCode") + "/" + toUpperCaseFirstOne(data.getString("templateCode")) + "ManageComponent.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath,"FrontService\\src\\main\\java\\com\\java110\\front\\components\\" +data.getString("templateCode") + "/" + toUpperCaseFirstOne(data.getString("templateCode")) + "ManageComponent.java");


    }

    /**
     * 生成接口类
     *
     * @param data
     */
    private void genneratorIListSmo(JSONObject data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/manage/IListSMO.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/smo/" + data.getString("templateCode") + "/IList" + toUpperCaseFirstOne(data.getString("templateCode")) + "sSMO.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath,"FrontService\\src\\main\\java\\com\\java110\\front\\smo\\" +data.getString("templateCode") + "/IList" + toUpperCaseFirstOne(data.getString("templateCode")) + "sSMO.java");

    }

    /**
     * 生成接口类
     *
     * @param data
     */
    private void genneratorListSmoImpl(JSONObject data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/manage/ListSMOImpl.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/smo/" + data.getString("templateCode") + "/impl/List" + toUpperCaseFirstOne(data.getString("templateCode")) + "sSMOImpl.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath,"FrontService\\src\\main\\java\\com\\java110\\front\\smo\\" +data.getString("templateCode") + "/impl/List" + toUpperCaseFirstOne(data.getString("templateCode")) + "sSMOImpl.java");

    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorListListener(JSONObject data) {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/manage/ListListener.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/listener/" + data.getString("templateCode") + "/List" + toUpperCaseFirstOne(data.getString("templateCode")) + "sListener.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }

    private void genneratorVo(JSONObject data) {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/manage/ApiVo.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/vo/" + data.getString("templateCode") + "/Api" + toUpperCaseFirstOne(data.getString("templateCode")) + "Vo.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }

    private void genneratorDataVo(JSONObject data) {
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
                + "out/api/vo/" + data.getString("templateCode") + "/Api" + toUpperCaseFirstOne(data.getString("templateCode")) + "DataVo.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }

}
