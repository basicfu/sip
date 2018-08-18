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
    @Value("\${ssh.hostname}")
    var sshHostname: String? = null
    @Value("\${ssh.username}")
    var sshUsername: String? = null
    @Value("\${ssh.password}")
    var sshPassword: String? = null
    @Value("\${ssh.port}")
    var sshPort: Int? = null

    @PostConstruct
    fun init() {
        SSH_HOSTNAME = sshHostname
        SSH_USERNAME = sshUsername
        SSH_PASSWORD = sshPassword
        SSH_PORT = sshPort
    }

    companion object {
        var SSH_HOSTNAME: String? = null
        var SSH_USERNAME: String? = null
        var SSH_PASSWORD: String? = null
        var SSH_PORT: Int? = null
    }
}
