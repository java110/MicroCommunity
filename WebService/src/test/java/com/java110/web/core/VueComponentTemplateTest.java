package com.java110.web.core;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class VueComponentTemplateTest {

    @Test
    public void handleResource() {
    }

    @Test
    public void sp(){
        String path = "components/privilege/privilege.html";
        System.out.println(File.separator);
        System.out.println( path.substring(path.lastIndexOf(File.separator) + 1, path.length()));
    }
}