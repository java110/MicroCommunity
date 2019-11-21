package com.java110.front.listener;

import com.java110.front.core.PackageScanner;
import com.java110.front.core.VueComponentTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ReloadComponentFileListener extends PackageScanner implements Runnable {

    private Logger logger = LoggerFactory.getLogger(ReloadComponentFileListener.class);

    /**
     * 默认扫描组件路径
     */
    private static final String DEFAULT_COMPONENT_PACKAGE_PATH = "components";


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


    private static final Map<String,Long> commentInfo = new HashMap<String,Long>();

    /**
     * 心跳加载组件信息 标志 true 为定时加载组件信息
     */
    private static boolean RELOAD_COMPONENT_FLAG = false;

    /**
     * 心跳时间，默认为5秒
     */
    private static long HEARTBEAT_TIME = 5 * 1000;

    public ReloadComponentFileListener() {

    }

    public ReloadComponentFileListener(boolean flag) {
        RELOAD_COMPONENT_FLAG = flag;
    }

    public ReloadComponentFileListener(boolean flag, long time) {
        RELOAD_COMPONENT_FLAG = flag;
        HEARTBEAT_TIME = time;
    }

    @Override
    public void run() {
        while (RELOAD_COMPONENT_FLAG) {
            try {
                reloadComponent();
                Thread.sleep(HEARTBEAT_TIME);//线程休息
            } catch (Throwable e) {
                logger.error("加载组件失败：", e);
            }
        }

    }

    private void reloadComponent() {
        logger.debug("开始扫描是否有组件添加或修改");
        VueComponentTemplate vueComponentTemplate = new VueComponentTemplate();
        vueComponentTemplate.packageScanner(DEFAULT_COMPONENT_PACKAGE_PATH, COMPONENT_JS);
        vueComponentTemplate.packageScanner(DEFAULT_COMPONENT_PACKAGE_PATH, COMPONENT_HTML);
        vueComponentTemplate.packageScanner(DEFAULT_COMPONENT_PACKAGE_PATH, COMPONENT_CSS);
        logger.debug("扫描完成是否有组件添加或修改");

    }

    @Override
    protected void handleResource(String filePath) {

        File componentFile = new File(filePath);
        Long lastModified = componentFile.lastModified();
        if(!commentInfo.containsKey(componentFile.getName())){
            commentInfo.put(componentFile.getName(), lastModified);
            reloadComponentContext(filePath);
        }
        Long prevModified = commentInfo.get(componentFile.getName());
        if (lastModified > prevModified) {
            reloadComponentContext(filePath);
            commentInfo.put(componentFile.getName(), lastModified);
        }
    }

    private void reloadComponentContext(String filePath){
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
            VueComponentTemplate.refreshComponent(componentKey, sb);

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
