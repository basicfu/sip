package com.basicfu.sip.core.common.handler

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.common.model.Result
import com.basicfu.sip.core.common.exception.CustomException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.NoHandlerFoundException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Suppress("unused")
@ControllerAdvice
class GlobalExceptionHandler {
    enum class Enum(val value: Int, val msg: String) {
        SERVER_ERROR(-1, "系统内部错误"),
        INVALID_PARAMETER(3, "无效的参数"),
    }

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    internal var request: HttpServletRequest? = null
    //加入未授权异常
//    response.setStatus(403)

    /**
     * 全局异常
     */
    @ResponseBody
    @ExceptionHandler(Exception::class)
    private fun errorHandler(response: HttpServletResponse, e: Exception): Result<Any> {
        log.error("全局异常：", e)
        response.status = 500
        return Result.error(Enum.SERVER_ERROR.msg, -1)
    }

    /**
     * spring接收参数异常
     * 会包括bean set时抛出的异常
     */
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException::class)
    private fun httpMessageNotReadableException(e: HttpMessageNotReadableException): Result<Any> {
        log.error("${Enum.INVALID_PARAMETER.msg}-【msg】--" + e.message)
        return Result.error(
            Enum.INVALID_PARAMETER.msg,
            Enum.INVALID_PARAMETER.value
        )
    }

    /**
     * 自定义异常
     */
    @ResponseBody
    @ExceptionHandler(CustomException::class)
    private fun customException(e: CustomException): Result<Any> {
        log.error("自定义异常-", e)
        log.error("自定义异常--【code】--" + e.code + "--【msg】--" + e.msg + "--【data】--" + e.data)
        return Result.error(e.msg, e.code, e.data)
    }

    /**
     * 前台传递参数限制异常
     * 参数验证异常
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException::class)
    private fun methodArgumentNotValidException(e: MethodArgumentNotValidException): Result<Any> {
        val fieldErrorsMap = e.bindingResult.fieldErrors?.associateBy({ it.field }, { it })
        if (fieldErrorsMap != null) {
            val linkedHashList = linkedSetOf<FieldError>()
            val bean = e.bindingResult.target
            val declaredFields = bean::class.java.declaredFields
            declaredFields.forEach {
                val error = fieldErrorsMap[it.name]
                if (error != null) {
                    linkedHashList.add(error)
                }
            }
            val first = linkedHashList.first()
            val array = JSONArray()
            linkedHashList.forEach {
                val data = JSONObject()
                data["field"] = it.field
                data["msg"] = it.defaultMessage
                array.add(data)
            }
            return Result.error(
                first.defaultMessage,
                Enum.INVALID_PARAMETER.value,
                array
            )
        }
        return Result.error(
            Enum.INVALID_PARAMETER.msg,
            Enum.INVALID_PARAMETER.value
        )
    }

    /**
     * 404
     */
    @ResponseBody
    @ExceptionHandler(NoHandlerFoundException::class)
    private fun noHandlerFoundException(response: HttpServletResponse, e: NoHandlerFoundException): Result<Any> {
        response.status = 404
        log.error(e.message)
        return Result.error(e.message.toString(), 404)
    }

    /**
     * not supported method
     */
    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    private fun httpMessageNotReadableException(
        response: HttpServletResponse,
        e: HttpRequestMethodNotSupportedException
    ): Result<Any> {
        response.status = 200
        log.error(e.message)
        return Result.error(e.message.toString(), 405)
    }

    /**
     * Content type not supported
     */
    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    private fun httpMediaTypeNotSupportedException(
        response: HttpServletResponse,
        e: HttpMediaTypeNotSupportedException
    ): Result<Any> {
        response.status = 200
        log.error(e.message)
        return Result.error(e.message.toString(), 500)
    }
}
