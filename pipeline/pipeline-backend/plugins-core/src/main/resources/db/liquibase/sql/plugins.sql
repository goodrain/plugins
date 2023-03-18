/*
 *
 * Copyright 2023 Talkweb Co., Ltd.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * /
 */

use pipeline;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for devops_app_service
-- ----------------------------
DROP TABLE IF EXISTS `devops_app_service`;
CREATE TABLE `devops_app_service`  (
  `id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'id主键',
  `gitlab_project_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '应用服务对应的gitlab项目id',
  `name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '应用服务名称',
  `code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '应用服务编码',
  `gitlab_code_url` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'gitlab仓库地址',
  `status` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '状态(ENABLE启用,DISABLE禁用)',
  `type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '项目类型',
  `team_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '团队标识',
  `team_code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '团队编码',
  `auto_build` tinyint(1) NULL DEFAULT NULL COMMENT '是否自动构建(0:否，1:是)',
  `service_type` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '服务类型',
  `pipeline_type` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '流水线类型',
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本',
  UNIQUE INDEX `idx_code`(`code`) USING BTREE,
  UNIQUE INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '应用服务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for devops_app_service_auto_deploy
-- ----------------------------
DROP TABLE IF EXISTS `devops_app_service_auto_deploy`;
CREATE TABLE `devops_app_service_auto_deploy`  (
  `id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'id主键',
  `app_service_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `sub_app_service_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `region_code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本'
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '应用服务成员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for devops_app_service_component
-- ----------------------------
DROP TABLE IF EXISTS `devops_app_service_component`;
CREATE TABLE `devops_app_service_component`  (
  `id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'id主键',
  `app_service_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '对应idaas的用户id',
  `sub_app_service_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '子应用服务id（可为空）',
  `paas_component_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '对应梧桐paas的组件id',
  `paas_component_code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本',
  `paas_app_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '对应梧桐paas的应用id',
  `region_code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '对应梧桐paas的集群编码',
  `team_code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '对应梧桐paas的团队编码'
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '应用服务成员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for devops_app_service_config
-- ----------------------------
DROP TABLE IF EXISTS `devops_app_service_config`;
CREATE TABLE `devops_app_service_config`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `app_service_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `token` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `version` int NULL DEFAULT NULL,
  `del_flag` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for devops_app_service_deploy_history
-- ----------------------------
DROP TABLE IF EXISTS `devops_app_service_deploy_history`;
CREATE TABLE `devops_app_service_deploy_history`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `app_service_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用服务id',
  `sub_app_service_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子应用服务Id',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `app_service_version` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用服务版本',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部署人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '部署时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标识',
  `version` int NULL DEFAULT NULL COMMENT '版本',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `deploy_info_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部署详情',
  `region_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `team_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `component_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for devops_app_service_pipeline
-- ----------------------------
DROP TABLE IF EXISTS `devops_app_service_pipeline`;
CREATE TABLE `devops_app_service_pipeline`  (
  `id` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'id主键',
  `pipeline_id` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '流水线id',
  `app_service_id` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '项目id',
  `status` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '状态(enable启用,disable禁用)',
  `gitlab_ref` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'gitlab分支',
  `sha` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '提交sha',
  `commit_content` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '提交内容',
  `commiter` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '提交人',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本',
  `duration` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '时长',
  UNIQUE INDEX `unique_app_pipeline`(`pipeline_id`, `app_service_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '应用服务的ci流水线表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for devops_app_service_pipeline_envs
-- ----------------------------
DROP TABLE IF EXISTS `devops_app_service_pipeline_envs`;
CREATE TABLE `devops_app_service_pipeline_envs`  (
  `id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键id',
  `app_service_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '应用服务id',
  `branch` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '应用服务分支',
  `module` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'maven多模块子模块',
  `env_key` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '变量key',
  `env_value` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '变量value',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '变量描述',
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '流水线执行环境变量表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for devops_app_service_pipeline_job
-- ----------------------------
DROP TABLE IF EXISTS `devops_app_service_pipeline_job`;
CREATE TABLE `devops_app_service_pipeline_job`  (
  `id` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'id主键',
  `app_service_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `pipeline_id` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '应用服务流水线id',
  `job_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'gitlab流水线任务id',
  `stage` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '任务阶段',
  `status` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '任务阶段状态',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  `name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '任务跳转地址',
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本'
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '应用服务的CI流水线任务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for devops_app_service_version
-- ----------------------------
DROP TABLE IF EXISTS `devops_app_service_version`;
CREATE TABLE `devops_app_service_version`  (
  `id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'id主键',
  `app_service_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '应用服务id',
  `app_service_version` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '应用服务版本',
  `committer` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '提交人',
  `commit_msg` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '提交信息',
  `commit_value` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '提交值',
  `commit_time` datetime NULL DEFAULT NULL COMMENT '提交时间',
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本',
  `image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '镜像',
  UNIQUE INDEX `idx_1`(`app_service_id`, `app_service_version`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '应用服务版本表（也就是镜像仓库的镜像版本）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for devops_custom_pipeline
-- ----------------------------
DROP TABLE IF EXISTS `devops_custom_pipeline`;
CREATE TABLE `devops_custom_pipeline`  (
  `id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键Id',
  `team_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '团队编码',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '流水线名称',
  `temp_id` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '流水线模板标识',
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '自定义流水线表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for devops_custom_pipeline_envs
-- ----------------------------
DROP TABLE IF EXISTS `devops_custom_pipeline_envs`;
CREATE TABLE `devops_custom_pipeline_envs`  (
  `id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键id',
  `pipeline_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '自定义流水线id',
  `env_key` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '变量key',
  `env_value` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '变量value',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '变量描述',
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '自定义流水线环境表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for devops_custom_pipeline_relation
-- ----------------------------
DROP TABLE IF EXISTS `devops_custom_pipeline_relation`;
CREATE TABLE `devops_custom_pipeline_relation`  (
  `id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键Id',
  `pipeline_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '流水线id',
  `app_service_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '应用服务id',
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '自定义流水线关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for devops_custom_pipeline_stage
-- ----------------------------
DROP TABLE IF EXISTS `devops_custom_pipeline_stage`;
CREATE TABLE `devops_custom_pipeline_stage`  (
  `id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键Id',
  `pipeline_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '自定义流水线id',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '流水线阶段名称',
  `code` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '流水线阶段编码',
  `stage_order` int NULL DEFAULT NULL COMMENT '流水线阶段顺序',
  `stage` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '流水线阶段json',
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '自定义流水线阶段表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for devops_custom_pipeline_yaml
-- ----------------------------
DROP TABLE IF EXISTS `devops_custom_pipeline_yaml`;
CREATE TABLE `devops_custom_pipeline_yaml`  (
  `id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '主键id',
  `pipeline_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '流水线id',
  `yaml` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT 'yaml',
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本'
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '自定义流水线表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for devops_gitlab_branch
-- ----------------------------
DROP TABLE IF EXISTS `devops_gitlab_branch`;
CREATE TABLE `devops_gitlab_branch`  (
  `id` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'id主键',
  `app_service_id` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '应用服务用id',
  `user_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `branch_name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '分支名',
  `origin_branch` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '来源分支',
  `issue_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '禅道问题id',
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本',
  `commit_user_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '最新提交用户',
  `commit_user_name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '最新提交用户名',
  `commit_msg` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '最新提交内容',
  `commit_date` datetime NULL DEFAULT NULL COMMENT '最新提交容器',
  `commit_sha` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '最新提交sha'
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = 'gitlab的commit表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for devops_gitlab_commit
-- ----------------------------
DROP TABLE IF EXISTS `devops_gitlab_commit`;
CREATE TABLE `devops_gitlab_commit`  (
  `id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'id主键',
  `app_service_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '应用服务用id',
  `user_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'gitlab用户id',
  `commit_sha` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'commit_sha',
  `commit_content` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '提交内容',
  `gitlab_ref` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'gitlab分支',
  `commit_date` timestamp NULL DEFAULT NULL COMMENT '提交日期',
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本'
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = 'gitlab的commit表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for devops_sub_app_service
-- ----------------------------
DROP TABLE IF EXISTS `devops_sub_app_service`;
CREATE TABLE `devops_sub_app_service`  (
  `id` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'id主键',
  `app_service_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '应用服务id',
  `code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '应用服务code',
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '应用子服务表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for devops_custom_pipeline_temp
-- ----------------------------
DROP TABLE IF EXISTS `devops_custom_pipeline_temp`;
CREATE TABLE `devops_custom_pipeline_temp`  (
  `id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键id',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '模板描述',
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '自定义流水线模板表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of devops_custom_pipeline_temp
-- ----------------------------
INSERT INTO `devops_custom_pipeline_temp` VALUES ('1_node_temp_stage', 'Node.js', 'Node.js', '23975', '2022-08-10 15:18:16', '23975', '2022-08-10 15:18:21', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp` VALUES ('2_maven_single_temp_stage', 'Maven单模块', 'Java Maven 单模块', '23975', '2022-08-10 15:16:57', '23975', '2022-08-10 15:17:03', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp` VALUES ('3_maven_multi_temp_stage', 'Maven多模块', 'Java Maven 多模块', '23975', '2022-08-09 08:32:26', '23975', '2022-08-24 08:32:32', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp` VALUES ('6_war_temp_stage', 'War', 'Java War', '23975', '2022-09-21 09:18:44', '23975', '2022-09-21 09:18:50', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp` VALUES ('7_gradle_temp_stage', 'Gradle', 'Gradle', '23975', '2022-11-11 09:51:53', '23975', '2022-11-11 09:52:00', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp` VALUES ('4_golang_temp_stage', 'Go', 'Golang', '23975', '2022-12-13 14:18:28', '23975', '2022-12-13 14:18:33', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp` VALUES ('5_python_temp_stage', 'Python', 'Python', '23975', '2022-12-13 14:38:27', '23975', '2022-12-13 14:38:33', NULL, NULL);

-- ----------------------------
-- Table structure for devops_custom_pipeline_temp_stage
-- ----------------------------
DROP TABLE IF EXISTS `devops_custom_pipeline_temp_stage`;
CREATE TABLE `devops_custom_pipeline_temp_stage`  (
  `id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键Id',
  `template_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '流水线模板id',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '阶段名称',
  `code` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '阶段编码',
  `stage_order` int NULL DEFAULT NULL COMMENT '阶段顺序',
  `stage` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '阶段json',
  `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否删除',
  `version` int NULL DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '自定义流水线模板阶段表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of devops_custom_pipeline_temp_stage
-- ----------------------------

-- Java Maven Single module
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41a4863931c9e71237f3', '2_maven_single_temp_stage', '版本生成', 'version', 4, '{\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/curl:7.87.0\",\r\n	\"script\": [\"http_status_code=`curl -o /dev/null -s -m 10 --connect-timeout 10 -w %{http_code} \\\"${GATEWAY}/hook/ci?gitlabProjectId=${CI_PROJECT_ID}&version=${DEVOPS_VERSION}&commit=${CI_COMMIT_SHA}&branch=${CI_COMMIT_REF_SLUG}&module=${SUB_SERVICE}&image=${IMAGE}\\\"`\\n      if [ \\\"$http_status_code\\\" != \\\"200\\\" ]; then\\n      echo $http_status_code\\n      echo \\\"上传应用服务版本失败\\\"\\n      exit 1\\n      fi\"],\r\n	\"stage\": \"version\"\r\n}', NULL, '2022-07-01 10:40:13', NULL, '2022-07-01 10:40:17', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41d4863931c9e71237f1', '2_maven_single_temp_stage', '代码扫描', 'sonar', 1, '{\n  \"artifacts\": {\n    \"paths\": []\n  },\n  \"cache\": {\n    \"paths\": []\n  },\n  \"except\": {\n    \"refs\": [],\n    \"variables\": []\n  },\n  \"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/maven:3.6.3-openjdk-8\",\n  \"only\": {\n    \"refs\": [],\n    \"variables\": []\n  },\n  \"script\": [\n    \"mvn sonar:sonar -Dsonar.projectKey=${SONAR_KEY} -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_TOKEN}\"\n  ],\n  \"stage\": \"sonar\"\n}\n', 'admin', '2022-08-16 15:40:24', 'admin', '2022-08-16 15:40:28', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41d4863931c9e71237f2', '2_maven_single_temp_stage', '编译构建物', 'compile', 2, '{\r\n	\"artifacts\": {\r\n		\"paths\": [\"target/*.jar\"]\r\n	},\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/maven:3.6.3-openjdk-8\",\r\n	\"script\": [\"mvn clean -U package -DskipTests=true\"],\r\n	\"stage\": \"compile\"\r\n}', 'admin', '2022-08-16 15:39:45', 'admin', '2022-08-16 15:39:51', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41d4863931c9e71237f4', '2_maven_single_temp_stage', '代码扫描', 'sonar', 1, '{\n  \"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/maven:3.6.3-openjdk-8\",\n  \"script\": [\n    \"mvn sonar:sonar -Dsonar.projectKey=${SONAR_KEY} -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_TOKEN}\"\n  ],\n  \"stage\": \"sonar\"\n}', 'admin', '2022-08-16 15:40:24', 'admin', '2022-08-16 15:40:28', NULL, NULL);

-- Java Maven Multi module
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41c4863931c9e71237f3', '3_maven_multi_temp_stage', '版本生成', 'version', 4, '{\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/curl:7.87.0\",\r\n	\"script\": [\"http_status_code=`curl -o /dev/null -s -m 10 --connect-timeout 10 -w %{http_code} \\\"${GATEWAY}/hook/ci?gitlabProjectId=${CI_PROJECT_ID}&version=${DEVOPS_VERSION}&commit=${CI_COMMIT_SHA}&branch=${CI_COMMIT_REF_SLUG}&module=${SUB_SERVICE}&image=${IMAGE}\\\"`\\n      if [ \\\"$http_status_code\\\" != \\\"200\\\" ]; then\\n      echo $http_status_code\\n      echo \\\"上传应用服务版本失败\\\"\\n      exit 1\\n      fi\"],\r\n	\"stage\": \"version\"\r\n}', NULL, '2022-07-01 10:40:13', NULL, '2022-07-01 10:40:17', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41d4863931c9e71237f8', '3_maven_multi_temp_stage', '编译构建物', 'compile', 2, '{\r\n	\"artifacts\": {\r\n		\"paths\": [\"${package_path}/target/${jar_name}.jar\"]\r\n	},\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/maven:3.6.3-openjdk-8\",\r\n	\"script\": [\"mvn clean -U package -pl ${package_path} -am -DskipTests=true\"],\r\n	\"stage\": \"compile\"\r\n}', 'admin', '2022-08-24 08:37:17', 'admin', '2022-08-24 08:37:23', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41d4863931c9e71237f9', '3_maven_multi_temp_stage', '构建镜像', 'build-image', 3, '{\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/docker:20.10.9\",\r\n	\"script\": [\"cp ${package_path}/target/${jar_name}.jar app.jar\", \"docker login -u ${REPOSITORY_USERNAME} -p ${REPOSITORY_PASSWORD} ${REPOSITORY_URL}\", \"docker build -t  ${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION} .\", \"docker push ${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION}\", \"export IMAGE=${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION}\"],\r\n	\"stage\": \"build-image\"\r\n}', 'admin', '2022-08-24 08:39:05', 'admin', '2022-08-24 08:39:11', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41c4863931c9e71237f1', '3_maven_multi_temp_stage', '代码扫描', 'sonar', 1, '{\n  \"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/maven:3.6.3-openjdk-8\",\n  \"script\": [\n    \"mvn sonar:sonar -Dsonar.projectKey=${SONAR_KEY} -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_TOKEN}\"\n  ],\n  \"stage\": \"sonar\"\n}', 'admin', '2022-08-16 15:40:24', 'admin', '2022-08-16 15:40:28', NULL, NULL);

-- Python
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('b6b5010845ee41d4863931c9e71237g5', '5_python_temp_stage', '编译构建物', 'compile', 2, '{\r\n	\"image\": \"python:3.9\",\r\n	\"stage\": \"compile\",\r\n	\"script\": [\"pip install --upgrade pip  -i https://pypi.tuna.tsinghua.edu.cn/simple/\", \"pip install pyinstaller -i https://pypi.tuna.tsinghua.edu.cn/simple/\", \"pip install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple/\", \"pyinstaller -F app.py\"],\r\n	\"artifacts\": {\r\n		\"paths\": [\"dist/app\"]\r\n	},\r\n	\"cache\": {\r\n		\"paths\": [\".cache/pip\"]\r\n	}\r\n}', 'admin', '2022-12-13 14:42:44', 'admin', '2022-12-13 14:42:49', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('b6b5010845ee41d4863931c9e71237g6', '5_python_temp_stage', '构建镜像', 'build-image', 3, '{\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/docker:20.10.9\",\r\n	\"script\": [\"docker login -u ${REPOSITORY_USERNAME} -p ${REPOSITORY_PASSWORD} ${REPOSITORY_URL}\", \"docker build -t  ${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION} .\", \"docker push ${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION}\", \r\n\"export IMAGE=${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION}\"],\r\n	\"stage\": \"build-image\"\r\n}', 'admin', '2022-12-13 14:39:23', 'admin', '2022-12-13 14:39:28', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41h4863931c9e71237f3', '5_python_temp_stage', '版本生成', 'version', 4, '{\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/curl:7.87.0\",\r\n	\"script\": [\"http_status_code=`curl -o /dev/null -s -m 10 --connect-timeout 10 -w %{http_code} \\\"${GATEWAY}/hook/ci?gitlabProjectId=${CI_PROJECT_ID}&version=${DEVOPS_VERSION}&commit=${CI_COMMIT_SHA}&branch=${CI_COMMIT_REF_SLUG}&module=${SUB_SERVICE}&image=${IMAGE}\\\"`\\n      if [ \\\"$http_status_code\\\" != \\\"200\\\" ]; then\\n      echo $http_status_code\\n      echo \\\"上传应用服务版本失败\\\"\\n      exit 1\\n      fi\"],\r\n	\"stage\": \"version\"\r\n}', NULL, '2022-07-01 10:40:13', NULL, '2022-07-01 10:40:17', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41h4863931c9e71237f1', '5_python_temp_stage', '代码扫描', 'sonar', 1, '{\n  \"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/sonar-scanner-cli:4\",\n  \"script\": [\n    \"sonar-scanner -Dsonar.projectKey=${SONAR_KEY} -Dsonar.sources=. -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_TOKEN}\"\n  ],\n  \"stage\": \"sonar\"\n}\n', 'admin', '2022-08-16 15:40:24', 'admin', '2022-08-16 15:40:28', NULL, NULL);

-- NodeJS
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41b4863931c9e71237f3', '1_node_temp_stage', '版本生成', 'version', 4, '{\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/curl:7.87.0\",\r\n	\"script\": [\"http_status_code=`curl -o /dev/null -s -m 10 --connect-timeout 10 -w %{http_code} \\\"${GATEWAY}/hook/ci?gitlabProjectId=${CI_PROJECT_ID}&version=${DEVOPS_VERSION}&commit=${CI_COMMIT_SHA}&branch=${CI_COMMIT_REF_SLUG}&module=${SUB_SERVICE}&image=${IMAGE}\\\"`\\n      if [ \\\"$http_status_code\\\" != \\\"200\\\" ]; then\\n      echo $http_status_code\\n      echo \\\"上传应用服务版本失败\\\"\\n      exit 1\\n      fi\"],\r\n	\"stage\": \"version\"\r\n}', NULL, '2022-07-01 10:40:13', NULL, '2022-07-01 10:40:17', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41d4863931c9e71237f5', '1_node_temp_stage', '编译构建物', 'compile', 2, '{\r\n	\"artifacts\": {\r\n		\"paths\": [\"dist\"]\r\n	},\r\n	\"cache\": {\r\n		\"paths\": [\"node_modules\"]\r\n	},\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/node:16.14.2\",\r\n	\"script\": [\"yarn config set registry https://registry.npm.taobao.org\", \"yarn config set disturl https://npm.taobao.org/dist\", \"yarn config set electron_mirror https://npm.taobao.org/mirrors/electron/\", \"yarn install\", \"yarn run build\"],\r\n	\"stage\": \"compile\"\r\n}', 'admin', '2022-08-16 15:56:10', 'admin', '2022-08-16 15:56:17', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41d4863931c9e71237f6', '1_node_temp_stage', '构建镜像', 'build-image', 3, '{\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/docker:20.10.9\",\r\n	\"script\": [\"docker login -u ${REPOSITORY_USERNAME} -p ${REPOSITORY_PASSWORD} ${REPOSITORY_URL}\", \"docker build -t  ${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION} .\", \"docker push ${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION}\",\"export IMAGE=${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION}\"],\r\n	\"stage\": \"build-image\"\r\n}', 'admin', '2022-08-16 15:56:51', 'admin', '2022-08-16 15:56:56', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41d4863931c9e71237ff', '1_node_temp_stage', '代码扫描', 'sonar', 1, '{\n  \"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/sonar-scanner-cli:4\",\n  \"script\": [\n    \"sonar-scanner -Dsonar.projectKey=${SONAR_KEY} -Dsonar.sources=. -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_TOKEN}\"\n  ],\n  \"stage\": \"sonar\"\n}\n', 'admin', '2022-08-16 15:40:24', 'admin', '2022-08-16 15:40:28', NULL, NULL);

-- Java War
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41d4863931c9e71237g5', '6_war_temp_stage', '编译构建物', 'compile', 1, '{\r\n	\"artifacts\": {\r\n		\"paths\": [\"app.war\"]\r\n	},\r\n	\"image\": \"lukaszimmermann/apache-ant:8-1.9.6\",\r\n	\"script\": [\"ant\"],\r\n	\"stage\": \"compile\"\r\n}', 'admin', '2022-09-01 15:39:45', 'admin', '2022-09-01 15:39:51', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41d4863931c9e71237g6', '6_war_temp_stage', '构建镜像', 'build-image', 2, '{\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/docker:20.10.9\",\r\n	\"script\": [\"docker login -u ${REPOSITORY_USERNAME} -p ${REPOSITORY_PASSWORD} ${REPOSITORY_URL}\", \"docker build -t  ${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION} .\", \"docker push ${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION}\",\r\n		\"export IMAGE=${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION}\"\r\n	],\r\n	\"stage\": \"build-image\"\r\n}', 'admin', '2022-09-01 15:40:24', 'admin', '2022-09-01 15:40:28', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41e4863931c9e71237f3', '6_war_temp_stage', '版本生成', 'version', 3, '{\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/curl:7.87.0\",\r\n	\"script\": [\"http_status_code=`curl -o /dev/null -s -m 10 --connect-timeout 10 -w %{http_code} \\\"${GATEWAY}/hook/ci?gitlabProjectId=${CI_PROJECT_ID}&version=${DEVOPS_VERSION}&commit=${CI_COMMIT_SHA}&branch=${CI_COMMIT_REF_SLUG}&module=${SUB_SERVICE}&image=${IMAGE}\\\"`\\n      if [ \\\"$http_status_code\\\" != \\\"200\\\" ]; then\\n      echo $http_status_code\\n      echo \\\"上传应用服务版本失败\\\"\\n      exit 1\\n      fi\"],\r\n	\"stage\": \"version\"\r\n}', NULL, '2022-07-01 10:40:13', NULL, '2022-07-01 10:40:17', NULL, NULL);

-- Gradle
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41f4863931c9e71237f3', '7_gradle_temp_stage', '版本生成', 'version', 3, '{\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/curl:7.87.0\",\r\n	\"script\": [\"http_status_code=`curl -o /dev/null -s -m 10 --connect-timeout 10 -w %{http_code} \\\"${GATEWAY}/hook/ci?gitlabProjectId=${CI_PROJECT_ID}&version=${DEVOPS_VERSION}&commit=${CI_COMMIT_SHA}&branch=${CI_COMMIT_REF_SLUG}&module=${SUB_SERVICE}&image=${IMAGE}\\\"`\\n      if [ \\\"$http_status_code\\\" != \\\"200\\\" ]; then\\n      echo $http_status_code\\n      echo \\\"上传应用服务版本失败\\\"\\n      exit 1\\n      fi\"],\r\n	\"stage\": \"version\"\r\n}', NULL, '2022-07-01 10:40:13', NULL, '2022-07-01 10:40:17', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('g6b5010845ee41d4863931c9e71237g5', '7_gradle_temp_stage', '构建镜像', 'build-image', 2, '{\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/docker:20.10.9\",\r\n	\"script\": [\"docker login -u ${REPOSITORY_USERNAME} -p ${REPOSITORY_PASSWORD} ${REPOSITORY_URL}\", \"docker build -t  ${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION} .\", \"docker push ${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION}\"],\r\n	\"stage\": \"build-image\"\r\n}', 'admin', '2022-11-11 10:07:11', 'admin', '2022-11-11 10:07:18', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('g6b5010845ee41d4863931c9e71237g6', '7_gradle_temp_stage', '编译构建物', 'compile', 1, '{\r\n	\"artifacts\": {\r\n		\"paths\": [\"build/libs/app.jar\"]\r\n	},\r\n	\"image\": \"swr.cn-north-4.myhuaweicloud.com/wutong/gradle-ci:jdk17\",\r\n	\"script\": [\"GRADLE_USER_HOME=\'$(pwd)/.gradle\'\", \"export GRADLE_USER_HOME\", \"chmod 755 ./gradlew \", \"./gradlew build --build-cache -x test \"],\r\n	\"stage\": \"compile\"\r\n}', 'admin', '2022-11-11 09:54:55', 'admin', '2022-11-11 09:55:00', NULL, NULL);

-- Golang
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('f6b5010845ee41g4863931c9e71237f3', '4_golang_temp_stage', '版本生成', 'version', 4, '{\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/curl:7.87.0\",\r\n	\"script\": [\"http_status_code=`curl -o /dev/null -s -m 10 --connect-timeout 10 -w %{http_code} \\\"${GATEWAY}/hook/ci?gitlabProjectId=${CI_PROJECT_ID}&version=${DEVOPS_VERSION}&commit=${CI_COMMIT_SHA}&branch=${CI_COMMIT_REF_SLUG}&module=${SUB_SERVICE}&image=${IMAGE}\\\"`\\n      if [ \\\"$http_status_code\\\" != \\\"200\\\" ]; then\\n      echo $http_status_code\\n      echo \\\"上传应用服务版本失败\\\"\\n      exit 1\\n      fi\"],\r\n	\"stage\": \"version\"\r\n}', NULL, '2022-07-01 10:40:13', NULL, '2022-07-01 10:40:17', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('t6b5010845ee41d4863931c9e71237g5', '4_golang_temp_stage', '编译构建物', 'compile', 2, '{\r\n	\"image\": \"golang:latest\",\r\n	\"stage\": \"compile\",\r\n	\"script\": [\"mkdir -p .go\", \"go env -w GO111MODULE=on\", \"go env -w GOPROXY=\\\"https://goproxy.io,direct\\\"\", \"CGO_ENABLED=0 GOOS=linux go build -o app  main.go\"],\r\n	\"artifacts\": {\r\n		\"paths\": [\"app\"]\r\n	},\r\n	\"cache\": {\r\n		\"paths\": [\".go/pkg/mod/\"]\r\n	}\r\n}', 'admin', '2022-12-14 14:37:50', 'admin', '2022-12-12 14:37:55', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('t6b5010845ee41d4863931c9e71237g6', '4_golang_temp_stage', '构建镜像', 'build-image', 3, '{\r\n	\"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/docker:20.10.9\",\r\n	\"script\": [\"docker login -u ${REPOSITORY_USERNAME} -p ${REPOSITORY_PASSWORD} ${REPOSITORY_URL}\", \"docker build -t  ${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION} .\", \"docker push ${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION}\", \"export IMAGE=${REPOSITORY_URL}/${ORG}/${MODULE}:${DEVOPS_VERSION}\"],\r\n	\"stage\": \"build-image\"\r\n}', 'admin', '2022-12-13 14:33:19', 'admin', '2022-12-13 14:33:24', NULL, NULL);
INSERT INTO `devops_custom_pipeline_temp_stage` (`id`, `template_id`, `name`, `code`, `stage_order`, `stage`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `version`) VALUES ('t6b5010845ee41d4863931c9e71237g1', '4_golang_temp_stage', '代码扫描', 'sonar', 1, '{\n  \"image\": \"registry.cn-hangzhou.aliyuncs.com/goodrain/sonar-scanner-cli:4\",\n  \"script\": [\n    \"sonar-scanner -Dsonar.projectKey=${SONAR_KEY} -Dsonar.sources=. -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_TOKEN}\"\n  ],\n  \"stage\": \"sonar\"\n}\n', 'admin', '2022-08-16 15:40:24', 'admin', '2022-08-16 15:40:28', NULL, NULL);


SET FOREIGN_KEY_CHECKS = 1;
