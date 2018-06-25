package com.basicfu.sip.core;

import com.basicfu.sip.core.annotation.EnableRedis;
import com.basicfu.sip.core.util.RedisUtil;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

public class BeanRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Map defaultAttrs = annotationMetadata.getAnnotationAttributes(EnableRedis.class.getName(), true);
        if (defaultAttrs != null) {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(RedisUtil.class);
            beanDefinition.setSynthetic(true);
            beanDefinitionRegistry.registerBeanDefinition("redisUtil", beanDefinition);
        }
    }
}