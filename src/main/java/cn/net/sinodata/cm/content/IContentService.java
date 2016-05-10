package cn.net.sinodata.cm.content;

import java.util.List;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;

public interface IContentService {

	public Object ensureFolder(final String path, final boolean createFolder) throws Exception;
	
	public void addContent(final BatchInfo batchInfo) throws Exception;
	
	public void saveContent(final BatchInfo batchInfo, final FileInfo fileInfo) throws Exception;
	
	public void delContent(final BatchInfo batchInfo, final FileInfo fileInfo) throws Exception;
	
	public Object getContent(final BatchInfo batchInfo) throws Exception;
	
	/**
	 * JCR注册节点用
	 */
	public void regist();

}
