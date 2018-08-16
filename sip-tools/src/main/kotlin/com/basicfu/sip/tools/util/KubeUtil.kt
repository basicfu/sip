package com.basicfu.sip.tools.util

import com.basicfu.sip.tools.common.Constant
import com.google.protobuf.Descriptors
import hapi.chart.ChartOuterClass
import hapi.chart.ConfigOuterClass
import hapi.services.tiller.Tiller
import io.fabric8.kubernetes.client.ConfigBuilder
import io.fabric8.kubernetes.client.DefaultKubernetesClient
import io.grpc.StatusRuntimeException
import org.kamranzafar.jtar.TarInputStream
import org.microbean.helm.ReleaseManager
import org.microbean.helm.chart.TapeArchiveChartLoader
import org.yaml.snakeyaml.Yaml
import java.io.BufferedInputStream
import java.lang.reflect.UndeclaredThrowableException
import java.util.zip.GZIPInputStream


/**
 * @author basicfu
 * @date 2018/8/15
 */
object KubeUtil {
    private var releaseManager:ReleaseManager?=null
    init {
        init()
    }
    private fun init(){
        val builder = ConfigBuilder()
        val config = builder.build()
        config.masterUrl = Constant.KUBE_URL
        config.clientKeyData =Constant.KUBE_CLIENT_KEY
        config.clientCertData =Constant.KUBE_CLIENT_CERT
        config.caCertData =Constant.KUBE_CA_CERT
        val client = DefaultKubernetesClient(config)
        val tiller = org.microbean.helm.Tiller(client)
        releaseManager = ReleaseManager(tiller)
    }
    @Suppress("UNCHECKED_CAST")
    private fun generate(values:String,set:Array<String>?): ChartOuterClass.Chart.Builder {
        val inputStream = this.javaClass.getResourceAsStream("/file/example-0.0.1.tgz")
        val tarInputStream = TarInputStream(GZIPInputStream(BufferedInputStream(inputStream)))
        val chartLoader = TapeArchiveChartLoader()
        val chartBuilder=chartLoader.load(tarInputStream).build().toBuilder()
        val load = Yaml().load(values)
        val linkedHashMap = load as LinkedHashMap<String, Any>
        set?.let {
            it.forEach {item->
                val split = item.split("=")
                val firstKey = split[0].split(".")[0]
                val secondKey = split[0].split(".")[1]
                val value=split[1]
                val firstMap=linkedHashMap[firstKey] as LinkedHashMap<String,Any>
                firstMap[secondKey]=value
                linkedHashMap[firstKey]=firstMap
            }
        }
        val valuesBuilder=chartBuilder.valuesBuilder
        valuesBuilder.raw=Yaml().dump(load)
        return chartBuilder
    }
    fun install(name:String,namespace:String,values:String,set:Array<String>?){
        val chartBuilder = generate(values,set)
        val install = Tiller.InstallReleaseRequest.newBuilder()
        install.name=name
        install.namespace=namespace
        if(releaseManager==null)init()
        releaseManager!!.install(install,chartBuilder)
        println("$name install success")
    }
    fun update(name:String,values:String,set:Array<String>?){
        val chartBuilder = generate(values,set)
        val update = Tiller.UpdateReleaseRequest.newBuilder()
        update.name=name
        if(releaseManager==null)init()
        releaseManager!!.update(update,chartBuilder)
        println("$name update success")
    }
    fun delete(name:String){
        val delete = Tiller.UninstallReleaseRequest.newBuilder()
        delete.name=name
        delete.purge=true
        if(releaseManager==null)init()
        releaseManager!!.uninstall(delete.build())
    }
    fun exists(name:String):Boolean{
        val get = Tiller.GetReleaseContentRequest.newBuilder()
        get.name=name
        if(releaseManager==null)init()
        val content = releaseManager!!.getContent(get.build())
        try{
            content.get()
        }catch (e: Exception){
            if(e.message!=null&&e.message!!.startsWith("io.grpc.StatusRuntimeException: UNKNOWN: release:")){
                return false
            }
        }
        return true
    }
}

