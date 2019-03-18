package com.java110.web.core;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.*;
import org.thymeleaf.processor.element.AbstractMarkupSubstitutionElementProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 组件 自定义标签功能类
 * Created by wuxw on 2019/3/18.
 */
public class VueComponentElement extends AbstractMarkupSubstitutionElementProcessor {

    protected VueComponentElement(String elementName) {
        super(elementName);
    }

    @Override
    protected List<Node> getMarkupSubstitutes(Arguments arguments, Element element) {
        List<Node> nodes = new ArrayList<>();
        //获取模板名称
        String componentName = element.getAttributeValue("name");
        String html = VueComponentTemplate.findTemplateByComponentCode(componentName+"."+VueComponentTemplate.COMPONENT_HTML);
        if(html == null){
            throw new RuntimeException("在缓存中未找到组件【"+componentName+"】");
        }
        Node nodeHtml = new Macro(html);
        nodes.add(nodeHtml);
        //css
        String css = VueComponentTemplate.findTemplateByComponentCode(componentName+"."+VueComponentTemplate.COMPONENT_CSS);
        if(css != null){
            css = "<style type=\"text/css\">" + css +"</style>";
            Node nodeCss = new Macro(css);
            nodes.add(nodeCss);
        }

        //css
        String js = VueComponentTemplate.findTemplateByComponentCode(componentName+"."+VueComponentTemplate.COMPONENT_JS);
        if(js != null){
            js = "<script type=\"text/javascript\">" + js +"</script>";
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
