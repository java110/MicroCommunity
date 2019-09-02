package com.java110.code.relationship;

import com.alibaba.fastjson.JSONObject;
import com.java110.code.BaseGenerator;
import com.java110.code.web.GeneratorStart;

/**
 * @ClassName GeneratorRelationShipStart
 * @Description  生成关系流程页面 可以参考 登录dev/admin 打开服务绑定 https://demo.java110.com/flow/serviceBindingFlow
 * @Author wuxw
 * @Date 2019/7/27 11:48
 * @Version 1.0
 * add by wuxw 2019/7/27
 **/
public class GeneratorRelationShipStart extends BaseGenerator {

    public static void main(String[] args) {

        StringBuffer sb = readFile(GeneratorStart.class.getResource("/relationship/template_1.json").getFile());

        JSONObject data = JSONObject.parseObject(sb.toString());

        GeneratorBindingComponent generatorBindingComponent = new GeneratorBindingComponent();
        generatorBindingComponent.generator(data);

        GeneratorFlow generatorFlow = new GeneratorFlow();
        generatorFlow.generator(data);
    }
}
