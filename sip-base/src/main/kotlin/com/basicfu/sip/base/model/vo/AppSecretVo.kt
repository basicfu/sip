package com.basicfu.sip.base.model.vo

import com.basicfu.sip.core.annotation.Insert
import com.basicfu.sip.core.annotation.Update
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotNull

class AppSecretVo {
    @NotNull(message = "ID不能为空", groups = [Update::class])
    var id: Long? = null
    var appId: Long? = null
    var secret: String? = null
    @Length(max=32,message = "描述最大32个字符",groups = [Insert::class, Update::class])
    var description: String? = null

    var q:String?=null
}
