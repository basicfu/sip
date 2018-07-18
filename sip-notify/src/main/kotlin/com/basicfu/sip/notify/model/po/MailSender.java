package com.basicfu.sip.notify.model.po;

import javax.persistence.*;

@Table(name = "mail_sender")
public class MailSender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 应用id
     */
    @Column(name = "app_id")
    private Long appId;

    /**
     * 发件人用户名
     */
    @Column(name = "from_uname")
    private String fromUname;

    /**
     * 发件人密码
     */
    @Column(name = "from_upwd")
    private String fromUpwd;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Integer cdate;

    /**
     * 更新时间
     */
    private Integer udate;

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
     * 获取发件人用户名
     *
     * @return from_uname - 发件人用户名
     */
    public String getFromUname() {
        return fromUname;
    }

    /**
     * 设置发件人用户名
     *
     * @param fromUname 发件人用户名
     */
    public void setFromUname(String fromUname) {
        this.fromUname = fromUname;
    }

    /**
     * 获取发件人密码
     *
     * @return from_upwd - 发件人密码
     */
    public String getFromUpwd() {
        return fromUpwd;
    }

    /**
     * 设置发件人密码
     *
     * @param fromUpwd 发件人密码
     */
    public void setFromUpwd(String fromUpwd) {
        this.fromUpwd = fromUpwd;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
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
}