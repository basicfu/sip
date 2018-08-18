package com.basicfu.sip.tools.util

import ch.ethz.ssh2.Connection
import com.basicfu.sip.core.util.FileUtil
import com.basicfu.sip.tools.common.Constant
import java.io.IOException

object SSHUtil {
    fun execShell(command:String): String {
        val hostname = Constant.SSH_HOSTNAME
        val username = Constant.SSH_USERNAME
        val password = Constant.SSH_PASSWORD
        val port = Constant.SSH_PORT!!
        val conn = Connection(hostname, port)
        conn.connect()
        val isAuthenticated = conn.authenticateWithPassword(username, password)
        if (!isAuthenticated) {
            throw IOException("Authentication failed.")
        }
        val session = conn.openSession()
        session.execCommand(command)
        val msg1 = FileUtil.inputToString(session.stdout)
        val msg2 = FileUtil.inputToString(session.stderr)
        session.close()
        conn.close()
        return msg1 + "\n" + msg2
    }
}
