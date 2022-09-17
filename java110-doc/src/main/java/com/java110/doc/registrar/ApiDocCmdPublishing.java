package com.java110.doc.registrar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文档 发布类
 */
public class ApiDocCmdPublishing {


    /**
     * 保存cmdDocs
     */
    private static final List<Map> cmdDocs = new ArrayList<Map>();

    /**
     * 添加cmd文档
     *
     * @param doc
     */
    public static void addCmdDoc(Map doc) {
        cmdDocs.add(doc);
    }
}
