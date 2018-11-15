package com.basicfu.sip.base.model.vo

import com.basicfu.sip.common.constant.Constant
import com.basicfu.sip.common.validator.Dict
import com.basicfu.sip.core.annotation.Insert
import com.basicfu.sip.core.annotation.Update
import org.hibernate.validator.constraints.NotBlank
import javax.validation.constraints.NotNull

class UserTemplateVo {

    @NotNull(message = "ID不能为空", groups = [Update::class])
    var id: Long? = null
    var appId: Long? = null
    @NotBlank(message = "字段名不能为空", groups = [Insert::class, Update::class])
    var name: String? = null
    @NotBlank(message = "字段英文名不能为空", groups = [Insert::class, Update::class])
    var enName: String? = null
    @Dict(message = "字段类型不能为空", groups = [Insert::class, Update::class], dict = Constant.Dict.USER_TEMPLATE_FIELD_TYPE)
    var type: String? = null
    @NotBlank(message = "扩展信息不能为空", groups = [Insert::class, Update::class])
    var extra: String? = null
    var defaultValue: String? = null
    @NotNull(message = "是否必填不能为空", groups = [Insert::class, Update::class])
    var required: Boolean? = null
    var sort: Int? = null

    var q: String? = null
}
