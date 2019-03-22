package com.java110.web.core;

import com.alibaba.fastjson.JSONObject;
import com.java110.web.smo.impl.LoginServiceSMOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.*;
import org.thymeleaf.processor.element.AbstractMarkupSubstitutionElementProcessor;
import org.thymeleaf.util.DOMUtils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 组件 自定义标签功能类
 * Created by wuxw on 2019/3/18.
 */
public class VueComponentElement extends AbstractMarkupSubstitutionElementProcessor {

    private final static Logger logger = LoggerFactory.getLogger(VueComponentElement.class);


    protected VueComponentElement(String elementName) {
        super(elementName);
    }

    @Override
    protected List<Node> getMarkupSubstitutes(Arguments arguments, Element element) {

        //logger.debug("arg:{},element:{}", JSONObject.toJSONString(arguments),element.getAttributeValue("name"));
        List<Node> nodes = new ArrayList<>();
        //获取模板名称
        String componentName = element.getAttributeValue("name");
        logger.debug("正在解析组件{}",componentName);
        String html = VueComponentTemplate.findTemplateByComponentCode(componentName+"."+VueComponentTemplate.COMPONENT_HTML);
        if(html == null){
            throw new RuntimeException("在缓存中未找到组件【"+componentName+"】");
        }
        //List<Node> tmpNodes = DOMUtils.getHtml5DOMFor(new StringReader(html)).getChildren();
        List<Node> tmpNodes = DOMUtils.getLegacyHTML5DOMFor(new StringReader(html)).getChildren();
        for(Node tmpNode : tmpNodes) {
            nodes.add(tmpNode);
        }
        //css
        String css = VueComponentTemplate.findTemplateByComponentCode(componentName+"."+VueComponentTemplate.COMPONENT_CSS);
        if(css != null){
            css = "<style type=\"text/css\">" + css +"</style>";
            Node nodeCss = new Macro(css);
            nodes.add(nodeCss);
        }

        //js
        String js = VueComponentTemplate.findTemplateByComponentCode(componentName+"."+VueComponentTemplate.COMPONENT_JS);
        if(js != null){
            js = "<script type=\"text/javascript\">//<![CDATA[ \n" + js +"//]]>\n</script>";
            Node nodeJs = new Macro(js);
            nodes.add(nodeJs);
        }


        return nodes;
    }

    @Override
    public int getPrecedence() {
        return 1000;
    }
}
