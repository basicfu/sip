package com.basicfu.sip.core.common

import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class FeignBeanFactoryPostProcessor : BeanFactoryPostProcessor {

    @Throws(BeansException::class)
    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        if (containsBeanDefinition(beanFactory, "feignContext", "eurekaAutoServiceRegistration")) {
            val bd = beanFactory.getBeanDefinition("feignContext")
            bd.setDependsOn("eurekaAutoServiceRegistration")
        }
    }

    private fun containsBeanDefinition(beanFactory: ConfigurableListableBeanFactory, vararg beans: String): Boolean {
        return Arrays.stream(beans).allMatch { b -> beanFactory.containsBeanDefinition(b) }
    }
}
