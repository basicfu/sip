package com.basicfu.sip.client.util

import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.client.feign.MailFeign
import com.basicfu.sip.client.model.Result
import com.basicfu.sip.client.model.SendMailVo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils
import java.io.File
import javax.annotation.PostConstruct

/**
 * @author tianhu
 * @date 2018/7/20
 */
@Suppress("UNCHECKED_CAST")
@Component
class MailUtil {

    @Autowired
    private lateinit var mailFeignTmp: MailFeign

    @PostConstruct
    fun init() {
        MailUtil.mailFeign = mailFeignTmp
    }

    companion object {
        private lateinit var mailFeign: MailFeign

        private fun sendMail(vo: SendMailVo): Result<JSONObject>{
            return mailFeign.sendMail(vo)
        }

        fun useTemplate(): UseTemplate {
            return UseTemplate()
        }

        fun disUseTemplate(): DisUseTemplate {
            return DisUseTemplate()
        }

        class UseTemplate {
            private var appId: Long? = null
            private var code: String? = null
            private var subject: String? = null
            private var async: Boolean = false
            private var properties: MutableMap<String, String>? = null
            private var files: MutableList<File>? = null
            private var images: Map<String, File>? = null
            fun appId(appId: Long?): UseTemplate {
                this.appId = appId
                return this
            }

            fun code(code: String): UseTemplate {
                this.code = code
                return this
            }

            fun subject(subject: String): UseTemplate{
                this.subject = subject
                return this
            }

            fun addProperties(k: String, v: String): UseTemplate {
                if (null == this.properties) {
                    this.properties = mutableMapOf()
                }
                this.properties!![k] = v
                return this
            }

            fun cover(): Cover {
                return Cover()
            }

            fun async(async: Boolean): UseTemplate {
                this.async = async
                return this
            }

            fun addFile(file: File): UseTemplate {
                if (CollectionUtils.isEmpty(this.files)) {
                    this.files = mutableListOf()
                }
                this.files!!.add(file)
                return this
            }

            fun addFile(files: List<File>): UseTemplate {
                if (CollectionUtils.isEmpty(this.files)) {
                    this.files = mutableListOf()
                }
                this.files!!.addAll(files)
                return this
            }

            fun addImages(images: Map<String, File>): UseTemplate {
                this.images = images
                return this
            }

            fun send(): Result<JSONObject> {
                if (null == appId) throw RuntimeException("appId不能为空")
                if (code.isNullOrBlank()) throw RuntimeException("邮件模板code未配置")
                val vo = SendMailVo()
                vo.appId = appId
                vo.code = code
                vo.subject = subject
                vo.properties = properties
                vo.async = async
                vo.useTemplate = true
                return sendMail(vo)
            }

            inner class Cover {
                private var toUser: MutableList<String>? = null
                private var copyUser: MutableList<String>? = null

                fun addReceiver(receiver: String): Cover {
                    if (CollectionUtils.isEmpty(toUser)) {
                        toUser = mutableListOf()
                    }
                    toUser!!.add(receiver)
                    return this
                }

                fun addReceiver(receiver: List<String>): Cover {
                    if (CollectionUtils.isEmpty(toUser)) {
                        toUser = mutableListOf()
                    }
                    toUser!!.addAll(receiver)
                    return this
                }

                fun addCopyer(copyer: String): Cover {
                    if (CollectionUtils.isEmpty(copyUser)) {
                        copyUser = mutableListOf()
                    }
                    copyUser!!.add(copyer)
                    return this
                }

                fun addCopyer(copyer: List<String>): Cover {
                    if (CollectionUtils.isEmpty(copyUser)) {
                        copyUser = mutableListOf()
                    }
                    copyUser!!.addAll(copyer)
                    return this
                }

                fun send(): Result<JSONObject> {
                    if (null == appId) throw RuntimeException("appId不能为空")
                    if (code.isNullOrBlank()) throw RuntimeException("邮件模板code未配置")
                    if (null == toUser || toUser!!.isEmpty()) throw RuntimeException("当选择覆盖收件人与抄送人时,收件人不能为空")
                    val vo = SendMailVo()
                    vo.appId = appId
                    vo.code = code
                    vo.useTemplate = true
                    vo.subject = subject
                    vo.async = async
                    vo.cover = true
                    vo.properties = properties
                    vo.files = files
                    vo.images = images
                    vo.toUser = this.toUser!!.toTypedArray()
                    vo.copyUser = this.copyUser?.toTypedArray()
                    return sendMail(vo)
                }
            }

        }

        class DisUseTemplate {
            private var appId: Long? = null
            private var subject: String? = null
            private var toUser: MutableList<String>? = null
            private var copyUser: MutableList<String>? = null
            private var content: String? = null
            private var async: Boolean = false
            private var files: MutableList<File>? = null
            private var images: Map<String, File>? = null

            fun appId(appId: Long?): DisUseTemplate {
                this.appId = appId
                return this
            }

            fun addReceiver(receiver: String): DisUseTemplate {
                if (CollectionUtils.isEmpty(toUser)) {
                    toUser = mutableListOf()
                }
                toUser!!.add(receiver)
                return this
            }

            fun addReceiver(receiver: List<String>): DisUseTemplate {
                if (CollectionUtils.isEmpty(toUser)) {
                    toUser = mutableListOf()
                }
                toUser!!.addAll(receiver)
                return this
            }

            fun addCopyer(copyer: String): DisUseTemplate {
                if (CollectionUtils.isEmpty(copyUser)) {
                    copyUser = mutableListOf()
                }
                copyUser!!.add(copyer)
                return this
            }

            fun addCopyer(copyer: List<String>): DisUseTemplate {
                if (CollectionUtils.isEmpty(copyUser)) {
                    copyUser = mutableListOf()
                }
                copyUser!!.addAll(copyer)
                return this
            }

            fun subject(subject: String): DisUseTemplate {
                this.subject = subject
                return this
            }


            fun content(content: String): DisUseTemplate {
                this.content = content
                return this
            }


            fun async(async: Boolean): DisUseTemplate {
                this.async = async
                return this
            }

            fun addFile(file: File): DisUseTemplate {
                if (CollectionUtils.isEmpty(this.files)) {
                    this.files = mutableListOf()
                }
                this.files!!.add(file)
                return this
            }

            fun addFile(files: List<File>): DisUseTemplate {
                if (CollectionUtils.isEmpty(this.files)) {
                    this.files = mutableListOf()
                }
                this.files!!.addAll(files)
                return this
            }

            fun addImages(images: Map<String, File>): DisUseTemplate {
                this.images = images
                return this
            }

            fun send(): Result<JSONObject> {
                if (null == this.appId) throw RuntimeException("appId不能为空")
                if (null == this.content) throw RuntimeException("邮件正文不能为空")
                if (null == this.toUser || this.toUser!!.isEmpty()) throw RuntimeException("收件人不能为空")
                val vo = SendMailVo()
                vo.appId = this.appId
                vo.async = this.async
                vo.subject = this.subject
                vo.useTemplate = false
                vo.files = this.files
                vo.images = this.images
                vo.content = this.content
                vo.toUser = this.toUser!!.toTypedArray()
                vo.copyUser = this.copyUser?.toTypedArray()
                return sendMail(vo)
            }

        }
    }

}