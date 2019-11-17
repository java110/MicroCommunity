package com.java110.front.core;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * 静态资源文件加载器
 * Created by wuxw on 2019/3/18.
 */
public class VueComponentTemplate extends PackageScanner {

    /**
     * 默认扫描路径
     */
    public static final String DEFAULT_COMPONENT_PACKAGE_PATH = "components";

    /**
     * js 文件
     */
    public static final String COMPONENT_JS = "js";

    /**
     * css 文件
     */
    public static final String COMPONENT_CSS = "css";

    /**
     * html 文件
     */
    public static final String COMPONENT_HTML = "html";


    /**
     * HTML 文件缓存器
     */
    private static final Map<String, String> componentTemplate = new HashMap<>();


    /**
     * 初始化 组件信息
     */
    public static void initComponent(String scanPath) {
        VueComponentTemplate vueComponentTemplate = new VueComponentTemplate();
        vueComponentTemplate.packageScanner(scanPath, COMPONENT_JS);
        vueComponentTemplate.packageScanner(scanPath, COMPONENT_HTML);
        vueComponentTemplate.packageScanner(scanPath, COMPONENT_CSS);
    }


    /**
     * 根据组件编码查询模板
     *
     * @param componentCode
     * @return
     */
    public static String findTemplateByComponentCode(String componentCode) {
        if (componentTemplate.containsKey(componentCode)) {
            return componentTemplate.get(componentCode);
        }

        return null;
    }


    /**
     * 处理资源
     *
     * @param filePath
     */
    protected void handleResource(String filePath) {
        Reader reader = null;
        String sb = "";
        try {
            InputStream inputStream = new ClassPathResource(filePath).getInputStream();
            //InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(File.separator + filePath);
            reader = new InputStreamReader(inputStream, "UTF-8");
            int tempChar;
            StringBuffer b = new StringBuffer();
            while ((tempChar = reader.read()) != -1) {
                b.append((char) tempChar);
            }
            sb = b.toString();
            if (StringUtils.isEmpty(sb)) {
                return;
            }
            String componentKey = "";
            //这里在window 读取jar包中文件时，也是 / 但是直接启动时 为\这个 所以不能用 File.separator 
            if (filePath.contains("/")) {
                componentKey = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
            } else {
                componentKey = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length());
            }
            componentTemplate.put(componentKey, sb);

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
}
