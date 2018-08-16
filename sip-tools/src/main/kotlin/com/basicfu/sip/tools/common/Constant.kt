package com.basicfu.sip.tools.common

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * @author basicfu
 * @date 2018/8/15
 */
@Component
class Constant {
    @Value("\${kube.url}")
    var kubeUrl: String? = null
    @Value("\${kube.client.key}")
    var kubeClientKey: String? = null
    @Value("\${kube.client.cert}")
    var kubeClientCert: String? = null
    @Value("\${kube.ca.cert}")
    var kubeCaCert: String? = null

    @PostConstruct
    fun init() {
        KUBE_URL = kubeUrl
        KUBE_CLIENT_KEY = kubeClientKey
        KUBE_CLIENT_CERT = kubeClientCert
        KUBE_CA_CERT = kubeCaCert
    }

    companion object {
        var KUBE_URL: String? = null
        var KUBE_CLIENT_KEY: String? = null
        var KUBE_CLIENT_CERT: String? = null
        var KUBE_CA_CERT: String? = null
    }
}
