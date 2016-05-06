package cn.net.sinodata.cm.hibernate.dao;

import org.springframework.stereotype.Repository;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;

@Repository
public class BatchLogDao extends GenericDao<BatchInfo>{

	public BatchLogDao() {
		super(BatchInfo.class);
	}

}
