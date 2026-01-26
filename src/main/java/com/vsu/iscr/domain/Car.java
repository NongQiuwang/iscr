package com.vsu.iscr.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 汽车实体类
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car implements Serializable {

    private Integer carId; // 汽车ID
    private String brand; // 品牌
    private String series; // 车系
    private String year; // 年款
    private String model; // 型号
    private String category; // 类别（如中大型车）
    private String displacement; // 排量（如2.0T）
    private String boxCount; // 厢数
    private Integer seatCount; // 座位数
    private String transmission; // 变速箱（自动/手动）
    private Integer doorCount; // 车门数
    private String fuelType; // 燃料类型（汽油/柴油/电动）
    private String fuelLabel; // 燃料标号（如95号）
    private String tankCapacity; // 油箱容积（如73L）
    private String maintenanceMileage; // 养护里程（如5000公里）
    private BigDecimal dailyRent; // 日租金
    private Integer status; // 状态：0-可租，1-已租，2-维护中
    private String imagePath; // 图片路径
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}
