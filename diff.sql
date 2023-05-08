-- 客户端库中缺少表：gomain_dept，创建语句如下：
CREATE TABLE IF NOT EXISTS gomain_dept (
	`dept_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '部门主键id' , 
	`parent_dept_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '上级部门id' , 
	`out_dept_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '外部组织机构标识' , 
	`out_parent_dept_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '外部组织机构上级标识' , 
	`dept_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '部门编号' , 
	`parent_dept_no_str` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '上级部门编码结构数' , 
	`dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '部门名称' , 
	`remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '备注' , 
	`address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '地址' , 
	`dept_level` int NOT NULL  DEFAULT '-1'  COMMENT '部门层级' , 
	`dept_type` tinyint NOT NULL  DEFAULT '1'  COMMENT '部门类型1：组织机构，2：部门' , 
	`area_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '地区id' , 
	`out_area_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT '0'  COMMENT '外部地区id' , 
	`status` tinyint NOT NULL  DEFAULT '0'  COMMENT '状态0:启用，1停用' , 
	`social_credit_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '社会统一信用代码' , 
	`is_external` tinyint NOT NULL  DEFAULT '0'  COMMENT '是否导入用户：0系统自建，后续是导入来源' , 
	`district_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '行政区划代码' , 
	`dept_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '机构编码' , 
	`dept_administrative_level` tinyint NOT NULL  DEFAULT '-1'  COMMENT '行政级别 1:省直属机构,2:市直属机构,3:区县直属机构,4:行政区域(省),5:行政区域(市),6:行政区域(区),7:行政区域(街道),8:其他部门' , 
	`operator_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '修改用户id' , 
	`operate_time` datetime NOT NULL  DEFAULT '1970-01-01 00:00:00'  on update CURRENT_TIMESTAMP  COMMENT '修改时间' , 
	`creator_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '创建用户id' , 
	`create_time` datetime NOT NULL  DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间' , 
	`is_deleted` tinyint NOT NULL  DEFAULT '0'  COMMENT '是否删除 0 未删除 1已删除' , 
	`link_man` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '联系人' , 
	`link_phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '联系人手机号' , 
	`administrative_division_province_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '行政区划省级代码' , 
	`administrative_division_city_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '行政区划市级代码' , 
	`administrative_division_area_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL  DEFAULT ''  COMMENT '行政区划区级代码' , PRIMARY KEY (dept_id), 
	INDEX `idx_dept_name` (`dept_name`) USING BTREE COMMENT '组织名称', 
	INDEX `idx_dept_type` (`dept_type`) USING BTREE COMMENT '部门类型', 
	INDEX `idx_out_dept_id` (`out_dept_id`) USING BTREE COMMENT '外部组织id', 
	INDEX `idx_out_parent_dept_id` (`out_parent_dept_id`) USING BTREE COMMENT '外部上级id', 
	INDEX `idx_parent_dept_id` (`parent_dept_id`) USING BTREE COMMENT '上级部门id', 
	INDEX `idx_parent_dept_no_str` (`parent_dept_no_str`) USING BTREE COMMENT '上级编号字符串', 
	INDEX `idx_social_credit_code` (`social_credit_code`) USING BTREE COMMENT '社会信用编码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='部门基础信息表'
-- 客户端库中表：gomain_stamp_cfg，字段不一致,字段：cfg_value，修改语句如下：
ALTER TABLE gomain_stamp_cfg MODIFY COLUMN 	`cfg_value` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci  DEFAULT ''  COMMENT '配置值' ;
-- 客户端库中表：gomain_stamp_cfg，字段不一致,字段：cfg_group，修改语句如下：
ALTER TABLE gomain_stamp_cfg MODIFY COLUMN 	`cfg_group` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci  DEFAULT ''  COMMENT '配置分组' ;
-- 客户端库中表：gomain_user，缺少字段：is_temporary，创建语句如下：
ALTER TABLE gomain_user ADD COLUMN 	`is_temporary` tinyint NOT NULL  DEFAULT '0'  COMMENT '是否临时用户 0 不是，1是' ;
-- 客户端库中表：gomain_user，缺少字段：user_platform，创建语句如下：
ALTER TABLE gomain_user ADD COLUMN 	`user_platform` tinyint NOT NULL  DEFAULT '0'  COMMENT '用户所属平台' ;
-- 客户端库中表：gomain_user，存在索引,列值为：email，重建索引：
ALTER TABLE gomain_user DROP INDEX `idx_email`;
ALTER TABLE gomain_user ADD   INDEX `idx_email` (`email`) USING BTREE COMMENT '邮箱';
-- 客户端库中表：gomain_user，缺少索引：idx_id_card，创建语句如下：
ALTER TABLE gomain_user ADD   INDEX `idx_id_card` (`id_card`) USING BTREE COMMENT '身份证';
-- 客户端库中表：gomain_user，存在索引,列值为：out_user_id，重建索引：
ALTER TABLE gomain_user DROP INDEX `idx_out_user_id`;
ALTER TABLE gomain_user ADD   INDEX `idx_out_user_id` (`out_user_id`) USING BTREE COMMENT '外部id';
-- 客户端库中表：gomain_user，修改表注释：
ALTER TABLE gomain_user COMMENT '';
