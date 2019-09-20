package com.basicfu.sip.client.common

import com.basicfu.sip.client.controller.SipController
import com.basicfu.sip.client.util.ApiUtil
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata

/**
 * @author basicfu
 * @date 2018/7/16
 */
@Suppress("UNCHECKED_CAST")
class BeanRegistrar : ImportBeanDefinitionRegistrar {
    override fun registerBeanDefinitions(metadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        //register feign config set request heads
        registry.registerBeanDefinition(
            ApiUtil::class.java.simpleName,
            BeanDefinitionBuilder.genericBeanDefinition(ApiUtil::class.java).beanDefinition
        )
        registry.registerBeanDefinition(
            SipController::class.java.simpleName,
            BeanDefinitionBuilder.genericBeanDefinition(SipController::class.java).beanDefinition
        )
    }
}
