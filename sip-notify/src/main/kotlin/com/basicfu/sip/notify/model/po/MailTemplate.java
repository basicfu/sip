package com.basicfu.sip.notify.model.po;

import javax.persistence.*;

@Table(name = "mail_template")
public class MailTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 应用id
     */
    @Column(name = "app_id")
    private Long appId;

    /**
     * 发送者id
     */
    @Column(name = "sender_id")
    private Long senderId;

    /**
     * 邮件模板code
     */
    private String code;

    /**
     * 收件人,多个收件人使用 , 分割
     */
    @Column(name = "to_user")
    private String toUser;

    /**
     * 收件人发送类型,alone:单独发送,mass:群发
     */
    @Column(name = "to_send_type")
    private String toSendType;

    /**
     * 抄送人
     */
    @Column(name = "copy_user")
    private String copyUser;

    /**
     * 抄送人发送类型,alone:单独发送,mass:群发
     */
    @Column(name = "copy_send_type")
    private String copySendType;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件描述
     */
    private String description;

    /**
     * 是否启用,0:禁用,1:启用
     */
    private Byte enable;

    /**
     * 创建时间
     */
    private Integer cdate;

    /**
     * 更新时间
     */
    private Integer udate;

    /**
     * 邮件正文
     */
    private String content;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取应用id
     *
     * @return app_id - 应用id
     */
    public Long getAppId() {
        return appId;
    }

    /**
     * 设置应用id
     *
     * @param appId 应用id
     */
    public void setAppId(Long appId) {
        this.appId = appId;
    }

    /**
     * 获取发送者id
     *
     * @return sender_id - 发送者id
     */
    public Long getSenderId() {
        return senderId;
    }

    /**
     * 设置发送者id
     *
     * @param senderId 发送者id
     */
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    /**
     * 获取邮件模板code
     *
     * @return code - 邮件模板code
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置邮件模板code
     *
     * @param code 邮件模板code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取收件人,多个收件人使用 , 分割
     *
     * @return to_user - 收件人,多个收件人使用 , 分割
     */
    public String getToUser() {
        return toUser;
    }

    /**
     * 设置收件人,多个收件人使用 , 分割
     *
     * @param toUser 收件人,多个收件人使用 , 分割
     */
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    /**
     * 获取收件人发送类型,alone:单独发送,mass:群发
     *
     * @return to_send_type - 收件人发送类型,alone:单独发送,mass:群发
     */
    public String getToSendType() {
        return toSendType;
    }

    /**
     * 设置收件人发送类型,alone:单独发送,mass:群发
     *
     * @param toSendType 收件人发送类型,alone:单独发送,mass:群发
     */
    public void setToSendType(String toSendType) {
        this.toSendType = toSendType;
    }

    /**
     * 获取抄送人
     *
     * @return copy_user - 抄送人
     */
    public String getCopyUser() {
        return copyUser;
    }

    /**
     * 设置抄送人
     *
     * @param copyUser 抄送人
     */
    public void setCopyUser(String copyUser) {
        this.copyUser = copyUser;
    }

    /**
     * 获取抄送人发送类型,alone:单独发送,mass:群发
     *
     * @return copy_send_type - 抄送人发送类型,alone:单独发送,mass:群发
     */
    public String getCopySendType() {
        return copySendType;
    }

    /**
     * 设置抄送人发送类型,alone:单独发送,mass:群发
     *
     * @param copySendType 抄送人发送类型,alone:单独发送,mass:群发
     */
    public void setCopySendType(String copySendType) {
        this.copySendType = copySendType;
    }

    /**
     * 获取邮件主题
     *
     * @return subject - 邮件主题
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 设置邮件主题
     *
     * @param subject 邮件主题
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 获取邮件描述
     *
     * @return description - 邮件描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置邮件描述
     *
     * @param description 邮件描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取是否启用,0:禁用,1:启用
     *
     * @return enable - 是否启用,0:禁用,1:启用
     */
    public Byte getEnable() {
        return enable;
    }

    /**
     * 设置是否启用,0:禁用,1:启用
     *
     * @param enable 是否启用,0:禁用,1:启用
     */
    public void setEnable(Byte enable) {
        this.enable = enable;
    }

    /**
     * 获取创建时间
     *
     * @return cdate - 创建时间
     */
    public Integer getCdate() {
        return cdate;
    }

    /**
     * 设置创建时间
     *
     * @param cdate 创建时间
     */
    public void setCdate(Integer cdate) {
        this.cdate = cdate;
    }

    /**
     * 获取更新时间
     *
     * @return udate - 更新时间
     */
    public Integer getUdate() {
        return udate;
    }

    /**
     * 设置更新时间
     *
     * @param udate 更新时间
     */
    public void setUdate(Integer udate) {
        this.udate = udate;
    }

    /**
     * 获取邮件正文
     *
     * @return content - 邮件正文
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置邮件正文
     *
     * @param content 邮件正文
     */
    public void setContent(String content) {
        this.content = content;
    }
}