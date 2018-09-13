package com.basicfu.sip.base.util

import com.basicfu.sip.base.common.Enum
import com.basicfu.sip.client.util.DictUtil
import com.basicfu.sip.core.common.exception.CustomException
import org.apache.commons.lang3.StringUtils
import java.math.BigDecimal
import java.text.SimpleDateFormat

/**
 * @author basicfu
 * @date 2018/9/13
 */
object UserTemplateUtil {
    /**
     * 检查用户模板扩展字段格式和
     */
    fun checkFormat(type: String, extra: String,value:String?) {
        val prefix="扩展信息:"
        val default="默认值不符合设置的扩展信息:"
        when (Enum.FieldType.valueOf(type)) {
        //2~32
            Enum.FieldType.TEXT -> {
                val split = extra.split("~")
                if (split.size != 2) {
                    throw CustomException("${prefix}约束格式不正确,如:2~32")
                }
                val start = split[0]
                val end = split[1]
                if (!StringUtils.isNumeric(start) || !StringUtils.isNumeric(end)) {
                    throw CustomException("${prefix}约束格式不正确,需要为整数数字,如:2~32")
                }
                val startLength=start.toInt()
                val endLength=end.toInt()
                if(start.startsWith("0")||end.startsWith("0")){
                    throw CustomException("${prefix}约束格式不正确,请输入非0开头的整数,如:2~32")
                }
                if (startLength !in 1..999999999 || endLength !in 1..999999999) {
                    throw CustomException("${prefix}开始和结束值在1~999999999之间")
                }
                if (startLength > endLength) {
                    throw CustomException("${prefix}结束值不能大于开始值")
                }
                if (!value.isNullOrBlank()&&value!!.length !in startLength..endLength) {
                    throw CustomException("${default}长度需要[$startLength~$endLength]个字符")
                }
            }
        /**
         * 整数位最大位数,小数位最大位数&最小值~最大值
         * eg:
         * 3,0&0~100
         * 2,5&100.48~200.3288
         */
            Enum.FieldType.NUMBER -> {
                val extraArray = extra.split("&")
                if (extraArray.size != 2) {
                    throw CustomException("${prefix}约束格式不正确,如:3,0&0~100")
                }
                val lengthRange = extraArray[0].split(",")
                val valueRange = extraArray[1].split("~")
                if ((lengthRange.size != 2||lengthRange[1].isEmpty()) || (valueRange.size != 2||valueRange[1].isEmpty())) {
                    throw CustomException("${prefix}约束格式不正确,如:3,0&0~100")
                }
                if(!value.isNullOrBlank()){
                    val startLength = lengthRange[0].toInt()
                    val endLength = lengthRange[1].toInt()
                    val startValue = valueRange[0].toFloat()
                    val endValue = valueRange[1].toFloat()
                    if(startLength>20){
                        throw CustomException("${prefix}整数位最大20位")
                    }
                    if(endLength>10){
                        throw CustomException("${prefix}小数位最大10位")
                    }
                    val splitValue = value!!.split(".")
                    if(splitValue.contains(".")&&(splitValue.size!=2||splitValue[1].isEmpty())){
                        throw CustomException("${default}不能只有点没有小数")
                    }
                    if (splitValue.size == 1) {
                        //移除符号判断长度
                        if (Math.abs(splitValue[0].toLong()).toString().length !in 1..startLength) {
                            throw CustomException("${default}整数位需要[1~$startLength]位")
                        }
                        if (splitValue[0].toLong() !in startValue..endValue) {
                            throw CustomException("${default}值范围需要[${BigDecimal(startValue.toString()).setScale(endLength)}~${BigDecimal(endValue.toString()).setScale(endLength)}]之间")
                        }
                    }
                    if (splitValue.size == 2) {
                        if (Math.abs(splitValue[0].toLong()).toString().length !in 1..startLength) {
                            throw CustomException("${default}整数位需要[1~$startLength]位")
                        }
                        if (splitValue[1].length > endLength) {
                            throw CustomException("${default}小数位不能大于$endLength]位")
                        }
                        //小数为不够自动补0
                        val floatValue = BigDecimal(value).setScale(endLength).toFloat()
                        if (floatValue !in startValue..endValue) {
                            throw CustomException("${default}值范围需要[${BigDecimal(startValue.toString()).setScale(endLength)}~${BigDecimal(endValue.toString()).setScale(endLength)}]之间")
                        }
                    }
                }
            }
            Enum.FieldType.CHECK, Enum.FieldType.RADIO, Enum.FieldType.SELECT -> {
                //TODO 待修改为缓存格式
                val dict=DictUtil.get(extra)
                if (dict==null||dict.isEmpty()) {
                    throw CustomException("${prefix}字典${extra}不存在")
                }
                if(!value.isNullOrBlank()){
                    if(!dict.associateBy{it.value}.containsKey(value)){
                        throw CustomException("${default}不在字典${extra}中")
                    }
                }
            }
            Enum.FieldType.DATE -> {
                val sdf:SimpleDateFormat
                try {
                    sdf=SimpleDateFormat(extra)
                } catch (e: Exception) {
                    throw CustomException("${prefix}日期格式不正确,需要输入支持Java SimpleDateFormat的格式")
                }
                if(!value.isNullOrBlank()){
                    try {
                        sdf.parse(value)
                    }catch (e:Exception){
                        throw CustomException("${default}格式不正确")
                    }
                }
            }
        }
    }
}
