package cn.net.sinodata.cm.service;

import javax.annotation.Resource;

import cn.net.sinodata.cm.content.IContentService;
import cn.net.sinodata.cm.hibernate.dao.BatchDao;
import cn.net.sinodata.cm.hibernate.dao.FileDao;
import cn.net.sinodata.cm.hibernate.dao.InvoiceDao;
import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.log.CmLogger;
import cn.net.sinodata.cm.util.Util;
import cn.net.sinodata.framework.exception.SinoException;

public abstract class BaseService {
	
	@Resource
	protected FileDao fileDao;
	@Resource
	protected BatchDao batchDao;
	@Resource
	protected InvoiceDao invoiceDao;
//	@Resource
//	protected CmDao cmDao;
	@Resource
	protected IContentService contentService;
	
	protected CmLogger logger = CmLogger.getLogger(this.getClass());

	protected String separator = "/";
	
	protected String buildPath(String... str){
		StringBuffer sb = new StringBuffer(separator);
		for (int i = 0; i < str.length; i++) {
			sb.append(str[i]);
			if(i+1<str.length){
				sb.append(separator);
			}
		}
		return sb.toString();
	}
	
	protected void checkBatch(BatchInfo batchInfo) throws SinoException{
		StringBuffer sb = new StringBuffer("批次信息不完整，缺少");
		boolean flag = true;
		if(Util.isStrEmpty(batchInfo.getBatchId())){
			sb.append(" 批次号(BatchId) ");
			flag = false;
		}
		if(Util.isStrEmpty(batchInfo.getSysId())){
			sb.append(" 系统号(SysId) ");
			flag = false;
		}
		if(Util.isStrEmpty(batchInfo.getOrgId())){
			sb.append(" 机构号(OrgId) ");
			flag = false;
		}
		if(!flag){
			throw new SinoException(sb.toString());
		}
	}

}
