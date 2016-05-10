/**
 * 
 */
package cn.net.sinodata.cm.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.net.sinodata.cm.common.EnumState;
import cn.net.sinodata.cm.common.GlobalVars;
import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;
import cn.net.sinodata.cm.hibernate.po.InvoiceInfo;
import cn.net.sinodata.cm.pb.ProtoBufInfo.EOperType;
import cn.net.sinodata.cm.service.BaseService;
import cn.net.sinodata.cm.service.IContentManagerService;
import cn.net.sinodata.cm.util.DateUtil;
import cn.net.sinodata.cm.util.Util;
import cn.net.sinodata.cm.util.ZipUtil;
import cn.net.sinodata.framework.exception.SinoException;

/**
 * @author manan
 * 
 */

@Service("manageService")
@Transactional(rollbackFor = Exception.class)
public class ContentManageServiceImpl extends BaseService implements IContentManagerService {
	protected final String SEPARATOR = File.separator;

	/**
	 * 获取批次信息
	 */
	@Override
	public BatchInfo getBatch(String batchId) throws Exception {
		BatchInfo batchInfo = batchDao.queryById(batchId);
		List<FileInfo> fileInfos = fileDao.queryListByBatchId(batchId);
		batchInfo.setFileInfos(fileInfos);
		
		contentService.getContent(batchInfo);
		
		batchInfo.setFileInfos(fileInfos);
		return batchInfo;
	}

	@Override
	public byte[] getContent(BatchInfo batchInfo) throws Exception {
		String batchId = batchInfo.getBatchId();
		String orgId = batchInfo.getOrgId();
		String sysId = batchInfo.getSysId();
		List<FileInfo> fileList = batchInfo.getFileInfos();
		Map<String, byte[]> fileBytes = new HashMap<String, byte[]>();
		for (FileInfo fileInfo : fileList) {
			String fileId = fileInfo.getFileId();
			String fileName = fileInfo.getFileName();
			String jcrPath = separator + sysId + separator + orgId + separator + batchId + separator + fileId;
			// JcrContent jcrContent = jcrService.getContent(jcrPath);
			// fileBytes.put(fileName, jcrContent.getData());
		}
		return ZipUtil.filesByte2Zip(fileBytes);
	}

	@Override
	public void deleteBatch(BatchInfo batch) {
		// TODO 删除批次
	}

	/**
	 * 提交批次信息和文件内容
	 */
	@Override
	public void submitBatchContent(BatchInfo batchInfo) throws Exception {
		List<FileInfo> fileInfos = batchInfo.getFileInfos();
		for (FileInfo fileInfo : fileInfos) {
			submitFile(batchInfo, fileInfo);
		}
		
		/*List<FileInfo> fileInfos = batchInfo.getFileInfos();

		//筛选出要删除的文件
		List<FileInfo> delFiles = new ArrayList<FileInfo>();
		for (FileInfo fileInfo : fileInfos) {
			if (fileInfo.getOperation() == EOperType.eDEL) {
				delFiles.add(fileInfo);
			}
		}
		fileInfos.removeAll(delFiles);

		batchDao.save(batchInfo);	//TODO 不要更新createTime creator等初信息始字段
		fileDao.save(fileInfos);
		fileDao.delete(delFiles);
		contentService.updContent(batchInfo, fileInfos);
		contentService.delContent(batchInfo, delFiles);*/
	}

	/**
	 * 校验发票信息，以后改成单张校验，不在提交时整体校验
	 */
	@Override
	public List<InvoiceInfo> checkInvoice(BatchInfo batchInfo) throws Exception {
		List<FileInfo> fileInfos = batchInfo.getFileInfos();
		List<String> invoiceIds= new ArrayList<String>();
		for (FileInfo fileInfo : fileInfos) {
			if(!Util.isStrEmpty(fileInfo.getInvoiceNo()))
				invoiceIds.add(fileInfo.getInvoiceNo());
		}

		/*List<String> invoiceIds = fileInfos.stream().filter(n -> Util.isStrEmpty(n.getInvoiceNo()))
										.map(n -> new String(n.getInvoiceNo()))
										.collect(Collectors.toList());*/
		if(Util.isListEmpty(invoiceIds)){
			return null;
		}
		return invoiceDao.queryListByIds(invoiceIds);
	}

	/**
	 * 提交批次信息，用于断点续传。只提交信息，不提交批次文件数据
	 */
	@Override
	public List<String> submitBatch(BatchInfo batchInfo) throws Exception {
		List<String> processingFileIds = null;
		String batchId = batchInfo.getBatchId();
		//查询已有批次信息
		BatchInfo orgiBatchInfo = batchDao.queryById(batchId);
		if(orgiBatchInfo == null){	//批次不存在，为新上传批次
			batchInfo.setState(EnumState.PROCESSING.ordinal());	//修改批次状态为处理中
			batchDao.save(batchInfo);
			
			List<FileInfo> fileInfos = batchInfo.getFileInfos();
			fileInfos.stream().forEach(fileInfo -> fileInfo.setState(EnumState.PROCESSING.ordinal()));	//修改文件状态为处理中
			fileDao.save(fileInfos);
			
			processingFileIds = fileInfos.stream().map(fileInfo -> fileInfo.getFileId()).collect(Collectors.toList());
		}else if(orgiBatchInfo.getState() == EnumState.PROCESSING.ordinal()){	//批次已存在, 上次提交未成功，需要继续处理剩余文件
			processingFileIds = fileDao.queryProcessingFileIds(batchId);	//查询未处理文件并返回客户端
		}else{	//批次已存在且上次提交成功，更新批次信息
			batchDao.evict(orgiBatchInfo);	//断开原对象连接，保存新对象
			
			batchInfo.setState(EnumState.PROCESSING.ordinal());	//修改批次状态为处理中
			batchDao.save(batchInfo);
			List<FileInfo> fileInfos = batchInfo.getFileInfos();
			fileInfos.stream().forEach(fileInfo -> fileInfo.setState(EnumState.PROCESSING.ordinal()));	//修改文件状态为处理中
			fileDao.save(fileInfos);
			
			processingFileIds = fileInfos.stream().map(fileInfo -> fileInfo.getFileId()).collect(Collectors.toList());
		}
		return processingFileIds;
	}

	/**
	 * 提交文件,用于断点续传
	 */
	@Override
	public void submitFile(BatchInfo batchInfo, FileInfo fileInfo) throws Exception {
		fileInfo.setState(EnumState.FINISH.ordinal());
		fileDao.save(fileInfo);
		if(fileInfo.getOperation() == EOperType.eDEL){
			contentService.delContent(batchInfo, fileInfo);
		}else if(fileInfo.getOperation() == EOperType.eUPDATEFILE){
			contentService.saveContent(batchInfo, fileInfo);
		}
	}

	/**
	 * 完成批次
	 */
	@Override
	public void finishBatch(String batchId) throws Exception {
		BatchInfo batchInfo = batchDao.queryById(batchId);
		batchInfo.setState(EnumState.FINISH.ordinal());
		batchDao.save(batchInfo);
	}

}
