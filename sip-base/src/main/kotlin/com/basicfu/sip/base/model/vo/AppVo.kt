package com.basicfu.sip.base.model.vo

import com.basicfu.sip.core.annotation.Insert
import com.basicfu.sip.core.annotation.Update
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class AppVo {
    @NotNull(message = "ID不能为空", groups = [Update::class])
    var id: Long? = null
    @NotBlank(message = "应用名不能为空", groups = [Insert::class, Update::class])
    @Length(min = 2, max = 32, message = "应用名需要2~32位", groups = [Insert::class, Update::class])
    var name: String? = null
    @NotBlank(message = "应用code不能为空", groups = [Insert::class, Update::class])
    @Length(min = 2, max = 32, message = "应用code需要2~32位", groups = [Insert::class, Update::class])
    var code: String? = null

    var q: String? = null
}
