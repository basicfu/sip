package com.basicfu.sip.logs.task

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * @author basicfu
 * @date 2018/8/24
 */
@Component
class Task {

    /**
     * 暂部单台
     * 暂时用单点调度,待sip-schedule完成迁入
     */
    @Scheduled(cron = "0 0/30 0 * * ?")
    fun pullLog() {

    }
}
