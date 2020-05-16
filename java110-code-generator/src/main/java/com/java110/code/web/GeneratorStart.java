package com.java110.code.web;

import com.alibaba.fastjson.JSONObject;
import com.java110.code.back.BaseGenerator;

public class GeneratorStart extends BaseGenerator {

    public static void main(String[] args) throws Exception {
        //加载配置
        StringBuffer sb = readFile(GeneratorStart.class.getResource("/web/template_company.json").getFile());

        JSONObject data = JSONObject.parseObject(sb.toString());


        GeneratorFlow flow = new GeneratorFlow();
//        flow.generator(data);     //生成flow页面


        GeneratorManagerComponent managerComponent = new GeneratorManagerComponent();
        managerComponent.generator(data);


        //添加功能开发完成
        GeneratorAddComponent addComponent = new GeneratorAddComponent();
        addComponent.generator(data);

        //编辑功能开发完成
        GeneratorEditComponent editComponent = new GeneratorEditComponent();
        editComponent.generator(data);

        //删除功能开发完成
        GeneratorDeleteComponent deleteComponent = new GeneratorDeleteComponent();
        deleteComponent.generator(data);

        //生成选择功能组件
        GeneratorChooseComponent generatorChooseComponent = new GeneratorChooseComponent();
        generatorChooseComponent.generator(data);

        GeneratorViewComponent generatorViewComponent = new GeneratorViewComponent();
        generatorViewComponent.generator(data);
    }
}
