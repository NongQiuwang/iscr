package com.vsu.iscr.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预约实体类
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation implements Serializable {

    private Integer reservationId; // 预约ID
    private Integer userId; // 用户ID
    private Integer carId; // 汽车ID
    private LocalDate startDate; // 开始日期
    private LocalDate endDate; // 结束日期
    private BigDecimal totalPrice; // 总价
    private Integer status; // 状态：0-待确认，1-已确认，2-已取消，3-已完成
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间

    // 扩展字段（用于查询返回）
    private String userName; // 用户名
    private String carBrand; // 汽车品牌
    private String carSeries; // 汽车车系
    private String carModel; // 汽车型号
}
