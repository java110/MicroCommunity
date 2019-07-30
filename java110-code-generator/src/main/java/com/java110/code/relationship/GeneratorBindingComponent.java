package com.java110.code.relationship;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.code.BaseGenerator;
import com.java110.code.web.GeneratorStart;
import org.springframework.util.StringUtils;

public class GeneratorBindingComponent extends BaseGenerator {

    public void generator(JSONObject data) {

        //处理组件
        generatorComponentHtml(data);
        generatorComponentJs(data);
        generatorComponentJava(data);
        genneratorIListSmo(data);
        genneratorListSmoImpl(data);


        genneratorListListener(data);

        //genneratorServiceCodeConstant(data);


    }

    private void genneratorAddHtml(JSONObject data, String componentName) {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/relationship/add/add.html").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceBindingTemplateContext(fileContext, data);

        // 处理 th 信息

        StringBuffer thSb = new StringBuffer();

        JSONObject _currentObj = data.getJSONObject("components").getJSONObject(componentName);

        JSONArray columns = _currentObj.getJSONArray("columns");
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            JSONObject column = columns.getJSONObject(columnIndex);
            if ("none".equals(column.getString("inputType"))) {
                continue;
            }
            String required = column.getBoolean("required") ? "必填" : "选填";
            String inputStr = "";
            if ("select".equals(column.getString("inputType"))) {

                String[] selectValues = column.getString("selectValue").split(",");
                String[] selectValueNames = column.getString("selectValueName").split(",");


                String option = "";
                for (int valueIndex = 0; valueIndex < selectValues.length; valueIndex++) {

                    String value = selectValues[valueIndex];

                    option += "<option  value=\"" + value + "\">" + selectValueNames[valueIndex] + "</option>\n";

                }

                inputStr = "<select class=\"custom-select\" v-model=\"" + _currentObj.getString("templateCode") + "ViewInfo." + column.getString("code") + "\">\n" +
                        "         <option selected  disabled value=\"\">" + required + "，请选择" + column.getString("cnCode") + "</option>\n" +
                        "         " + option +
                        "  </select>";
            } else if ("textarea".equals(column.getString("inputType"))) {
                inputStr = "<textarea  placeholder=\"" + required + "，请填写" + column.getString("cnCode") + "\" class=\"form-control\"" +
                        " v-model=\"" + _currentObj.getString("templateCode") + "ViewInfo." + column.getString("code") + "\">" +
                        "</textarea>";
            } else {
                inputStr = "           <input v-model=\"" + _currentObj.getString("templateCode") + "ViewInfo." + column.getString("code") + "\" " +
                        "                  type=\"text\" placeholder=\"" + required + "，请填写" + column.getString("cnCode") + "\" class=\"form-control\">\n";
            }
            thSb.append("<div class=\"form-group row\">\n" +
                    "         <label class=\"col-sm-2 col-form-label\">" + column.getString("cnCode") + "</label>\n" +
                    "         <div class=\"col-sm-10\">\n" +
                    inputStr +
                    "         </div>\n" +
                    "</div>\n");

        }

        fileContext = fileContext.replace("@@addTemplateColumns@@", thSb.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/relationship/component/" + _currentObj.getString("package") + "/" + _currentObj.getString("templateCode") + "View/" + toUpperCaseFirstOne(_currentObj.getString("templateCode")) + "View.html";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }

    /**
     * 生成 html js java 类
     *
     * @param data
     */
    private void generatorAddJs(JSONObject data, String componentName) {

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/relationship/add/add.js").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceBindingTemplateContext(fileContext, data);


        JSONObject _currentObj = data.getJSONObject("components").getJSONObject(componentName);
        fileContext = fileContext.replace("@@columnTemplateCode@@", _currentObj.getString("templateCode"));
        fileContext = fileContext.replace("@@ColumnTemplateCode@@", toUpperCaseFirstOne(_currentObj.getString("templateCode")));

        //替换 变量@@templateCodeColumns@@
        JSONArray columns = _currentObj.getJSONArray("columns");

        StringBuffer variable = new StringBuffer();
        String defaultValue = "";

        String validateInfo = "";
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            JSONObject column = columns.getJSONObject(columnIndex);
            defaultValue = column.getBoolean("hasDefaultValue") ? column.getString("defaultValue") : "";
            defaultValue = "'" + defaultValue + "'";
            variable.append(column.getString("code") + ":" + defaultValue + ",\n");

            validateInfo += "'" + _currentObj.getString("templateCode") + "ViewInfo." + column.getString("code") + "':[\n";
            if (column.getBoolean("required")) {
                validateInfo += "{\n" +
                        "                            limit:\"required\",\n" +
                        "                            param:\"\",\n" +
                        "                            errInfo:\"" + column.getString("cnCode") + "不能为空\"\n" +
                        "                        },\n";
            }

            if (column.containsKey("limit") && !StringUtils.isEmpty(column.getString("limit"))) {
                validateInfo += " {\n" +
                        "                            limit:\"" + column.getString("limit") + "\",\n" +
                        "                            param:\"" + column.getString("limitParam") + "\",\n" +
                        "                            errInfo:\"" + column.getString("limitErrInfo") + "\"\n" +
                        "                        },\n" +
                        "                    ],\n";
            }

        }
        fileContext = fileContext.replace("@@templateCodeColumns@@", variable.toString());
        fileContext = fileContext.replace("@@addTemplateCodeValidate@@", validateInfo);

        // 替换 数据校验部分代码


        String writePath = this.getClass().getResource("/").getPath()
                + "out/relationship/component/" + _currentObj.getString("package") + "/" + _currentObj.getString("templateCode") + "View/" + toUpperCaseFirstOne(_currentObj.getString("templateCode")) + "View.js";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);


    }

    /**
     * 生成常量类
     *
     * @param data
     */
    private void genneratorServiceCodeConstant(JSONObject data) {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/constant/ServiceCodeConstant.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/constant/" + data.getString("templateCode") + "/ServiceCode" + toUpperCaseFirstOne(data.getString("templateCode")) + "Constant.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);

    }

    /**
     * 生成 html js java 类
     *
     * @param data
     */
    private void generatorComponentHtml(JSONObject data) {

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/relationship/binding/binding.html").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceBindingTemplateContext(fileContext, data);

        //  @@allStep@@

        StringBuffer allStep = new StringBuffer();

        JSONArray flows = data.getJSONArray("flows");
        for (int flowIndex = 0; flowIndex < flows.size(); flowIndex++) {
            JSONObject flow = flows.getJSONObject(flowIndex);
            String showAffirmPage = data.getBoolean("needAffirm") ? " || " + data.getString("templateCode") + "Info.index == " + flows.size() : "";
            allStep.append("<div v-if=\"" + data.getString("templateCode") + "Info.index == " + flowIndex + showAffirmPage + "\">\n" +
                    "        <vc:create name=\"" + flow.getString("vcName") + "\"\n" +
                    "                   callBackListener=\"" + data.getString("templateCode") + "\"\n" +
                    "                   callBackFunction=\"notify\"\n" +
                    "        ></vc:create>\n" +
                    "    </div>\n");
            //如果相应组件不存在，则根据组件配置自动生成
            if (!flow.getBoolean("existsComponent")) {
                genneratorAddHtml(data, flow.getString("vcName"));
            }
        }

        int stepLastIndex = data.getBoolean("needAffirm") ? flows.size() : flows.size() - 1;
        fileContext = fileContext.replace(" @@allStep@@", allStep.toString())
                .replace("@@stepLastIndex@@", stepLastIndex + "");


        String writePath = this.getClass().getResource("/").getPath()
                + "out/relationship/component/" + data.getString("package") + "/" + data.getString("templateCode") + "/" + data.getString("templateCode") + ".html";
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

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/relationship/binding/binding.js").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceBindingTemplateContext(fileContext, data);

        //替换 变量@@templateCodeColumns@@
        JSONArray columns = data.getJSONArray("columns");

        StringBuffer variable = new StringBuffer();
        String defaultValue = "";

        StringBuffer validateInfo = new StringBuffer();
        StringBuffer allStep = new StringBuffer("[");
        JSONArray flows = data.getJSONArray("flows");
        for (int flowIndex = 0; flowIndex < flows.size(); flowIndex++) {
            JSONObject flow = flows.getJSONObject(flowIndex);
            allStep.append("\"" + flow.getString("cnCode") + "\"");
            if (flowIndex < flows.size() - 1) {
                allStep.append(",");
            }


            validateInfo.append("vc.emit('" + flow.getString("vcName") + "', 'onIndex', vc.component.serviceBindingInfo.index);\n");

            //如果相应组件不存在，则根据组件配置自动生成
            if (!flow.getBoolean("existsComponent")) {
                generatorAddJs(data, flow.getString("vcName"));
            }

        }
        String showAffirmPage = data.getBoolean("needAffirm") ? "确认信息" : "";
        allStep.append(showAffirmPage);
        allStep.append("]");
        fileContext = fileContext.replace("@@stepTitle@@", allStep.toString());
        fileContext = fileContext.replace("@@notifyOnIndex@@", validateInfo.toString());
        fileContext = fileContext.replace("@@jumpUrl@@", data.getString("successUrl"));

        // 替换 数据校验部分代码


        String writePath = this.getClass().getResource("/").getPath()
                + "out/relationship/component/" + data.getString("package") + "/" + data.getString("templateCode") + "/" + data.getString("templateCode") + ".js";
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

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/relationship/binding/BindingComponent.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceBindingTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/relationship/component/java/" + data.getString("templateCode") + "/" + toUpperCaseFirstOne(data.getString("templateCode")) + "BindingComponent.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);


    }

    /**
     * 生成接口类
     *
     * @param data
     */
    private void genneratorIListSmo(JSONObject data) {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/relationship/binding/IBindingSMO.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceBindingTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/relationship/smo/" + data.getString("templateCode") + "/I" + toUpperCaseFirstOne(data.getString("templateCode")) + "BindingSMO.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }

    /**
     * 生成接口类
     *
     * @param data
     */
    private void genneratorListSmoImpl(JSONObject data) {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/relationship/binding/BindingSMOImpl.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceBindingTemplateContext(fileContext, data);

        //替换校验部分代码 @@validateTemplateColumns@@

        JSONArray flows = data.getJSONArray("flows");
        StringBuffer validateStr = new StringBuffer();
        for (int flowIndex = 0; flowIndex < flows.size(); flowIndex++) {

            JSONObject flowObj = flows.getJSONObject(flowIndex);

            if (flowObj.containsKey("existsComponent") && flowObj.getBoolean("existsComponent")) {
                continue;
            }

            String vcName = flowObj.getString("vcName");

            JSONObject vcObject = data.getJSONObject("components").getJSONObject(vcName);

            JSONArray columns = vcObject.getJSONArray("columns");

            for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
                JSONObject column = columns.getJSONObject(columnIndex);
                if (column.getBoolean("required")) {
                    validateStr.append("Assert.hasKeyByFlowData(infos, \"" + flowObj.getString("vcName") + "\", \"" + column.getString("code") + "\", \"" + column.getString("desc") + "\");\n");
                }
            }
        }

        fileContext = fileContext.replace("@@validateTemplateColumns@@", validateStr.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/relationship/smo/" + data.getString("templateCode") + "/impl/" + toUpperCaseFirstOne(data.getString("templateCode")) + "BindingSMOImpl.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorListListener(JSONObject data) {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/relationship/binding/BindingListener.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceBindingTemplateContext(fileContext, data);

        //替换校验部分代码 @@validateTemplateColumns@@
        JSONArray flows = data.getJSONArray("flows");
        StringBuffer validateStr = new StringBuffer();
        StringBuffer variableStr = new StringBuffer();

        StringBuffer ifCode = new StringBuffer();

        StringBuffer methodCode = new StringBuffer();
        for (int flowIndex = 0; flowIndex < flows.size(); flowIndex++) {

            JSONObject flowObj = flows.getJSONObject(flowIndex);

            String vcName = flowObj.getString("vcName");

            variableStr.append("JSONObject " + vcName + " = getObj(infos, \"" + vcName + "\");\n");

            ifCode.append("" +
                    "        if(!hasKey("+vcName+", \"" + flowObj.getString("flowKey") + "\")){\n" +
                    "             "+vcName+".put(\"" + flowObj.getString("flowKey") + "\", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_" + flowObj.getString("flowKey") + "));\n" +
                    "             businesses.add(add" + toUpperCaseFirstOne(flowObj.getString("businessName")) + "("+vcName+", context));\n" +
                    "        }\n");


            methodCode.append("private JSONObject add"+toUpperCaseFirstOne(flowObj.getString("businessName"))+"(JSONObject paramInJson, DataFlowContext dataFlowContext) {\n" +
                    "        JSONObject business = JSONObject.parseObject(\"{\\\"datas\\\":{}}\");\n" +
                    "        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant."+flowObj.getString("businessType")+");\n" +
                    "        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);\n" +
                    "        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);\n" +
                    "        JSONObject businessObj = new JSONObject();\n" +
                    "        businessObj.putAll(paramInJson);\n" +
                    "        //计算 应收金额\n" +
                    "        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(\""+flowObj.getString("businessName")+"\", businessObj);\n" +
                    "        return business;\n" +
                    "    }\n");
            if (flowObj.containsKey("existsComponent") && flowObj.getBoolean("existsComponent")) {
                continue;
            }


            JSONObject vcObject = data.getJSONObject("components").getJSONObject(vcName);

            JSONArray columns = vcObject.getJSONArray("columns");

            for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
                JSONObject column = columns.getJSONObject(columnIndex);
                if (column.getBoolean("required")) {
                    validateStr.append("Assert.hasKeyByFlowData(infos, \"" + flowObj.getString("vcName") + "\", \"" + column.getString("code") + "\", \"" + column.getString("desc") + "\");\n");
                }
            }
        }

        fileContext = fileContext.replace("@@validateTemplateColumns@@", validateStr.toString());
        fileContext = fileContext.replace("@@doSoService@@", variableStr.toString() + ifCode.toString());
        fileContext = fileContext.replace("@@bindingMethod@@", methodCode.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/listener/" + data.getString("templateCode") + "/Binding" + toUpperCaseFirstOne(data.getString("templateCode")) + "Listener.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }


}
