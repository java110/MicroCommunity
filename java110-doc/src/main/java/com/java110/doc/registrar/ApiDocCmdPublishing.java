package com.java110.doc.registrar;

import com.java110.doc.entity.CmdDocDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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


    public static List<CmdDocDto> getCmdDocs(String resource) {
        List<CmdDocDto> retCmdDocDto = new ArrayList<>();
        if (cmdDocs.size() < 1) {
            return retCmdDocDto;
        }

        for (CmdDocDto cmdDocDto : cmdDocs) {
            if (cmdDocDto.getResource().equals(resource)) {
                retCmdDocDto.add(cmdDocDto);
            }
        }

        // 根据字段排序

        Collections.sort(retCmdDocDto);

        return retCmdDocDto;
    }


    public static CmdDocDto getCmdDocs(String resource,String serviceCode) {

        if (cmdDocs.size() < 1) {
            return null;
        }

        for (CmdDocDto cmdDocDto : cmdDocs) {
            if (cmdDocDto.getResource().equals(resource) && cmdDocDto.getServiceCode().equals(serviceCode)) {
                return cmdDocDto;
            }
        }

        return null;
    }
}
