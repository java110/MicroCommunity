package com.java110.web.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class VueComponentElementTest {

    @Test
    public void getMarkupSubstitutes() {
    }

    @Test
    public void testSubString(){
        String js = "vc.extends({\n" +
                "        propTypes: {\n" +
                "            @OpenAddUnitModelName:string\n" +
                "        },\n" +
                "        data:{\n" +
                "            floorInfo:{\n" +
                "                floorId:\"\",\n" +
                "                floorName:\"\",\n" +
                "                floorNum:\"\"\n" +
                "            }\n" +
                "        },";

        //解析propTypes信息
        String tmpProTypes = js.substring(js.indexOf("propTypes"));
        tmpProTypes = tmpProTypes.substring(tmpProTypes.indexOf("{")+1, tmpProTypes.indexOf("}")).trim();

        System.out.println(tmpProTypes);

    }
}