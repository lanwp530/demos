-----------------------------------------------------
-- Export file for user C##SCOTT@LOCALHOST         --
-- Created by Administrator on 2020/2/13, 20:31:44 --
-----------------------------------------------------

set define off
spool plsql_tables.log

prompt
prompt Creating table TEST
prompt ===================
prompt
create table TEST
(
  id          NUMBER not null,
  name        VARCHAR2(50) not null,
  birthday    DATE,
  create_time DATE default sysdate not null,
  update_time DATE
)
;
comment on column TEST.id
  is '主键';
comment on column TEST.name
  is '名称';
comment on column TEST.birthday
  is '生日';
comment on column TEST.create_time
  is '创建时间';
comment on column TEST.update_time
  is '修改时间';
alter table TEST
  add constraint PK_TEST primary key (ID);

prompt
prompt Creating table TEST1
prompt ====================
prompt
create table TEST1
(
  id          NUMBER not null,
  name        VARCHAR2(50) not null,
  birthday    DATE,
  create_time DATE default sysdate not null,
  update_time DATE
)
;
alter table TEST1
  add constraint PK_TEST1 primary key (ID);

prompt
prompt Creating sequence SEQ_TEST
prompt ==========================
prompt
create sequence SEQ_TEST
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;


spool off
