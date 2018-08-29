package com.basicfu.sip.core.common.datasource

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder
import com.basicfu.sip.core.common.interceptor.SqlInterceptor
import org.mybatis.spring.SqlSessionFactoryBean
import org.springframework.aop.Advisor
import org.springframework.aop.aspectj.AspectJExpressionPointcut
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.stereotype.Component
import org.springframework.transaction.interceptor.TransactionInterceptor
import java.util.*
import javax.sql.DataSource

@Import(SqlInterceptor::class)
@Component
class DataSourceConfig {
    @Autowired
    lateinit var sqlStatsInterceptor: SqlInterceptor

    /**
     * @Component形式注入beanName不同会默认覆盖dataSource，而手动注入的beanName不会覆盖默认注入的dataSource所以导致出现2个数据库连接池
     */
    @Primary
    @Bean
    fun dataSource(): DataSource {
        return DruidDataSourceBuilder.create().build()
    }

    @Bean
    fun dynamicDataSource(): DataSource {
        val targetDataSources = HashMap<Any, Any>()
        val masterDataSource = dataSource()
        targetDataSources[DataSourceContextHolder.DataSourceType.MASTER.name + "0"] = masterDataSource
        val dataSource = DynamicDataSource(1, 2)
        dataSource.setTargetDataSources(targetDataSources)
        dataSource.setDefaultTargetDataSource(masterDataSource)
        return dataSource
    }

    @Bean
    fun sqlSessionFactory(dataSource: DataSource): SqlSessionFactoryBean {
        val sqlSessionFactoryBean = SqlSessionFactoryBean()
        sqlSessionFactoryBean.setDataSource(dataSource)
        sqlSessionFactoryBean.setMapperLocations(PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/*.xml"))
        sqlSessionFactoryBean.setPlugins(arrayOf(sqlStatsInterceptor))
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
        properties.setProperty("import*", "PROPAGATION_REQUIRED,-Exception")
        return TransactionInterceptor(transactionManager, properties)
    }

    //    @Bean
//    fun txProxy(): BeanNameAutoProxyCreator {
//        val creator = BeanNameAutoProxyCreator()
//        creator.setInterceptorNames("txAdvice")
//        creator.setBeanNames("*Service")
//        creator.isProxyTargetClass = true
//        return creator
//    }
    @Bean
    fun txAdviceAdvisor(txAdvice: TransactionInterceptor): Advisor {
        val pointcut = AspectJExpressionPointcut()
        pointcut.expression =
                "execution(* com.basicfu.sip.*.service.*Service.*(..))&&!execution(* com.basicfu.sip.core.service.BaseService.*(..))"
        return DefaultPointcutAdvisor(pointcut, txAdvice)
    }
}
