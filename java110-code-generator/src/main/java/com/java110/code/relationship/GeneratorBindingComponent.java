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
        //genneratorListListener(data);

        //genneratorServiceCodeConstant(data);


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

//        //替换校验部分代码 @@validateTemplateColumns@@
//        JSONArray columns = data.getJSONArray("columns");
//        StringBuffer validateStr = new StringBuffer();
//        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
//            JSONObject column = columns.getJSONObject(columnIndex);
//            if (column.getBoolean("required")) {
//                validateStr.append("Assert.hasKeyAndValue(paramIn, \"" + column.getString("code") + "\", \"" + column.getString("desc") + "\");\n");
//            }
//        }
//
//        fileContext = fileContext.replace("@@validateTemplateColumns@@", validateStr.toString());


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
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/add/SaveListener.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        //替换校验部分代码 @@validateTemplateColumns@@
        JSONArray columns = data.getJSONArray("columns");
        StringBuffer validateStr = new StringBuffer();
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            JSONObject column = columns.getJSONObject(columnIndex);
            if (column.getBoolean("required")) {
                validateStr.append("Assert.hasKeyAndValue(reqJson, \"" + column.getString("code") + "\", \"" + column.getString("desc") + "\");\n");
            }
        }

        fileContext = fileContext.replace("@@validateTemplateColumns@@", validateStr.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/listener/" + data.getString("templateCode") + "/Save" + toUpperCaseFirstOne(data.getString("templateCode")) + "Listener.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }


}
