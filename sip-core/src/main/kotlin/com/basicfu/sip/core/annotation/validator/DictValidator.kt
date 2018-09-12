package com.basicfu.sip.core.annotation.validator

import com.basicfu.sip.core.common.Constant
import com.basicfu.sip.core .model.dto.DictDto
import com.basicfu.sip.core.util.RedisUtil
import com.basicfu.sip.core.util.RequestUtil
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * @author basicfu
 * @date 2018/9/12
 */
class DictValidator : ConstraintValidator<Dict, String> {
    private var dict:String?=null

    override fun initialize(dict: Dict) {
        this.dict=dict.dict
    }

    override fun isValid(value: String, context: ConstraintValidatorContext): Boolean {
        val dict = this.dict ?: throw RuntimeException("dict is null")
        val dictMap = RedisUtil.get<DictDto>("${Constant.Redis.DICT}${RequestUtil.getParameter(Constant.System.APP_CODE)}")
            ?.children?.associateBy({ it.value!! }, { it })?.get(dict)?.children?.associateBy { it.value }
        if(dictMap?.containsKey(value) == false){
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate("值【$value】不在字典中").addConstraintViolation()
            return false
        }
        return true
    }
}
