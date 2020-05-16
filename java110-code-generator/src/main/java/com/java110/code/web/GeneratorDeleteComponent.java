package com.java110.code.web;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.code.back.BaseGenerator;
import com.java110.code.util.FileUtilBase;

public class GeneratorDeleteComponent extends BaseGenerator {

    public void generator(JSONObject data) throws Exception {

        //处理组件
        generatorComponentHtml(data);
        generatorComponentJs(data);
        generatorComponentJava(data);
        genneratorIListSmo(data);
        genneratorListSmoImpl(data);
//        genneratorListListener(data);



    }

    /**
     * 生成 html js java 类
     *
     * @param data
     */
    private void generatorComponentHtml(JSONObject data) {

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/delete/delete.html").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);



        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/component/"+data.getString("templateCode")+"Package/delete" + toUpperCaseFirstOne(data.getString("templateCode")) + "/delete" + toUpperCaseFirstOne(data.getString("templateCode")) + ".html";
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

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/delete/delete.js").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);


        // 替换 数据校验部分代码


        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/component/"+data.getString("templateCode")+"Package/delete" + toUpperCaseFirstOne(data.getString("templateCode")) + "/delete" + toUpperCaseFirstOne(data.getString("templateCode")) + ".js";
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

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/delete/DeleteComponent.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/component/java/" + data.getString("templateCode") + "/Delete" + toUpperCaseFirstOne(data.getString("templateCode")) + "Component.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath,"FrontService\\src\\main\\java\\com\\java110\\front\\components\\" +data.getString("templateCode") + "/Delete" + toUpperCaseFirstOne(data.getString("templateCode")) + "Component.java");



    }

    /**
     * 生成接口类
     *
     * @param data
     */
    private void genneratorIListSmo(JSONObject data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/delete/IDeleteSMO.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/smo/" + data.getString("templateCode") + "/IDelete" + toUpperCaseFirstOne(data.getString("templateCode")) + "SMO.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath,"FrontService\\src\\main\\java\\com\\java110\\front\\smo\\" +data.getString("templateCode") + "/IDelete" + toUpperCaseFirstOne(data.getString("templateCode")) + "SMO.java");

    }

    /**
     * 生成接口类
     *
     * @param data
     */
    private void genneratorListSmoImpl(JSONObject data) throws Exception {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/delete/DeleteSMOImpl.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        //替换校验部分代码 @@validateTemplateColumns@@
        JSONArray columns = data.getJSONArray("columns");
        StringBuffer validateStr = new StringBuffer();
        validateStr.append("Assert.hasKeyAndValue(paramIn, \""+data.getString("templateKey")+"\", \""+data.getString("templateKeyName")+"不能为空\");\n");


        fileContext = fileContext.replace("@@validateTemplateColumns@@", validateStr.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/web/smo/" + data.getString("templateCode") + "/impl/Delete" + toUpperCaseFirstOne(data.getString("templateCode")) + "SMOImpl.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
        //复制生成的文件到对应分区目录下
        FileUtilBase.copyfile(writePath,"FrontService\\src\\main\\java\\com\\java110\\front\\smo\\" +data.getString("templateCode") + "/impl/Delete" + toUpperCaseFirstOne(data.getString("templateCode")) + "SMOImpl.java");

    }

    /**
     * 生成API 侦听处理类
     *
     * @param data
     */
    private void genneratorListListener(JSONObject data) {
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/delete/DeleteListener.java").getFile());
        String fileContext = sb.toString();

        fileContext = super.replaceTemplateContext(fileContext, data);

        //替换校验部分代码 @@validateTemplateColumns@@
        JSONArray columns = data.getJSONArray("columns");
        StringBuffer validateStr = new StringBuffer();
        validateStr.append("Assert.hasKeyAndValue(reqJson, \""+data.getString("templateKey")+"\", \""+data.getString("templateKeyName")+"不能为空\");\n");

        fileContext = fileContext.replace("@@validateTemplateColumns@@", validateStr.toString());


        String writePath = this.getClass().getResource("/").getPath()
                + "out/api/listener/" + data.getString("templateCode") + "/Delete" + toUpperCaseFirstOne(data.getString("templateCode")) + "Listener.java";
        System.out.printf("writePath: " + writePath);
        writeFile(writePath,
                fileContext);
    }



}
