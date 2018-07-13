package com.basicfu.sip.core.handler

import com.basicfu.sip.core.exception.CustomException
import com.basicfu.sip.core.model.Result
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.NoHandlerFoundException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class GlobalExceptionHandler {
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
        return Result.error(com.basicfu.sip.core.Enum.SERVER_ERROR.msg, -1)
    }

    /**
     * spring接收参数异常
     */
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException::class)
    private fun httpMessageNotReadableException(e: HttpMessageNotReadableException): Result<Any> {
        log.error("\${Enum.INVALID_PARAMETER.msg}-【msg】--" + e.message)
        return Result.error(
            com.basicfu.sip.core.Enum.INVALID_PARAMETER.msg,
            com.basicfu.sip.core.Enum.INVALID_PARAMETER.value
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
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException::class)
    private fun notValidException(e: Exception): Result<Any> {
        log.error(e.message)
        return Result.error(
            com.basicfu.sip.core.Enum.INVALID_PARAMETER.msg,
            com.basicfu.sip.core.Enum.INVALID_PARAMETER.value
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
        response.status = 405
        log.error(e.message)
        return Result.error(e.message.toString(), 405)
    }
}
