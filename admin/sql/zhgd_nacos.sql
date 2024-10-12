/*
 Navicat Premium Data Transfer

 Source Server         : 智慧工地
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44-log)
 Source Host           : 123.56.77.35:3306
 Source Schema         : zhgd_nacos

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44-log)
 File Encoding         : 65001

 Date: 12/10/2024 16:53:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'service.vgroupMapping.crm_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2020-10-12 09:06:43', '2020-10-12 09:10:53', NULL, '192.168.0.119', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (2, 'service.vgroupMapping.admin_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2020-10-12 09:06:43', '2020-10-12 09:10:54', NULL, '192.168.0.119', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (3, 'store.mode', 'SEATA_GROUP', 'db', 'd77d5e503ad1439f585ac494268b351b', '2020-10-12 09:06:43', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (4, 'store.db.datasource', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2020-10-12 09:06:43', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (5, 'store.db.dbType', 'SEATA_GROUP', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2020-10-12 09:06:43', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (6, 'store.db.driverClassName', 'SEATA_GROUP', 'com.mysql.jdbc.Driver', '683cf0c3a5a56cec94dfac94ca16d760', '2020-10-12 09:06:43', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (7, 'store.db.url', 'SEATA_GROUP', 'jdbc:mysql://rm-m5eqdyah37h7a10si6o.mysql.rds.aliyuncs.com:3306/wk_seata?useUnicode=true', '7bc0dff4e2b1f41ca4eabf7bb8bb1173', '2020-10-12 09:06:43', '2020-12-09 01:45:17', NULL, '192.168.0.119', '', '', '', '', '', '', '');
INSERT INTO `config_info` VALUES (8, 'store.db.user', 'SEATA_GROUP', 'qdoner_123', '63a9f0ea7bb98050796b649e85481845', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (9, 'store.db.password', 'SEATA_GROUP', 'qdoner_123', '5f4dcc3b5aa765d61d8327deb882cf99', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (10, 'store.db.minConn', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (11, 'store.db.maxConn', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (12, 'store.db.globalTable', 'SEATA_GROUP', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (13, 'store.db.branchTable', 'SEATA_GROUP', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (14, 'store.db.queryLimit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (15, 'store.db.lockTable', 'SEATA_GROUP', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (16, 'store.db.maxWait', 'SEATA_GROUP', '5000', 'a35fe7f7fe8217b4369a0af4244d1fca', '2020-10-12 09:06:44', '2020-10-12 09:10:54', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (17, 'service.vgroupMapping.oa_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2020-12-09 01:42:15', '2020-12-09 01:42:47', NULL, '192.168.0.119', '', '', '', '', '', 'text', '');
INSERT INTO `config_info` VALUES (20, 'service.vgroup_mapping.crm_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2020-12-10 06:11:51', '2020-12-10 06:11:51', NULL, '192.168.0.119', '', '', 'V1.0', NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (21, 'service.vgroup_mapping.admin_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2020-12-10 06:12:49', '2020-12-10 06:12:49', NULL, '192.168.0.119', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (22, 'service.vgroup_mapping.oa_tx_group', 'DEFAULT_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2020-12-10 06:19:14', '2020-12-10 06:19:14', NULL, '192.168.0.119', '', '', 'V1.0', NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (23, 'store.db.driver-class-name', 'SEATA_GROUP', 'com.mysql.jdbc.Driver', '683cf0c3a5a56cec94dfac94ca16d760', '2020-12-10 06:20:12', '2020-12-10 06:20:28', NULL, '192.168.0.119', '', '', 'V1.0', '', '', 'text', '');
INSERT INTO `config_info` VALUES (25, 'store.db.db-type', 'SEATA_GROUP', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2020-12-10 06:26:55', '2020-12-10 06:33:14', NULL, '192.168.0.119', '', '', 'V1.0', '', '', 'text', '');
INSERT INTO `config_info` VALUES (26, 'store.db.min-conn', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2020-12-10 06:28:49', '2020-12-10 06:28:49', NULL, '192.168.0.119', '', '', 'v1.0', NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (27, 'store.db.max-conn', 'SEATA_GROUP', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', '2020-12-10 06:29:05', '2020-12-10 06:29:05', NULL, '192.168.0.119', '', '', 'v1.0', NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (28, 'store.db.query-limit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2020-12-10 06:29:27', '2020-12-10 06:29:27', NULL, '192.168.0.119', '', '', 'v1.0', NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (29, 'store.db.lock-table', 'SEATA_GROUP', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2020-12-10 06:29:48', '2020-12-10 06:29:48', NULL, '192.168.0.119', '', '', 'v1.0', NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (30, 'recovery.committing-retry-period', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2020-12-10 06:30:03', '2020-12-10 06:30:03', NULL, '192.168.0.119', '', '', 'v1.0', NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (31, 'recovery.asyn-committing-retry-period', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2020-12-10 06:30:14', '2020-12-10 06:30:14', NULL, '192.168.0.119', '', '', 'v1.0', NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (32, 'recovery.rollbacking-retry-period', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2020-12-10 06:30:23', '2020-12-10 06:30:23', NULL, '192.168.0.119', '', '', 'v1.0', NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (33, 'recovery.timeout-retry-period', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2020-12-10 06:30:32', '2020-12-10 06:30:32', NULL, '192.168.0.119', '', '', 'v1.0', NULL, NULL, 'text', NULL);

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_route
-- ----------------------------
DROP TABLE IF EXISTS `config_info_route`;
CREATE TABLE `config_info_route`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `route_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路由id',
  `uri` varchar(526) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'uri路径',
  `predicates` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '判定器',
  `filters` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '过滤器',
  `orders` int(11) NULL DEFAULT NULL COMMENT '排序',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `intercept` int(1) NOT NULL DEFAULT 1 COMMENT '是否拦截 1 是 0 否',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态：Y-有效，N-无效',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `route_id_unique`(`route_id`) USING BTREE,
  INDEX `ux_gateway_routes_uri`(`uri`(191)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 151 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '网关路由表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_route
-- ----------------------------
INSERT INTO `config_info_route` VALUES (101, 'authorization', 'lb://authorization', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/authorization*/**\"}}]', '[]', 100, '用户认证相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (102, 'admin', 'lb://admin', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/admin*/**\"}}]', '[]', 100, '系统管理相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (103, 'login', 'lb://authorization/login', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/login\"}}]', '[]', 100, '用户登录相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (104, 'logout', 'lb://authorization/logout', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/logout\"}}]', '[]', 100, '用户退出相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (105, 'reLogin', 'lb://authorization/reLogin', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/reLogin\"}}]', '[]', 100, '用户重新登录相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (106, 'crm', 'lb://crm', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/crm*/**\"}}]', '[]', 100, '客户管理相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (109, 'work', 'lb://work', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/work*/**\"}}]', '[]', 100, '项目管理相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (113, 'oa', 'lb://oa', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/oa*/**\"}}]', '[]', 100, 'OA相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (116, 'bi', 'lb://bi', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/bi*/**\"}}]', '[]', 100, '商业智能相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (117, 'file', 'http://127.0.0.1:8012/onlinePreview', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/onlinePreview\"}}]', '[]', 100, '文件预览相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (118, 'queryShareUrl', 'lb://km', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/documentShare/queryShareUrl/*\"}}]', '[]', 100, '知识库分享接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (119, 'crmCallUpload', 'lb://crmCall/upload', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"crmCall/upload/*\"}}]', '[]', 100, '呼叫中心上传接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (120, 'dLogin', 'lb://authorization/dLogin', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/dLogin/**\"}}]', '[]', 100, '钉钉登录相关接口', 0, 1, '2020-12-14 16:07:23', '2020-12-14 16:07:23');
INSERT INTO `config_info_route` VALUES (121, 'change', 'lb://authorization/change', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/change\"}}]', '[]', 100, '切换用户相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (122, 'wf', 'lb://wf', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/wf*/**\"}}]', '[]', 100, '流程引擎相关接口', 1, 1, '2021-03-11 17:00:32', '2021-03-11 17:00:32');
INSERT INTO `config_info_route` VALUES (123, 'build', 'lb://build', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/build*/**\"}}]', '[]', 100, '智慧工地--业务相关接口', 1, 1, '2021-03-12 11:00:32', '2021-03-12 11:00:32');
INSERT INTO `config_info_route` VALUES (124, 'water', 'lb://water', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/water*/**\"}}]', '[]', 100, '智慧水务--水厂采集相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (125, 'utils', 'lb://utils', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/utils*/**\"}}]', '[]', 100, '常用工具相关接口', 1, 1, '2021-03-11 17:00:32', '2021-03-11 17:00:32');
INSERT INTO `config_info_route` VALUES (126, 'socket', 'http://127.0.0.1:9898/socket.io/', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/socket.io/**\"}}]', '[]', 100, 'Socket相关接口', 1, 1, '2021-04-06 14:51:48', '2021-04-06 14:51:48');
INSERT INTO `config_info_route` VALUES (127, 'message', 'lb://message', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/msg*/**\"}}]', '[]', 100, '消息相关接口', 1, 1, '2021-03-11 17:00:32', '2021-03-11 17:00:32');
INSERT INTO `config_info_route` VALUES (128, 'websocket', 'lb://message', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/socket.web/**\"}}]', '[]', 100, 'Socket相关接口', 1, 1, '2021-04-06 14:51:48', '2021-04-06 14:51:48');
INSERT INTO `config_info_route` VALUES (129, 'datacenter', 'lb://datacenter', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/data*/**\"}}]', '[]', 100, '元数据中心相关接口', 1, 1, '2021-04-06 14:51:48', '2021-04-06 14:51:48');
INSERT INTO `config_info_route` VALUES (130, 'loginWithoutEncrypt', 'lb://authorization/loginWithoutEncrypt', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/loginWithoutEncrypt\"}}]', '[]', 100, '用户登录非加密传输相关接口', 0, 1, '2020-05-11 15:00:32', '2020-05-11 15:00:32');
INSERT INTO `config_info_route` VALUES (133, 'rpttool', 'lb://rpttool', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/jmreport/**\"}}]', '[{\"name\":\"StripPrefix\",\"args\":{\"0\":\"0\"}}]', 100, '报表工具', 1, 1, '2021-09-03 11:38:05', '2021-09-03 11:38:05');
INSERT INTO `config_info_route` VALUES (134, 'SiteDataUpload', 'http://127.0.0.1:1001/', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/\"}}]', '[]', 10000, '现场设备上传（废除）', 0, 0, '2021-09-26 15:40:56', '2021-09-26 15:40:56');
INSERT INTO `config_info_route` VALUES (135, 'recept', 'lb://datarecept', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/datarecept/**\"}}]', '[]', 100, '数据接收服务', 1, 1, '2021-11-15 15:30:35', '2021-11-15 15:30:35');
INSERT INTO `config_info_route` VALUES (136, 'wxLogin', 'lb://authorization/wxLogin', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/wxLogin/**\"}}]', '[]', 100, '微信小程序登录相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (139, 'yest', 'https://open.ys7.com/ezopen/h5/iframe_se?bSupporDoubleClickFull=0&url=ezopen://open.ys7.com/33011081992397348766:33010996991117288753/2.live&autoplay=1&audio=1&accessToken=at.3q1r2unb394lbiowac6g652jb9pvvv8q-8z5gzzv2sk-19rxxq9-rsnxm8sio&templete=0&id=myPlaye', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/test\"}}]', '[]', 100, NULL, 0, 1, '2022-03-14 15:58:11', '2022-03-14 15:58:11');
INSERT INTO `config_info_route` VALUES (140, 'loginProApp', 'lb://authorization', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/loginProApp/**\"}}]', '[]', 100, '用户登录相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (141, 'actuator', 'lb://admin', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/actuator*/**\"}}]', '[]', 100, '\nSpringboot actuator信息泄露', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (142, 'datatransmit', 'lb://datatransmit', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/bTransmit*/**\"}}]', '[]', 100, '数据推送服务', 0, 1, '2021-11-15 15:30:35', '2021-11-15 15:30:35');
INSERT INTO `config_info_route` VALUES (143, 'xxl-job-admin', 'lb://xxl-job-admin', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/xxl-job-admin/**,/static/**\"}}]', '[]', 100, 'job管理', 0, 1, '2021-11-15 15:30:35', '2021-11-15 15:30:35');
INSERT INTO `config_info_route` VALUES (145, 'dzdata', 'lb://dzdata', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/dzdata/**\"}}]', '[]', 100, '德州国土局', 1, 1, '2022-07-29 11:13:04', '2022-07-29 11:13:04');
INSERT INTO `config_info_route` VALUES (146, 'captcha-code', 'lb://authorization/getCaptcha', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/getCaptcha\"}}]', '[]', 100, '获取验证码', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (147, 'build-recept', 'lb://buildrecept', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/brecept/**\"}}]', '[{\"name\":\"StripPrefix\",\"args\":{\"_genkey_0\":\"1\"}}]', 100, '监管数据接收', 0, 1, '2022-07-29 11:13:04', '2022-07-29 11:13:04');
INSERT INTO `config_info_route` VALUES (148, 'yfamily', 'lb://yfamily', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/yfamily/**\"}}]', '[]', 100, '【益家人】应用', 1, 1, '2023-02-01 16:28:16', '2023-02-01 16:28:16');
INSERT INTO `config_info_route` VALUES (150, 'yfamily-wxLogin', 'lb://authorization/wxappLogin', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/wxappLogin/**\"}}]', '[]', 100, '【益家人】微信小程序登录', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint(64) UNSIGNED NOT NULL,
  `nid` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username`, `role`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of roles
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('ico', '$2a$10$KSB3v1d1Ywb2agT5zu6/b.7XXoPuxgH0kpkiF1jx9f0kaZiCZ1YIa', 1);
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
