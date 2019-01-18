package com.basicfu.sip.api.model.dto

class ProjectCategoryDto {
    /**分类ID或URL ID*/
    var id: Long? = null
    var projectId: Long? = null
    var pid: Long? = null
    var name: String? = null
    var sort: Int? = null
    var cdate: Int? = null
    var udate: Int? = null
    var cuid: Long? = null

    /**目录、URL*/
    var type:String?=null
    /**请求方式*/
    var method:String?=null
    var path:String?=null
    var children:List<ProjectCategoryDto>?=null
}
