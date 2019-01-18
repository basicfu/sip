package com.basicfu.sip.api.model.po

import javax.persistence.*

@Table(name = "interface_history")
class InterfaceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    @Column(name = "interface_id")
    var interfaceId: Long? = null
    var type: String? = null
    var name: String? = null
    var description: String? = null
    var status: String? = null
    @Column(name = "project_id")
    var projectId: Long? = null
    @Column(name = "category_id")
    var categoryId: Long? = null
    var method: String? = null
    var path: String? = null
    @Column(name = "path_params")
    var pathParams: String? = null
    @Column(name = "query_params")
    var queryParams: String? = null
    @Column(name = "req_headers")
    var reqHeaders: String? = null
    @Column(name = "req_body_type")
    var reqBodyType: String? = null
    @Column(name = "req_body")
    var reqBody: String? = null
    @Column(name = "res_body_type")
    var resBodyType: String? = null
    @Column(name = "res_body")
    var resBody: String? = null
    var cdate: Int? = null
    var cuid: Long? = null
}
