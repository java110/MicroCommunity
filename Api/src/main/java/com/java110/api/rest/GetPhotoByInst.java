package com.java110.api.rest;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * @ClassName GetPhotoByInst
 * @Description TODO
 * @Author wuxw
 * @Date 2019/8/28 22:49
 * @Version 1.0
 * add by wuxw 2019/8/28
 **/
public class GetPhotoByInst {

    final static String PHOTO_INFO_PATH= "photoInfo.txt";
    final static String DEFAULTE_PHOTO_DIR= "./Api/photos/";

    public static void main(String[] args) {
        //读取文件
        Reader reader = null;
        String sb = "";
        try {
            InputStream inputStream = new ClassPathResource(PHOTO_INFO_PATH).getInputStream();
            //InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(File.separator + filePath);
            reader = new InputStreamReader(inputStream, "UTF-8");
            int tempChar;
            StringBuffer b = new StringBuffer();
            while ((tempChar = reader.read()) != -1) {
                b.append((char) tempChar);
            }
            sb = b.toString();

            String[] strs = sb.split("\n");

            for(String str : strs){
                dealPhoto(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void dealPhoto(String str) {

        String[] tmpPhotoPaths = str.split("\\|");

        String instId = tmpPhotoPaths[0];
        String photoUrl = tmpPhotoPaths[1];

        downloadFromUrl(photoUrl,DEFAULTE_PHOTO_DIR,"qhdx_"+instId+"_17.jpg");
        //downloadFromUrl(photoUrl,DEFAULTE_PHOTO_DIR,instId+".jpg");
    }


    /**
     * 文件下载的方法
     * @param  url 地址
     * @param  dir 目录
     * @return String fileName
     */
    public static String downloadFromUrl(String url, String dir,String fileName) {

        try {
            URL httpurl = new URL(url);
            //	fileName = getFileNameFromUrl(url);
            //String[] us=url.split("/");
            //fileName=us[us.length-1];
            System.out.println("fileName:"+fileName);
            File f = new File(dir + fileName);
            FileUtils.copyURLToFile(httpurl, f);
        } catch (Exception e) {
            e.printStackTrace();
            return "Fault!";
        }
        return fileName;
    }
}