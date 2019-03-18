package com.java110.web.core;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义 vue组件 标签
 * Created by wuxw on 2019/3/18.
 */
@Component
public class VueComponent extends AbstractDialect {
    @Override
    public String getPrefix() {
        return "vc";
    }

    @Override
    public Set<IProcessor> getProcessors() {
        final Set<IProcessor>processors = new HashSet<>();
        processors.add(new VueComponentElement("create"));
        return processors;
    }
}
