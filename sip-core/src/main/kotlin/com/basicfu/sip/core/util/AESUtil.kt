package com.basicfu.sip.core.util

import org.apache.tomcat.util.buf.HexUtils
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AESUtil {
    /**
     * 加密
     */
    fun encrypt(str: String, key: String): String? {
        return try {
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key.toByteArray(), "AES"))
            HexUtils.toHexString(cipher.doFinal(str.toByteArray(charset("utf-8"))))
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 解密
     */
    fun decrypt(str: String, key: String): String? {
        return try {
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key.toByteArray(), "AES"))
            val original = cipher.doFinal(HexUtils.fromHexString(str))
            String(original, charset("utf-8"))
        } catch (e: Exception) {
            null
        }
    }
}
