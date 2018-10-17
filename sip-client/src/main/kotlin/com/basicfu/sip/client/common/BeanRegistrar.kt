package com.basicfu.sip.client.common

import com.basicfu.sip.client.annotation.EnableSipClient
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
    private val beanNamePrefix="SIP_"

    override fun registerBeanDefinitions(metadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        //register feign config set request heads
        val fc = BeanDefinitionBuilder.genericBeanDefinition(FeignConfiguration::class.java)
        val fh = BeanDefinitionBuilder.genericBeanDefinition(FeignHystrixConcurrencyStrategyIntellif::class.java)
        registry.registerBeanDefinition(FeignConfiguration::class.java.simpleName, fc.beanDefinition)
        registry.registerBeanDefinition(FeignHystrixConcurrencyStrategyIntellif::class.java.simpleName, fh.beanDefinition)

        val attrs = metadata.getAnnotationAttributes(EnableSipClient::class.java.name, true)
        if (attrs != null) {
            val enable = attrs["enable"] as Array<Function>
            val disable = attrs["disable"] as Array<Function>
            if (enable.isNotEmpty() && disable.isNotEmpty()) {
                throw RuntimeException("enable和disable不能同时存在")
            }
            if (enable.isEmpty() && disable.isEmpty()) {
                val values = Function.values().toList()
                values.forEach {
                    it.value.forEach {
                        val builder = BeanDefinitionBuilder.genericBeanDefinition(it)
                        registry.registerBeanDefinition(beanNamePrefix+it.simpleName, builder.beanDefinition)
                    }
                }
            } else if (enable.isNotEmpty()) {
                enable.forEach {
                    it.value.forEach {
                        val builder = BeanDefinitionBuilder.genericBeanDefinition(it)
                        registry.registerBeanDefinition(beanNamePrefix+it.simpleName, builder.beanDefinition)
                    }
                }
            } else if (disable.isNotEmpty()) {
                val values = Function.values().toMutableList()
                disable.forEach {
                    values.remove(it)
                }
                values.forEach {
                    it.value.forEach {
                        val builder = BeanDefinitionBuilder.genericBeanDefinition(it)
                        registry.registerBeanDefinition(beanNamePrefix+it.simpleName, builder.beanDefinition)
                    }
                }
            }
        }
    }
}
