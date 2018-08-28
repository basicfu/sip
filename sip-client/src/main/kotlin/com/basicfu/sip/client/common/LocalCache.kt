//package com.basicfu.sip.client.common
//
//import java.util.*
//import java.util.concurrent.ConcurrentHashMap
//import java.util.concurrent.ScheduledThreadPoolExecutor
//import java.util.concurrent.TimeUnit
//
//object LocalCache {
//    private val TIMER = Timer()
//    private val map = ConcurrentHashMap<String, Any>()
//    internal var mutex = Any()
//    fun setCache(key: String, ce: Any, validityTime: Int) {
//        val executorService = ScheduledThreadPoolExecutor(1)
//        executorService.scheduleAtFixedRate({
//            //do something
//        }, 0, 2, TimeUnit.SECONDS)
//
//    }
//}
