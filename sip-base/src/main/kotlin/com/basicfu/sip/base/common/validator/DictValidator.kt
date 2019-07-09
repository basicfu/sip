package com.basicfu.sip.base.common.validator

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * @author basicfu
 * @date 2018/9/12
 */
class DictValidator : ConstraintValidator<Dict, String> {
    private var dict: String? = null

    override fun initialize(dict: Dict) {
        this.dict = dict.dict
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
//        if(value==null){
//            return false
//        }
//        val dict = this.dict ?: throw RuntimeException("dict is null")
//        val dictMap = RedisUtil.get<DictDto>("${Constant.Redis.DICT}${AppUtil.getAppId()}")
//            ?.children?.associateBy({ it.value!! }, { it })?.get(dict)?.children?.associateBy { it.value }
//        if(dictMap?.containsKey(value) == false){
//            context.disableDefaultConstraintViolation()
//            context.buildConstraintViolationWithTemplate("值[$value]不在字典中").addConstraintViolation()
//            return false
//        }
        return true
    }
}
