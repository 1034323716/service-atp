create schema ATPDB collate utf8_general_ci;

create table ATP_email
(
	emailAddress varchar(256) null,
	receiveState int null
)
engine=InnoDB;

create table ATP_log
(
	id bigint auto_increment comment 'id'
		primary key,
	taskResultId bigint null comment 'taskResultId',
	log text null,
	CreateTime timestamp default CURRENT_TIMESTAMP not null comment '插入时间'
)
engine=InnoDB;

create index ATP_log_taskResultId_index
	on ATP_log (taskResultId);

create table ATP_pkg
(
	id bigint auto_increment comment 'id'
		primary key,
	name varchar(256) not null comment '服务名称',
	version varchar(256) not null comment '运行的jar包版本',
	`desc` varchar(256) not null comment '服务描述',
	createTime timestamp default CURRENT_TIMESTAMP not null comment '插入时间',
	type varchar(256) not null comment '文件类型',
	userId bigint not null comment '用户ID',
	md5 varchar(256) null,
	configName text null comment 'testNo=主叫的手机号
			testNo=主叫的手机号'
)
engine=InnoDB;

create index ATP_pkg_userId_index
	on ATP_pkg (userId);

create table ATP_pkgCfg
(
	id bigint auto_increment comment '主键'
		primary key,
	name varchar(256) null comment '名字描述',
	properties text null comment '具体的配置信息
			testNo=13600000001
			testHost=10.10.220.105
			testNo1=13600000001
			testHost1=10.10.220.105',
	pkgId bigint null comment '外键，关联的入口',
	isDefault tinyint(1) null comment '是否Jar包原始配置',
	createTime timestamp default CURRENT_TIMESTAMP not null comment '插入时间'
)
comment '运行是的配置信息' engine=InnoDB;

create index ATP_pkgCfg_pkgId_index
	on ATP_pkgCfg (pkgId);

create table ATP_plan
(
	id bigint auto_increment comment 'ID'
		primary key,
	name varchar(256) default '' not null comment '名称',
	`desc` varchar(256) default '' not null comment '描述',
	createTime timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
	userId int not null comment '所属用户',
	cronExpression varchar(256) default '' not null comment 'cron表达式',
	taskId bigint null comment '任务id',
	state int not null comment '计划状态',
	isAlarm tinyint(1) default 1 not null
)
engine=InnoDB;

create index ATP_plan_taskId_index
	on ATP_plan (taskId);

create table ATP_tag
(
	id bigint auto_increment
		primary key,
	name varchar(256) not null comment '标签名，用于显示',
	type int null comment '类型可以改，"用例管理"的标签查询"ATP_pkg"这张表，"用例集管理"的标签查询"ATP_tcs"这张表"任务管理"的标签查询"ATP_task"这张表'
)
comment '标签，用于分类';

create table ATP_tag_relation
(
	id bigint auto_increment
		primary key,
	tagId bigint null comment '标签id',
	valueId bigint null comment 'ATP_pkg,ATP_tcs,ATP_task中唯一标示'
)
comment '标签与显示项的关系表';

create table ATP_task
(
	id bigint auto_increment comment 'ID'
		primary key,
	name varchar(256) default '' not null comment '名称',
	`desc` varchar(256) default '' not null comment '描述',
	createTime timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
	userId int not null comment '所属用户',
	summarys text null comment '<summarys>
				<summary type = 1 id = 4/>
				<summary type = 1 id = 5/>
		    <summary type = 0>    0：用例  1：用例集
				  <package id = "package主键" config = "config配置主键">
					  <entry id="entry用例主键"/>
					  <entry id="entry用例主键"/>
				  </summary>
			</summarys>',
	taskSetId int null
)
engine=InnoDB;

create index ATP_task_userId_index
	on ATP_task (userId);

create table ATP_taskResult
(
	id bigint auto_increment comment 'id'
		primary key,
	code varchar(256) null comment '运行结果返回码',
	`desc` varchar(256) null comment '运行结果描述',
	createTime datetime null comment '测试时间',
	finishTime datetime null comment '结束时间',
	taskId bigint not null comment '任务Id',
	planId bigint null comment '计划id',
	state int not null comment '这条task的运行状态 0：运行中  1：已完成',
	remark varchar(256) null comment '计划的备注'
)
engine=InnoDB;

create index ATP_taskResult_finishTime_taskId_state_index
	on ATP_taskResult (finishTime, taskId, state);

create table ATP_taskResultDetail
(
	id bigint auto_increment comment 'id'
		primary key,
	taskResultId bigint not null comment 'taskResultId',
	code varchar(256) null comment '运行结果返回码',
	`desc` varchar(256) null comment '运行结果描述',
	createTime datetime null comment '测试时间',
	finishTime datetime null comment '结束时间',
	record blob null comment '运行中间值保存(协议栈提供埋点接口)',
	tcsId bigint null comment '用例集Id',
	tcId bigint null comment '用例Id',
	state int not null comment '这条用例的运行状态 0：运行中  1：已完成'
)
engine=InnoDB;

create index ATP_taskResultDetail_taskResultId_index
	on ATP_taskResultDetail (taskResultId);

create table ATP_taskSet
(
	id int not null
		primary key,
	name varchar(255) null
)
comment '用于存放task' engine=InnoDB;

create table ATP_tc
(
	id bigint auto_increment comment 'ID'
		primary key,
	pkgId bigint not null comment 'ATP_package   id',
	name varchar(256) not null comment '测试用例名称',
	classPath varchar(256) not null comment '测试用例类路径',
	`desc` varchar(256) not null comment '描述',
	nickname varchar(256) not null comment '用例别名',
	state int default 1 null comment '1：可用：0：不可用'
)
engine=InnoDB;

create index ATP_tc_pkgId_index
	on ATP_tc (pkgId);

create table ATP_tcs
(
	id bigint auto_increment
		primary key,
	name varchar(256) default '' not null comment '名称,每一个环境一个用例集，每个用例集一个配置。命名时以 ‘_环境名’ 结尾',
	`desc` varchar(256) default '' not null comment '描述',
	createTime timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
	userId int not null comment '所属用户',
	summary text null comment '<summary>
				<package id = "package主键" config = "config配置主键">
					<entry id="entry用例主键"/>
					<entry id="entry用例主键"/>
				</package>
				<package id = "package主键" config = "config配置主键">
					<entry id="entry用例主键"/>
					<entry id="entry用例主键"/>
				</package>
			</summary>',
	state int default 1 null comment '1：可用：0：不可用'
)
engine=InnoDB;

create index ATP_tcs_userId_index
	on ATP_tcs (userId);

create table base_user
(
	id int auto_increment
		primary key,
	portal_id smallint(6) not null,
	name varchar(32) not null,
	password varchar(128) not null,
	salt varchar(16) not null,
	full_name varchar(16) not null,
	email varchar(255) null,
	phone varchar(16) null,
	gender tinyint default 1 null,
	avatar varchar(256) null,
	is_super tinyint default 0 not null,
	is_lock tinyint default 0 not null,
	token varchar(32) null,
	create_time timestamp default CURRENT_TIMESTAMP null,
	update_time timestamp null,
	constraint index_email
		unique (portal_id, email),
	constraint index_name
		unique (portal_id, name)
)
engine=InnoDB;

