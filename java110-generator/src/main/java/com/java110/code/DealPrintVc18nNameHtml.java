package com.java110.code;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @ClassName DealHtml
 * @Description TODO
 * @Author wuxw
 * @Date 2022/4/20 22:18
 * @Version 1.0
 * add by wuxw 2022/4/20
 **/
public class DealPrintVc18nNameHtml {

    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\Administrator\\Documents\\project\\hc\\MicroCommunityWeb\\public\\components");
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
            context += (str + "\n");
            //doDealHtmlNode(str,fileName);
            context = doDealHtmlNode(context, fileName, fileNameObj);

        }
        js.put(fileName, fileNameObj);
        System.out.println(context);
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tmpFile));
//        bufferedWriter.write(context);
//        bufferedWriter.close();


    }

    private static String doDealHtmlNode(String str, String fileName, JSONObject fileNameObj) {
        String vcStr = "<vc:i18n name=\"";
        if(!str.contains(vcStr)){
            return str;
        }

        String[] options =  str.split(vcStr);

        String endStr = "</vc:i18n>";
        String name = "";
        for(String optionStr: options){
            if(!optionStr.contains(endStr)){
                continue;
            }
            optionStr = optionStr.substring(0,optionStr.indexOf("\""));

            fileNameObj.put(optionStr,optionStr);

            //str = str.replace(optionStr,"{{vc.i18n('"+name+"','"+fileName+"')}}");
        }

        return str;
    }
}
