package com.java110.front.core;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import java.util.LinkedHashSet;
import java.util.Set;


/**
 * <vc:create name="xxxx"></vc:create>
 *
 * 标签解析
 */
@Component
public class VcCreateDialect extends AbstractProcessorDialect {

    private final static String PREFIX = "vc";

    private final static String NAME = "create";


    protected VcCreateDialect() {
        super(NAME, PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        LinkedHashSet<IProcessor> processors = new LinkedHashSet<IProcessor>();
        //	添加自定义标签处理器，可添加多个
        processors.add(new VcCreateProcessor(dialectPrefix));
        return processors;
    }
}
