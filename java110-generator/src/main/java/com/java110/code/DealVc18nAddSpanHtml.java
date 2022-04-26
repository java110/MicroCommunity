package com.java110.code;

import com.alibaba.fastjson.JSONObject;

import java.io.*;

/**
 * @ClassName DealHtml
 * @Description TODO
 * @Author wuxw
 * @Date 2022/4/20 22:18
 * @Version 1.0
 * add by wuxw 2022/4/20
 **/
public class DealVc18nAddSpanHtml {

    public static void main(String[] args) throws Exception {
        File file = new File("C:\\project\\vip\\MicroCommunityWeb\\public\\pages");
        JSONObject js = new JSONObject();
        listFiles(file, js);
        System.out.println("js = " + js.toJSONString());
    }

    public static void listFiles(File file, JSONObject js) throws Exception {
        if (file.isFile()) {
            if (file.getName().endsWith(".html")) {
                doDealHtml(file, js);
            }
            return;
        }

        File[] files = file.listFiles();

        for (File tmpFile : files) {
            listFiles(tmpFile, js);
        }
    }

    private static void doDealHtml(File tmpFile, JSONObject js) throws Exception {

        String fileName = tmpFile.getName().replace(".html", "");
        System.out.println("fileName=" + fileName + ",dir=" + tmpFile.getPath());
        BufferedReader in = new BufferedReader(new FileReader(tmpFile));
        String str;
        String context = "";
        JSONObject fileNameObj = new JSONObject();
        while ((str = in.readLine()) != null) {
            //doDealHtmlNode(str,fileName);
            context += doDealHtmlNode(str+"\n", fileName, fileNameObj);

        }
        System.out.println(context);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tmpFile));
        bufferedWriter.write(context);
        bufferedWriter.close();


    }

    private static String doDealHtmlNode(String str, String fileName, JSONObject fileNameObj) {
        String vcStr = "<vc:i18n";
        if (!str.contains(vcStr)) {
            return str;
        }


        String endStr = "</vc:i18n>";
        String name = "";

        str = str.replaceAll(vcStr,"<span>"+vcStr);
        str = str.replaceAll(endStr,endStr+"</span>");


        return str;
    }
}
