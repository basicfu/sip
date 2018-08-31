package com.basicfu.sip.base

import com.basicfu.sip.core.common.mapper.CustomMapper
import org.junit.ClassRule
import org.junit.Rule
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.EnvironmentTestUtils
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.GenericContainer


@RunWith(SpringRunner::class)
@SpringBootTest
@ContextConfiguration(initializers = [(BaseTests.Initializer::class)])
abstract class BaseTests<M : CustomMapper<T>,T> {
    @Autowired
    lateinit var mapper: M
    @get:Rule
    var exception = ExpectedException.none()!!

    class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)
    companion object {
        @get:ClassRule
        val redis = KGenericContainer("redis:4.0.11").withExposedPorts(6379)!!
    }

    class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            redis.start()
            EnvironmentTestUtils.addEnvironment(
                configurableApplicationContext.environment,
                "spring.redis.host=${redis.containerIpAddress}",
                "spring.redis.port=${redis.getMappedPort(6379)}"
            )
        }
    }
}
