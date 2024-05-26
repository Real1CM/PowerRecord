drop database if exists wave;
create database wave character set utf8mb4 collate utf8mb4_0900_ai_ci;

use wave;


drop table if exists user;
create table user (
    `id` bigint(12) auto_increment primary key comment '自增ID',
    `name` varchar(255) not null  comment '用户名',
    `password` varchar(255) not null comment '密码',
    `create_time` datetime not null comment '注册时间',
    `update_time` datetime not null  comment '最近登录时间',
    unique (`name`)
) char set utf8mb4 comment '用户信息表' auto_increment=1;


drop table if exists facility;
create table facility (
    `id` bigint(12) auto_increment primary key comment '自增ID',
    `name` varchar(255) not null comment '设备名称',
    `mac_address` varchar(255) comment '设备MAC地址',
    `create_time` datetime not null comment '添加时间',
    `update_time` datetime not null comment '修改时间',
    unique (`name`)
) char set utf8mb4 comment '设备信息表' auto_increment = 1;

drop table if exists facility_wave;
create table facility_wave (
    `id` bigint(12) auto_increment primary key comment '自增ID',
    `facility_id` bigint(12) not null comment '设备id',
    `wave_data_id` bigint(12) not null comment '波形id',
    `create_time` datetime not null comment '添加时间',
    `update_time` datetime not null comment '修改时间'
) char set utf8mb4 comment '设备波形关联表' auto_increment = 1;


drop table if exists wave_data;
create table wave_data (
    `id` bigint(12) auto_increment primary key comment '自增ID',
    `name` varchar(255) not null comment '波形数据名称',
    `xy_value` json  not null comment '坐标轴数据',
    `unit_value` int(3) not null default 1 comment '单位长度',
    `period` bigint(12) not null default 1 comment '周期',
    `create_time` datetime not null comment '添加时间',
    `update_time` datetime not null comment '修改时间'
)char set utf8mb4 comment '波形数据表' auto_increment = 1;