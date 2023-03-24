package com.java110.doc.registrar;

import com.java110.doc.entity.ApiDocDto;
import com.java110.doc.entity.RequestMappingsDocDto;

import java.util.Collections;
import java.util.List;

/**
 * 文档 发布类
 */
public class ApiDocPublishing {

    private static ApiDocDto apiDocDto;


    /**
     * 保存cmdDocs
     */
    private static List<RequestMappingsDocDto> mappingsDocDtos;

    /**
     * 添加cmd文档
     *
     * @param mappingsDocDtos
     */
    public static void setApiDoc(ApiDocDto apiDocDto, List<RequestMappingsDocDto> mappingsDocDtos) {
        ApiDocPublishing.apiDocDto = apiDocDto;
        Collections.sort(mappingsDocDtos);
        ApiDocPublishing.mappingsDocDtos = mappingsDocDtos;
    }

    public static ApiDocDto getApiDocDto(){
        return ApiDocPublishing.apiDocDto;
    }

    public static List<RequestMappingsDocDto> getMappingsDocDtos(){
        return ApiDocPublishing.mappingsDocDtos;
    }
}
