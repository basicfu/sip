package com.basicfu.sip.dict.common

/**
 * @author fuliang
 * @date 17/10/26
 */
enum class Enum constructor(val value: Int,val msg:String) {
    SUCCESS(0,"成功"),
    SERVER_ERROR(1,"服务器内部发生错误"),
    INVALID_PARAMETER(2,"无效的参数"),
    MISSING_PARAMETER(3,"缺少必要的参数"),
    INVALID_PERMISSION(4,"无权操作"),
    LOGIN_TIMEOUT(5,"登录超时"),
    ILLEGAL_REQUEST(6,"非法请求");
    enum class FiledType {
        STRING(),
        NUMBER(),
        DATE(),
        TIME(),
        DATETIME(),
        DEVICE(),
        NO(),
    }
    enum class Order {
        ASC(),
        DESC(),
    }
    enum class Resume constructor(val value: Int,val msg:String) {
        ALREADY_EXIST(1000,"简历已存在"),
        NO_EMAIL_NO_MOBILE(1001, "请输入电子邮箱或联系电话"),
        CLAIM(1002, "该简历已经被认领"),
        BLACK_EXIST(1003, "该简历已经被列入黑名单")
    }

    enum class IsConcern constructor(val value: Int,val msg:String){
        YES(1,"关注"),
        NO(0,"取消关注")
    }

    enum class ConcernType constructor(val value: Int,val msg:String){
        PART_IN(1,"参与"),
        CONCERN(0,"关注")
    }

    enum class ResumeDynamicType constructor(val value: Int,val msg:String){
        UPDATE(1,"简历更新"),
        RECOMMEND(2,"推荐记录"),
        BLACKLIST(3,"黑名单"),
        JOBING(4, "进行中的职位")
    }

    enum class DeliverState constructor(val value: Int,val msg:String){
        SEND(0,"已投递"),
        RECOMMEND(5,"已推荐"),
        FIRST_INTERVIEW(10,"预约初试"),
        SECOND_INTERVIEW(15,"预约复试"),
        PASS_INTERVIEW(20,"面试通过"),
        CONFIRM_ENTRY(25,"确认入职"),
        DURING_ENTRNCE(30,"入场确认中"),
        CONFIRM_ENTRNCE(35,"确认入场"),
        RESUME_UNPASS(-5,"简历未通过"),
        INTERVIEW_FIRST_UNPASS(-10,"初试未通过"),
        INTERVIEW_SECOND_UNPASS(-15,"复试未通过"),
        INTERVIEW_REJECT(-20,"拒绝面试"),
        INTERVIEW_BREAK(-25,"面试爽约"),
        ENTRY_REJECT(-30,"拒绝入职"),
        BACK(-35,"退回");

        companion object {
            fun formateValue(value: Int): DeliverState? {
                val values = DeliverState.values()
                values.forEach {
                    v ->
                    if (value == v.value) {
                        return v
                    }
                }
                return null
            }
        }
    }

    enum class Type constructor(val value: Int,msg: String){
        CLIENT(1,"客户端"),
        SERVER(2,"服务端")
    }



}
