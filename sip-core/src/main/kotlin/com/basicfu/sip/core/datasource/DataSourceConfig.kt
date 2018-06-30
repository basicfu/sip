package com.basicfu.sip.core.datasource

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder
import org.mybatis.spring.SqlSessionFactoryBean
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.interceptor.TransactionInterceptor
import java.util.*
import javax.sql.DataSource

@Configuration
class DataSourceConfig {

    @Primary
    @Bean
//    @ConfigurationProperties("spring.datasource.druid.master")
    fun masterDataSource(): DataSource {
        return DruidDataSourceBuilder.create().build()
    }

    @Bean
    fun dynamicDataSource(): DataSource {
        val targetDataSources = HashMap<Any, Any>()
        val masterDataSource = masterDataSource()
        targetDataSources.put(DataSourceContextHolder.DataSourceType.MASTER.name + "0", masterDataSource)
        val dataSource = DynamicDataSource(1, 2)
        dataSource.setTargetDataSources(targetDataSources)
        dataSource.setDefaultTargetDataSource(masterDataSource)
        return dataSource
    }

    @Bean
    fun sqlSessionFactoryBean(dataSource: DataSource): SqlSessionFactoryBean {
        val sqlSessionFactoryBean = SqlSessionFactoryBean()
        sqlSessionFactoryBean.setDataSource(dataSource)
        sqlSessionFactoryBean.setMapperLocations(PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/*.xml"))

        return sqlSessionFactoryBean
    }

    @Bean
    internal fun transactionManager(dataSource: DataSource): DataSourceTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }

    @Bean
    fun txAdvice(transactionManager: DataSourceTransactionManager): TransactionInterceptor {
        val properties = Properties()
        properties.setProperty("insert*", "PROPAGATION_REQUIRED,-Exception")
        properties.setProperty("update*", "PROPAGATION_REQUIRED,-Exception")
        properties.setProperty("delete*", "PROPAGATION_REQUIRED,-Exception")
        return TransactionInterceptor(transactionManager, properties)
    }

    @Bean
    fun txProxy(): BeanNameAutoProxyCreator {
        val creator = BeanNameAutoProxyCreator()
        creator.setInterceptorNames("txAdvice")
        creator.setBeanNames("*Service")
        creator.isProxyTargetClass = true
        return creator
    }
}
