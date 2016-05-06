CREATE DATABASE image_new DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

DROP TABLE cm_batch_info;
CREATE TABLE cm_batch_info ( batchid varchar(50) NOT NULL, sysid varchar(20), orgid varchar(20), createtime timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL, lastmodified timestamp DEFAULT '0000-00-00 00:00:00' NULL, creator varchar(20), version varchar(10) DEFAULT '""', provincecode varchar(20), remark varchar(255), verify_result int, verify_remark varchar(255), PRIMARY KEY (batchid) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE cm_batch_log;
CREATE TABLE cm_batch_log ( batchid varchar(50), createtime timestamp NULL, opertime timestamp NULL, operation varchar(20), userid varchar(20), orgid varchar(20), logid varchar(50) DEFAULT '0' NOT NULL, invoice_no varchar(50), PRIMARY KEY (logid) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE cm_file_info;
CREATE TABLE cm_file_info ( fileid varchar(20) NOT NULL, batchid varchar(50) NOT NULL, filename varchar(100) NOT NULL, MD5 varchar(100), createtime timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL, creator varchar(20), category varchar(2), filesize bigint, verify_result int, verify_remark varchar(255), invoice_no varchar(20), PRIMARY KEY (batchid, filename) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE cm_file_log;
CREATE TABLE cm_file_log ( batchid varchar(20), fileid varchar(5), filename varchar(100), invoice_no varchar(20), operation varchar(20), opertime timestamp NULL, userid varchar(20), logid varchar(50) NOT NULL, PRIMARY KEY (logid) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE cm_invoice_info;
CREATE TABLE cm_invoice_info ( invoice_no varchar(20) NOT NULL, filename varchar(20), author varchar(20), createtime timestamp NULL, batchid varchar(20), PRIMARY KEY (invoice_no) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
