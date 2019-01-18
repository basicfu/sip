package com.basicfu.sip.api.model.vo

import com.basicfu.sip.api.model.biz.ApiBiz
import com.basicfu.sip.common.enum.Enum
import com.basicfu.sip.core.annotation.Insert
import com.basicfu.sip.core.annotation.Update
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class InterfaceVo {
    var id: Long? = null
    @NotBlank(message = "接口名不能为空",groups = [Insert::class, Update::class])
    var name: String? = null
    var description: String? = null
    var status: String? = null
    @NotNull(message = "项目不能为空",groups = [Insert::class])
    var projectId: Long? = null
    @NotNull(message = "分类不能为空",groups = [Insert::class,Update::class])
    var categoryId: Long? = null
    @NotNull(message = "请求方法不能为空", groups = [Insert::class, Update::class])
    var method: Enum.RequestMethod? = null
    @NotNull(message = "HOST不能为空", groups = [Insert::class, Update::class])
    var host: String? = null
    @NotNull(message = "PATH不能为空", groups = [Insert::class, Update::class])
    var path: String? = null
    var pathParams: Array<ApiBiz.Path>? = null
    var queryParams: List<ApiBiz.Query>? = null
    var reqHeaders: List<ApiBiz.Header>? = null
    var reqBodyType: Enum.ReqBodyType? = null
    var reqBody: String? = null
    var resBodyType: String? = null
    var resBody: String? = null
    var cdate: Int? = null
    var udate: Int? = null
    var cuid: Long? = null

    var files: Array<MultipartFile>? = null
}
