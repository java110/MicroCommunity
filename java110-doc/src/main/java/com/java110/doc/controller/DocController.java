package com.java110.doc.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController

@RequestMapping(value = "/doc")
public class DocController {

    @RequestMapping(path = "/api", method = RequestMethod.GET)
    public ResponseEntity<String> html(
            HttpServletRequest request){
        return new ResponseEntity<>("<html></html>", HttpStatus.OK);
    }

    @RequestMapping(path = "/api/{resource}/{serviceCode}", method = RequestMethod.GET)
    public ResponseEntity<String> api( @PathVariable String resource,
                                       @PathVariable String serviceCode,
                                       HttpServletRequest request){
        return null;
    }
}
