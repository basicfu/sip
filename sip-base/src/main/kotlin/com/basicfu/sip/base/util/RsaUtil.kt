package com.basicfu.sip.base.util

import com.basicfu.sip.core.util.log
import org.apache.commons.codec.binary.Base64
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec

/**
 * @author basicfu
 * @date 2019-07-06
 */
object RsaUtil {
    fun rsa256Sign(content: String, privateKey: String, charset: String?): String? {
        try {
            val factory = KeyFactory.getInstance("RSA")
            val priKey = factory.generatePrivate(PKCS8EncodedKeySpec(privateKey.toByteArray()))
            val signature = java.security.Signature.getInstance("SHA256WithRSA")
            signature.initSign(priKey)
            if (charset.isNullOrBlank()) {
                signature.update(content.toByteArray())
            } else {
                signature.update(content.toByteArray(charset(charset)))
            }
            return String(Base64.encodeBase64(signature.sign()))
        } catch (e: Exception) {
            log.error("RSAcontent = $content; charset = $charset", e)
        }
        return null
    }
}
