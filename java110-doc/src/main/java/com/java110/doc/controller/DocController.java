package com.java110.doc.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.doc.entity.ApiDocDto;
import com.java110.doc.entity.CmdDocDto;
import com.java110.doc.entity.RequestMappingsDocDto;
import com.java110.doc.registrar.ApiDocCmdPublishing;
import com.java110.doc.registrar.ApiDocPublishing;
import com.java110.utils.factory.ApplicationContextFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(path = "/api/{resource}/{serviceCode}", method = RequestMethod.GET)
    public ResponseEntity<String> api(@PathVariable String resource,
                                      @PathVariable String serviceCode,
                                      HttpServletRequest request) {
        return null;
    }
}
