package com.basicfu.sip.base.model.vo

import com.basicfu.sip.core.annotation.Insert
import com.basicfu.sip.core.annotation.Update
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class AppServiceVo {
    @NotNull(message = "ID不能为空", groups = [Update::class])
    var id: Long? = null
    var appId: Long? = null
    @NotBlank(message = "服务名不能为空", groups = [Insert::class, Update::class])
    @Length(min = 2, max = 32, message = "服务名需要2~32位", groups = [Insert::class, Update::class])
    var name: String? = null
    @Length(min = 2, max = 32, message = "PATH需要2~32位", groups = [Insert::class, Update::class])
    var path: String? = null
    @Length(max=32,message = "注册名最大32个字符",groups = [Insert::class, Update::class])
    var serverId: String? = null
    @Length(max=32,message = "URL最大255个字符",groups = [Insert::class, Update::class])
    var url: String? = null
    @NotNull(message = "过滤前缀不能为空", groups = [Insert::class, Update::class])
    var stripPrefix: Boolean? = null
    @NotNull(message = "重试不能为空", groups = [Insert::class, Update::class])
    var retryable: Boolean? = null
    var sensitiveHeaders: String? = null

    var q: String? = null
}
