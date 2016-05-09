package cn.net.sinodata.cm.content;

import java.util.List;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;

public interface IContentService {

	public Object ensureFolder(final String path, final boolean createFolder);
	
	public void addContent(final BatchInfo batchInfo) throws Exception;
	
	public void saveContent(final BatchInfo batchInfo) throws Exception;
	
	public void saveContent(final BatchInfo batchInfo, final FileInfo fileInfo) throws Exception;
	
	public void updContent(final BatchInfo batchInfo, final List<FileInfo> files) throws Exception;
	
	public void delContent(final BatchInfo batchInfo, final List<FileInfo> delFiles) throws Exception;
	
//	public void addContent(final String path, final BaseContent content);
	
//	public JcrContent getContent(final String path);
	
	public Object getContent(final BatchInfo batchInfo) throws Exception;
	
	public void modifyContentFile(final BatchInfo batchInfo, final FileInfo fileInfo) throws Exception;
//	public boolean isNodeExist(String path);
	/**
	 * JCR注册节点用
	 */
	public void regist();

	/**
	 * 更新文件内容
	 * @param batchInfo
	 * @param fileInfo
	 * @throws Exception
	 */
	void updContent(BatchInfo batchInfo, FileInfo fileInfo) throws Exception;
}
