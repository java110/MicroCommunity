package com.java110.doc.registrar;

import com.java110.doc.entity.CmdDocDto;

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
    private static final List<CmdDocDto> cmdDocs = new ArrayList<CmdDocDto>();

    /**
     * 添加cmd文档
     *
     * @param doc
     */
    public static void addCmdDoc(CmdDocDto doc) {
        cmdDocs.add(doc);
    }
}
