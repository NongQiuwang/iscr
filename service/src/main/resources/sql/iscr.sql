/*
 Navicat Premium Dump SQL

 Source Server         : 本地MySQL
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : iscr

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 17/09/2026 03:40:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_car
-- ----------------------------
DROP TABLE IF EXISTS `tb_car`;
CREATE TABLE `tb_car`  (
  `car_id` int NOT NULL AUTO_INCREMENT COMMENT '汽车ID',
  `brand` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品牌',
  `series` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车系',
  `year` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '年款',
  `model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '型号',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类别（如中大型车）',
  `displacement` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '排量（如2.0T）',
  `box_count` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '厢数',
  `seat_count` int NULL DEFAULT NULL COMMENT '座位数',
  `transmission` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '变速箱（自动/手动）',
  `door_count` int NULL DEFAULT NULL COMMENT '车门数',
  `fuel_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '燃料类型（汽油/柴油/电动）',
  `fuel_label` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '燃料标号（如95号）',
  `tank_capacity` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '油箱容积（如73L）',
  `maintenance_mileage` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '养护里程（如5000公里）',
  `daily_rent` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '日租金',
  `status` int NULL DEFAULT 0 COMMENT '状态：0-可租，1-已租，2-维护中',
  `image_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片路径',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`car_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '汽车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_car
-- ----------------------------
INSERT INTO `tb_car` VALUES (1, 'Audi', 'A6L', '2024 Model', '45 TFSI quattro Luxury Dynamic', 'Mid-Large Sedan', '2.0T', 'threeCompartment', 5, 'automatic', 4, 'Gasoline', '95 Octane', '73L', '5000KM', 500.00, 0, NULL, '2026-01-21 22:33:07', '2026-09-17 01:36:17');
INSERT INTO `tb_car` VALUES (2, 'BMW', '5 Series', '2024 Model', '530Li Exclusive M Sport', 'Mid-Large Sedan', '2.0T', 'threeCompartment', 5, 'automatic', 4, 'Gasoline', '95 Octane', '68L', '10000KM', 550.00, 0, NULL, '2026-01-21 22:33:07', '2026-09-17 01:36:17');
INSERT INTO `tb_car` VALUES (3, 'Mercedes-Benz', 'E-Class', '2024 Model', 'E 300 L Luxury', 'Mid-Large Sedan', '2.0T', 'threeCompartment', 5, 'automatic', 4, 'Gasoline', '95 Octane', '66L', '10000KM', 600.00, 0, NULL, '2026-01-21 22:33:07', '2026-09-17 01:36:17');
INSERT INTO `tb_car` VALUES (4, 'Tesla', 'Model 3', '2024 Model', 'RWD Edition', 'Mid-size Sedan', 'Electric', 'threeCompartment', 5, 'automatic', 4, 'Electric', '-', '-', '20000KM', 400.00, 0, NULL, '2026-01-21 22:33:07', '2026-09-17 01:36:17');
INSERT INTO `tb_car` VALUES (5, 'Toyota', 'Camry', '2024 Model', '2.0E Elite Edition', 'Mid-size Sedan', '2.0L', 'threeCompartment', 5, 'automatic', 4, 'Gasoline', '92 Octane', '60L', '5000KM', 300.00, 0, NULL, '2026-01-21 22:33:07', '2026-09-17 01:36:17');

-- ----------------------------
-- Table structure for tb_reservation
-- ----------------------------
DROP TABLE IF EXISTS `tb_reservation`;
CREATE TABLE `tb_reservation`  (
  `reservation_id` int NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `car_id` int NOT NULL COMMENT '汽车ID',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `total_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '总价',
  `status` int NULL DEFAULT 0 COMMENT '状态：0-待确认，1-已确认，2-已取消，3-已完成',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`reservation_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `car_id`(`car_id` ASC) USING BTREE,
  CONSTRAINT `tb_reservation_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tb_users` (`u_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `tb_reservation_ibfk_2` FOREIGN KEY (`car_id`) REFERENCES `tb_car` (`car_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '预约表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_reservation
-- ----------------------------
INSERT INTO `tb_reservation` VALUES (11, 6, 1, '2026-09-17', '2026-09-18', 1000.00, 0, '2026-09-16 08:33:53', '2026-09-16 08:33:53');

-- ----------------------------
-- Table structure for tb_users
-- ----------------------------
DROP TABLE IF EXISTS `tb_users`;
CREATE TABLE `tb_users`  (
  `u_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码（加密）',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `role` int NULL DEFAULT 0 COMMENT '角色：0-普通用户，1-管理员',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`u_id`) USING BTREE,
  UNIQUE INDEX `user_name`(`user_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_users
-- ----------------------------
INSERT INTO `tb_users` VALUES (1, 'admin', 'admin@example.com', '$2a$10$ZP4oW1svmYjYW2c4RUjk9uen3/U3J5Wzsu/hqfhIatRGti2bHzMGS', '13800138000', 1, '2026-01-21 22:33:07', '2026-09-16 07:43:37');
INSERT INTO `tb_users` VALUES (2, 'user1', 'user1@example.com', '$2a$10$qS1IlNaxXigeeWbaNtwNTuYiw3J8pnUsVrz68lBZXqkuQ62tNmWmG', '13800138001', 0, '2026-01-21 22:33:07', '2026-09-16 07:43:37');
INSERT INTO `tb_users` VALUES (6, 'user2', 'nqw2002@gmail.com', '$2a$10$iUJG8gC6S8hgdohENbFcreVMEqgX8zBunmSNVUSChfpHwrMcsBP8i', '13377157493', 0, '2026-09-16 08:31:29', '2026-09-16 08:31:29');

SET FOREIGN_KEY_CHECKS = 1;
