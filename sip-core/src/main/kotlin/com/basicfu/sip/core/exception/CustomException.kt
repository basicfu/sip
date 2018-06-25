package com.basicfu.sip.core.exception

class CustomException : RuntimeException {
    var code = 0
    var msg = ""
    var data: Any? = null

    constructor() {}

    constructor(any: Any) {
        try {
            deal(any)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    constructor(any: Any, data: String) {
        try {
            deal(any)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        this.data = data
    }

    constructor(msg: String, data: Any) {
        this.msg = msg
        this.data = data
        this.code = 999
    }

    constructor(msg: String, data: String) {
        this.msg = msg
        this.data = data
        this.code = 999
    }

    constructor(msg: String) {
        this.msg = msg
        this.code = 999
    }

    constructor(enum: Any,data:Any) {
        deal(enum)
        this.data=data
    }

    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    private fun deal(any: Any) {
        val java = any.javaClass
        val value = java.getDeclaredField("value")
        value.isAccessible = true
        this.code = value.getInt(any)
        val msg = java.getDeclaredField("msg")
        msg.isAccessible = true
        this.msg = msg.get(any).toString()

    }
}
