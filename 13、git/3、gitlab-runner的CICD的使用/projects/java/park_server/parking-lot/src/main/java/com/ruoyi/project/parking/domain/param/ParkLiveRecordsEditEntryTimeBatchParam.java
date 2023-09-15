package com.ruoyi.project.parking.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 批量修改 在场记录 入场时间参数
 */
@Data
public class ParkLiveRecordsEditEntryTimeBatchParam {

    /**
     * 在场记录id 列表
     */
    private List<Integer> liveIdList;

    /**
     * 入场时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTime;
}
