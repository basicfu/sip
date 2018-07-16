package com.basicfu.sip.core.common

import com.basicfu.sip.core.annotation.EnableSipCore
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
        val attrs = metadata.getAnnotationAttributes(EnableSipCore::class.java.name, true)
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
                        registry.registerBeanDefinition(it.simpleName, builder.beanDefinition)
                    }
                }
            } else if (enable.isNotEmpty()) {
                enable.forEach {
                    it.value.forEach {
                        val builder = BeanDefinitionBuilder.genericBeanDefinition(it)
                        registry.registerBeanDefinition(it.simpleName, builder.beanDefinition)
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
                        registry.registerBeanDefinition(it.simpleName, builder.beanDefinition)
                    }
                }
            }
        }
    }
}
