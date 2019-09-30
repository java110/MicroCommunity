package com.java110.core.annotation;

import com.java110.utils.util.Assert;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义侦听扫描
 * Created by wuxw on 2018/7/2.
 */
public class Java110ListenerDiscoveryRegistrar implements ImportBeanDefinitionRegistrar,ResourceLoaderAware, BeanClassLoaderAware {

    private ResourceLoader resourceLoader;

    private ClassLoader classLoader;

    public Java110ListenerDiscoveryRegistrar(){

    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        try {
            registerListener(importingClassMetadata,registry);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 注册侦听
     * @param metadata
     * @param registry
     */
    public void registerListener(AnnotationMetadata metadata,
                                 BeanDefinitionRegistry registry) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        Set<String> basePackages;
        Map<String, Object> attrs = metadata
                .getAnnotationAttributes(Java110ListenerDiscovery.class.getName());

        Object listenerPublishClassObj =  attrs.get("listenerPublishClass");

        Assert.notNull(listenerPublishClassObj,"Java110ListenerDiscovery 没有配置 listenerPublishClass 属性");

        Class<?> listenerPublishClass = (Class<?>) listenerPublishClassObj;

        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(
                Java110Listener.class);

        scanner.addIncludeFilter(annotationTypeFilter);
        basePackages = getBasePackages(metadata);

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner
                    .findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    // verify annotated class is an interface
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();


                    Map<String, Object> attributes = annotationMetadata
                            .getAnnotationAttributes(
                                    Java110Listener.class.getCanonicalName());

                    String beanName = getListenerName(attributes,beanDefinition);

                    /*BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
                    BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);*/
                    Method method = listenerPublishClass.getMethod("addListener",String.class);
                    method.invoke(null,beanName);
                }
            }
        }
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false) {

            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().isIndependent()) {
                    // TODO until SPR-11711 will be resolved
                    if (beanDefinition.getMetadata().isInterface()
                            && beanDefinition.getMetadata()
                            .getInterfaceNames().length == 1
                            && Annotation.class.getName().equals(beanDefinition
                            .getMetadata().getInterfaceNames()[0])) {
                        try {
                            Class<?> target = ClassUtils.forName(
                                    beanDefinition.getMetadata().getClassName(),
                                    Java110ListenerDiscoveryRegistrar.this.classLoader);
                            return !target.isAnnotation();
                        }
                        catch (Exception ex) {
                            this.logger.error(
                                    "Could not load target class: "
                                            + beanDefinition.getMetadata().getClassName(),
                                    ex);

                        }
                    }
                    return true;
                }
                return false;

            }
        };
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(Java110ListenerDiscovery.class.getCanonicalName());

        Set<String> basePackages = new HashSet<String>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        if (basePackages.isEmpty()) {
            basePackages.add(
                    ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }


    /**
     * 获取名称
     * @param listeners
     * @param beanDefinition
     * @return
     */
    private String getListenerName(Map<String, Object> listeners,AnnotatedBeanDefinition beanDefinition) {
        if (listeners == null) {
            String shortClassName = ClassUtils.getShortName(beanDefinition.getBeanClassName());
            return Introspector.decapitalize(shortClassName);
        }
        String value = (String) listeners.get("value");
        if (!StringUtils.hasText(value)) {
            value = (String) listeners.get("name");
        }
        if (StringUtils.hasText(value)) {
            return value;
        }

        String shortClassName = ClassUtils.getShortName(beanDefinition.getBeanClassName());
        value = Introspector.decapitalize(shortClassName);
        return value;
    }


}
