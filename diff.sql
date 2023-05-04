-- 客户端库中表：area_system_count，字段不一致,字段：area_code，修改语句如下：
ALTER TABLE area_system_count MODIFY COLUMN ;
-- 客户端库中表：area_system_count，字段不一致,字段：area_name，修改语句如下：
ALTER TABLE area_system_count MODIFY COLUMN ;
-- 客户端库中缺少表：bs_stamp_cfg，创建语句如下：
CREATE TABLE bs_stamp_cfg (
	id bigint(19) NOT NULL COMMENT '主键', 
	cfg_key varchar(50) COMMENT '配置的key', 
	cfg_value varchar(2000) COMMENT '配置值', 
	cfg_group varchar(50) COMMENT '配置分组', 
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', 
	version bigint(19) DEFAULT 0 COMMENT '版本控制使用', 
	remark varchar(45) COMMENT '备注', PRIMARY KEY (id), 
	UNIQUE INDEX cfg_key_UNIQUE (cfg_key), 
	UNIQUE INDEX id_UNIQUE (id), 
	UNIQUE INDEX PRIMARY (id)
);
-- 客户端库中缺少表：ps_interface_log，创建语句如下：
CREATE TABLE ps_interface_log (
	id bigint(19) NOT NULL, 
	system_ip varchar(200) COMMENT '系统ip', 
	system_name varchar(100) COMMENT '系统名称', 
	system_company varchar(100) COMMENT '系统所属公司', 
	interface_name varchar(50) COMMENT '接口名称', 
	operate_time varchar(100) COMMENT '操作耗时', 
	appkey varchar(100) COMMENT 'appkey', 
	create_time varchar(20) COMMENT '创建时间', 
	status varchar(20) COMMENT '成功状态', 
	operate_type varchar(50) COMMENT '操作状态', 
	result varchar(50) COMMENT '操作状态', PRIMARY KEY (id), 
	INDEX appkey (appkey), 
	INDEX create_time (create_time), 
	UNIQUE INDEX id (id), 
	UNIQUE INDEX PRIMARY (id), 
	INDEX system_name (system_name)
) COMMENT='对外接口操作日志';
-- 客户端库中缺少表：ps_user，创建语句如下：
CREATE TABLE ps_user (
	id bigint(20) NOT NULL, 
	site_id bigint(20) COMMENT '站点siteId', 
	position_dict_id bigint(19) DEFAULT 0 COMMENT '职务id', 
	user_name varchar(100), 
	user_account varchar(50) NOT NULL COMMENT '用户账号', 
	password varchar(200) DEFAULT 123456 COMMENT '使用印章时的密码默认123456', 
	status tinyint DEFAULT 0 COMMENT '用户状态 0:可用 1:停用', 
	is_deleted tinyint NOT NULL DEFAULT 0 COMMENT '删除标记 0：未删除 1：已删除', 
	is_manager tinyint COMMENT '是否后台用户（0：前台用户；1：后台用户）', 
	phone varchar(50), 
	email varchar(50) COMMENT '邮件', 
	operator_id bigint(19) COMMENT '操作人ID', 
	operator_name varchar(50) COMMENT '操作人姓名', 
	is_frozen tinyint DEFAULT 0 COMMENT '是否冻结 0未冻结 1已冻结', 
	cert_sn varchar(50) COMMENT '正数序列号', 
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间', 
	operate_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间', 
	remark varchar(100) COMMENT '备注', 
	session_id varchar(64), 
	id_number varchar(100) COMMENT '身份证号', 
	out_user_id varchar(100) NOT NULL COMMENT '外部用户唯一标识', 
	is_external tinyint DEFAULT 0 COMMENT '是否导入用户、0：默认，1：外部导入', 
	is_usb_key_user tinyint DEFAULT 0 COMMENT '是否是usbkey用户，0为false，1为true', 
	sign_cert varchar(2000) COMMENT '签名证书', 
	cert_sign_public_key varchar(200) COMMENT '证书签名公钥', 
	ca_sign_public_key varchar(200) COMMENT '颁发机构的证书公钥', 
	unit_name varchar(100) COMMENT '单位名称', 
	is_four_user tinyint DEFAULT 0 COMMENT '是否是usbkey用户，0为false，1为true', 
	device_id varchar(50) COMMENT '设备ID', 
	is_first tinyint DEFAULT 0 COMMENT '是否第一次登陆，0：第一次，1：不是第一次', 
	update_password_time varchar(50) COMMENT '管理员修改密码的时间,用于判断是否为初次登陆', 
	is_temporary tinyint, 
	sign_pass_word varchar(100) DEFAULT 0 COMMENT '签章密码', 
	id_number_type tinyint, 
	has_dept tinyint NOT NULL DEFAULT 0 COMMENT '用户是否有组织机构，1 有   0 没有， 默认  0', 
	sm3 varchar(200) COMMENT 'sm3摘要值', PRIMARY KEY (id), 
	INDEX idx_has_dept_delete (is_deleted,has_dept), 
	INDEX idx_user_name (user_name), 
	INDEX index_has_dept (has_dept), 
	INDEX is_deleted_index (is_deleted), 
	INDEX out_user_id_index (out_user_id), 
	UNIQUE INDEX PRIMARY (id), 
	INDEX ps_user (user_account)
) COMMENT='用户表';
