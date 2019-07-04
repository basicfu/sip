package com.basicfu.sip.client.common

import feign.Client
import feign.Request
import feign.Response
import org.springframework.stereotype.Component

/**
 * @author basicfu
 * @date 2019/3/21
 */
@Component
class FeignClient : Client {
    override fun execute(request: Request?, options: Request.Options?): Response {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
