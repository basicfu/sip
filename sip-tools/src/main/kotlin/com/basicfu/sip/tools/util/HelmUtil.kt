package com.basicfu.sip.tools.util

/**
 * @author basicfu
 * @date 2018/8/18
 */
object HelmUtil {
    fun exists(name: String): Boolean {
        val result = SSHUtil.execShell("helm status $name")
        return !result.contains("release: \"$name\" not found")
    }

    fun install(name: String, namespace: String, values: String, param: String): String {
        return SSHUtil.execShell(
            "tee values.yaml << EOF\n"
                    + values +
                    "EOF\n" +
                    "helm install example-0.0.1.tgz -f values.yaml -n $name --namespace $namespace $param && rm -f values.yaml"
        )
    }

    fun update(name: String, values: String, param: String): String {
        return SSHUtil.execShell(
            "tee values.yaml << EOF\n"
                    + values +
                    "EOF\n" +
                    "helm upgrade $name example-0.0.1.tgz -f values.yaml $param && rm -f values.yaml"
        )
    }
}
