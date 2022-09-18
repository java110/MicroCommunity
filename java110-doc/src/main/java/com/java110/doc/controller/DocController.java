package com.java110.doc.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.doc.annotation.*;
import com.java110.doc.entity.ApiDocDto;
import com.java110.doc.entity.CmdDocDto;
import com.java110.doc.entity.RequestMappingsDocDto;
import com.java110.doc.registrar.ApiDocCmdPublishing;
import com.java110.doc.registrar.ApiDocPublishing;
import com.java110.utils.factory.ApplicationContextFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.List;

@RestController
@RequestMapping(value = "/doc")
public class DocController {


    @RequestMapping(path = "/api", method = RequestMethod.GET)
    public ResponseEntity<String> html(
            HttpServletRequest request) {


        ApiDocDto apiDocDto = ApiDocPublishing.getApiDocDto();

        List<RequestMappingsDocDto> mappingsDocDtos = ApiDocPublishing.getMappingsDocDtos();

        JSONObject param = new JSONObject();
        param.put("api", apiDocDto);
        param.put("mappings", mappingsDocDtos);
        return new ResponseEntity<>(param.toString(), HttpStatus.OK);
    }

    @RequestMapping(path = "/api/page", method = RequestMethod.GET)
    public ResponseEntity<String> pages(
            @RequestParam("name") String name,
            @RequestParam("resource") String resource,
            HttpServletRequest request) {

        List<RequestMappingsDocDto> mappingsDocDtos = ApiDocPublishing.getMappingsDocDtos();

        RequestMappingsDocDto newMappingsDocDto = null;
        for (RequestMappingsDocDto mappingsDocDto : mappingsDocDtos) {
            if (mappingsDocDto.getName().equals(name)) {
                newMappingsDocDto = mappingsDocDto;
            }
        }

        if (newMappingsDocDto == null) {
            return new ResponseEntity<>("[]", HttpStatus.OK);
        }

        RestTemplate restTemplate = ApplicationContextFactory.getBean("restTemplate", RestTemplate.class);
        if ("boot".equals(newMappingsDocDto.getStartWay())) {
            restTemplate = ApplicationContextFactory.getBean("outRestTemplate", RestTemplate.class);
        }
        ResponseEntity<String> responseEntity = null;
        HttpEntity<String> httpEntity = new HttpEntity<String>("", new HttpHeaders());
        try {
            responseEntity = restTemplate.exchange(newMappingsDocDto.getUrl()+"/doc/api/"+resource, HttpMethod.GET, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @RequestMapping(path = "/api/{resource}", method = RequestMethod.GET)
    public ResponseEntity<String> resourcePages(
            @PathVariable String resource,
            HttpServletRequest request) {

        List<CmdDocDto> cmdDocDtos = ApiDocCmdPublishing.getCmdDocs(resource);

        return new ResponseEntity<>(JSONArray.toJSONString(cmdDocDtos), HttpStatus.OK);
    }


    @RequestMapping(path = "/api/pageContent", method = RequestMethod.GET)
    public ResponseEntity<String> pageContent(
            @RequestParam("name") String name,
            @RequestParam("serviceCode") String serviceCode,
            @RequestParam("resource") String resource,
            HttpServletRequest request) {

        List<RequestMappingsDocDto> mappingsDocDtos = ApiDocPublishing.getMappingsDocDtos();

        RequestMappingsDocDto newMappingsDocDto = null;
        for (RequestMappingsDocDto mappingsDocDto : mappingsDocDtos) {
            if (mappingsDocDto.getName().equals(name)) {
                newMappingsDocDto = mappingsDocDto;
            }
        }

        if (newMappingsDocDto == null) {
            return new ResponseEntity<>("{}", HttpStatus.OK);
        }

        RestTemplate restTemplate = ApplicationContextFactory.getBean("restTemplate", RestTemplate.class);
        if ("boot".equals(newMappingsDocDto.getStartWay())) {
            restTemplate = ApplicationContextFactory.getBean("outRestTemplate", RestTemplate.class);
        }
        ResponseEntity<String> responseEntity = null;
        HttpEntity<String> httpEntity = new HttpEntity<String>("", new HttpHeaders());
        try {
            responseEntity = restTemplate.exchange(newMappingsDocDto.getUrl()+"/doc/api/"+resource+"/"+serviceCode, HttpMethod.GET, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @RequestMapping(path = "/api/{resource}/{serviceCode}", method = RequestMethod.GET)
    public ResponseEntity<String> api(@PathVariable String resource,
                                      @PathVariable String serviceCode,
                                      HttpServletRequest request) {
        CmdDocDto cmdDocDto = ApiDocCmdPublishing.getCmdDocs(resource,serviceCode);

        if(cmdDocDto == null){
            return new ResponseEntity<>("{}", HttpStatus.OK);
        }

        JSONObject param = JSONObject.parseObject(JSONObject.toJSONString(cmdDocDto));


        Class<?> clazz = null;
        try {
            clazz = Class.forName(cmdDocDto.getCmdClass());
        } catch (ClassNotFoundException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.OK);
        }


        doJava110ParamsDoc(clazz,param);


        doJava110ResponseDoc(param, clazz);

        doJava110ExampleDoc(param, clazz);

        return new ResponseEntity<>(param.toJSONString(), HttpStatus.OK);
    }

    private void doJava110ExampleDoc(JSONObject param, Class clazz) {
        Java110ExampleDoc java110ExampleDoc = AnnotationUtils.findAnnotation(clazz,Java110ExampleDoc.class);

        if(java110ExampleDoc == null){
            return ;
        }

        param.put("reqBody",java110ExampleDoc.reqBody());
        param.put("resBody",java110ExampleDoc.resBody());
    }

    private void doJava110ResponseDoc(JSONObject param, Class clazz) {
        Java110ResponseDoc java110ResponseDoc = AnnotationUtils.findAnnotation(clazz,Java110ResponseDoc.class);

        if(java110ResponseDoc == null){
            return ;
        }

        Java110ParamDoc[] java110ParamDocs = java110ResponseDoc.params();

        JSONArray params = new JSONArray();
        JSONObject p = null;
        for(Java110ParamDoc java110ParamDoc : java110ParamDocs){
            p = new JSONObject();
            p.put("name",java110ParamDoc.name());
            p.put("defaultValue",java110ParamDoc.defaultValue());
            p.put("remark",java110ParamDoc.remark());
            p.put("type",java110ParamDoc.type());
            p.put("length",java110ParamDoc.length());
            p.put("parentNodeName",java110ParamDoc.parentNodeName());
            params.add(p);
        }
        param.put("resParam",params);
    }

    private void doJava110ParamsDoc(Class clazz, JSONObject param) {

        Java110ParamsDoc java110ParamsDoc = AnnotationUtils.findAnnotation(clazz,Java110ParamsDoc.class);

        if(java110ParamsDoc == null){
            return ;
        }

        Java110HeaderDoc[] java110HeaderDocs = java110ParamsDoc.headers();

        JSONArray headers = new JSONArray();
        JSONObject header = null;
        for(Java110HeaderDoc java110HeaderDoc : java110HeaderDocs){
            header = new JSONObject();
            header.put("name",java110HeaderDoc.name());
            header.put("defaultValue",java110HeaderDoc.defaultValue());
            header.put("description",java110HeaderDoc.description());
            headers.add(header);
        }

        param.put("headers",headers);


        Java110ParamDoc[] java110ParamDocs = java110ParamsDoc.params();

        JSONArray params = new JSONArray();
        JSONObject p = null;
        for(Java110ParamDoc java110ParamDoc : java110ParamDocs){
            p = new JSONObject();
            p.put("name",java110ParamDoc.name());
            p.put("defaultValue",java110ParamDoc.defaultValue());
            p.put("remark",java110ParamDoc.remark());
            p.put("type",java110ParamDoc.type());
            p.put("length",java110ParamDoc.length());
            p.put("parentNodeName",java110ParamDoc.parentNodeName());
            params.add(p);
        }
        param.put("reqParam",params);
    }
}
