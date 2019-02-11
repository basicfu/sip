package com.basicfu.sip.getway.common.datasource

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder
import org.mybatis.spring.SqlSessionFactoryBean
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import java.util.*
import javax.sql.DataSource


@Configuration
class DataSourceConfig {
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.druid.base")
    fun baseDataSource(): DataSource {
        return DruidDataSourceBuilder.create().build()
    }

    @Bean
    fun dataSource(
        @Qualifier("baseDataSource") baseDataSource: DataSource
    ): DataSource {
        val targetDataSources = HashMap<Any, Any>()
        targetDataSources[DataSourceContextHolder.DataSourceType.BASE.name] = baseDataSource
        val dataSource = DynamicDataSource()
        dataSource.setTargetDataSources(targetDataSources)
        dataSource.setDefaultTargetDataSource(baseDataSource)
        return dataSource
    }

    @Bean
    fun sqlSessionFactory(@Qualifier("dataSource") dataSource: DataSource): SqlSessionFactoryBean {
        val sqlSessionFactoryBean = SqlSessionFactoryBean()
        sqlSessionFactoryBean.setDataSource(dataSource)
        sqlSessionFactoryBean.setMapperLocations(PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/*.xml"))
        return sqlSessionFactoryBean
    }

    @Bean
    internal fun transactionManager(dataSource: DataSource): DataSourceTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }
}
