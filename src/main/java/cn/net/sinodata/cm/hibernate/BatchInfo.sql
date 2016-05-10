DROP TABLE cm_batch_info;
CREATE TABLE cm_batch_info ( batchid varchar(50) NOT NULL, sysid varchar(20), orgid varchar(20), createtime timestamp NULL, lastmodified timestamp NULL, creator varchar(20), version varchar(10), remark varchar(255), verify_result int, verify_remark varchar(255), state int DEFAULT '0', last_operation varchar(20), PRIMARY KEY (batchid) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
