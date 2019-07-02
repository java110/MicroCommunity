package com.java110.code.web;

import com.alibaba.fastjson.JSONObject;
import com.java110.code.BaseGenerator;

public class GeneratorStart extends BaseGenerator {

    public static void main(String[] args) {
        //加载配置
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/template_1.json").getFile());

        JSONObject data = JSONObject.parseObject(sb.toString());

        GeneratorManagerComponent managerComponent = new GeneratorManagerComponent();
        managerComponent.generator(data);


        //添加功能开发完成

        GeneratorAddComponent addComponent = new GeneratorAddComponent();
        addComponent.generator(data);
    }
}
