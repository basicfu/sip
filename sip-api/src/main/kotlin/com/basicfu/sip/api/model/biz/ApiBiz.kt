package com.basicfu.sip.api.model.biz

class ApiBiz {
    class Path {
        var key: String? = null
        var value: String? = null
        var description: String? = null
    }

    class Query {
        var key: String? = null
        var value: String? = null
        var description: String? = null
        var enabled: Boolean? = null
    }

    class Header {
        var key: String? = null
        var value: String? = null
        var description: String? = null
        var enabled: Boolean? = null
    }
}
