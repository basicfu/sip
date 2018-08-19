package com.basicfu.sip.tools.util

import org.apache.commons.lang3.StringUtils

/**
 * @author basicfu
 * @date 2018/8/18
 */
object HelmUtil{
    fun exists(name:String):Boolean{
        val result = SSHUtil.execShell("helm status $name")
        return !result.contains("release: \"$name\" not found")
    }
    fun install(name:String,namespace:String,values:String,param:String):String{
        return SSHUtil.execShell(
            "cat <<< '" + values +
                    "' > values.yaml &&\n" +
                    "helm install example-0.0.1.tgz -f values.yaml -n $name --namespace $namespace $param"
        )
    }
    fun update(name:String,values:String,param:String):String{
        return SSHUtil.execShell("cat <<< '" + values +
                "' > values.yaml &&\n" +
                "helm upgrade $name example-0.0.1.tgz -f values.yaml $param")
    }
}
