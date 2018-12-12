package com.basicfu.sip.client.common

import com.netflix.hystrix.HystrixThreadPoolKey
import com.netflix.hystrix.HystrixThreadPoolProperties
import com.netflix.hystrix.strategy.HystrixPlugins
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle
import com.netflix.hystrix.strategy.properties.HystrixProperty
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Callable
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @author basicfu
 * @date 2018/7/18
 * 自定义Feign的隔离策略
 * 在转发Feign的请求头的时候，如果开启了Hystrix，Hystrix的默认隔离策略是Thread(线程隔离策略)，因此转发拦截器内是无法获取到请求的请求头信息的，可以修改默认隔离策略为信号量模式：hystrix.command.default.execution.isolation.strategy=SEMAPHORE，这样的话转发线程和请求线程实际上是一个线程，这并不是最好的解决方法，信号量模式也不是官方最为推荐的隔离策略；
 * 另一个解决方法就是自定义Hystrix的隔离策略，思路是将现有的并发策略作为新并发策略的成员变量,在新并发策略中，返回现有并发策略的线程池、Queue；将策略加到Spring容器即可；
 */
@Suppress("LeakingThis")
@Component
class FeignHystrixConcurrencyStrategyIntellif : HystrixConcurrencyStrategy() {
    private var delegate: HystrixConcurrencyStrategy? = null

    init {
        this.delegate = HystrixPlugins.getInstance().concurrencyStrategy
        if (this.delegate !is FeignHystrixConcurrencyStrategyIntellif) {
            val commandExecutionHook = HystrixPlugins.getInstance().commandExecutionHook
            val eventNotifier = HystrixPlugins.getInstance().eventNotifier
            val metricsPublisher = HystrixPlugins.getInstance().metricsPublisher
            val propertiesStrategy = HystrixPlugins.getInstance().propertiesStrategy
            HystrixPlugins.reset()
            HystrixPlugins.getInstance().registerConcurrencyStrategy(this)
            HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook)
            HystrixPlugins.getInstance().registerEventNotifier(eventNotifier)
            HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher)
            HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy)
        }
    }

    override fun <T> wrapCallable(callable: Callable<T>): Callable<T> {
        val requestAttributes = RequestContextHolder.getRequestAttributes()
        return WrappedCallable(callable, requestAttributes)
    }

    override fun getThreadPool(
        threadPoolKey: HystrixThreadPoolKey,
        corePoolSize: HystrixProperty<Int>, maximumPoolSize: HystrixProperty<Int>,
        keepAliveTime: HystrixProperty<Int>, unit: TimeUnit, workQueue: BlockingQueue<Runnable>
    ): ThreadPoolExecutor {
        return this.delegate!!.getThreadPool(
            threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime,
            unit, workQueue
        )
    }

    override fun getThreadPool(
        threadPoolKey: HystrixThreadPoolKey,
        threadPoolProperties: HystrixThreadPoolProperties
    ): ThreadPoolExecutor {
        return this.delegate!!.getThreadPool(threadPoolKey, threadPoolProperties)
    }

    override fun getBlockingQueue(maxQueueSize: Int): BlockingQueue<Runnable> {
        return this.delegate!!.getBlockingQueue(maxQueueSize)
    }

    override fun <T> getRequestVariable(rv: HystrixRequestVariableLifecycle<T>): HystrixRequestVariable<T> {
        return this.delegate!!.getRequestVariable(rv)
    }

    internal class WrappedCallable<T>(
        private val target: Callable<T>,
        private val requestAttributes: RequestAttributes?
    ) : Callable<T> {
        @Throws(Exception::class)
        override fun call(): T {
            try {
                RequestContextHolder.setRequestAttributes(requestAttributes)
                return target.call()
            } finally {
                RequestContextHolder.resetRequestAttributes()
            }
        }
    }
}
