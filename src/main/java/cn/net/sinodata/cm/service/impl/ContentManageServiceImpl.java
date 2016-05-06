/**
 * 
 */
package cn.net.sinodata.cm.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.net.sinodata.cm.common.EnumBatchState;
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

	// @Override
	public BatchInfo _getBatch(BatchInfo batchInfo) throws Exception {
		String batchId = batchInfo.getBatchId();
		batchInfo = batchDao.queryById(batchId);
		if (batchInfo == null) {
			throw new SinoException("待获取批次" + batchId + "不存在！");
		}
		List<FileInfo> fileInfos = fileDao.queryListByBatchId(batchId);
		batchInfo.setFileInfos(fileInfos);
		return batchInfo;
	}

	/*@Override
	public BatchInfo getBatch(String batchId) throws Exception {
		// String batchId = batchInfo.getBatchId();
		BatchInfo batchInfo = batchDao.queryById(batchId);
		if (batchInfo == null) {
			throw new SinoException("待获取批次" + batchId + "不存在！");
		}
		List<FileInfo> fileInfos = fileDao.queryListByBatchId(batchId);
		batchInfo.setFileInfos(fileInfos);
		HBaseDao hbaseDao = new HBaseDao();
		// String path = buildPath(batchInfo);
		for (FileInfo fileInfo : fileInfos) {
			// Hbase处理，把二进制数据放到fileInfo中 返回到控件
			HQuery hquery = new HQuery();
			hquery.setColumFamily("F");
			hquery.setColumnName("content");
			hquery.setRowkey(fileInfo.getFileMd5());
			hquery.setTableName("tb_image1");
			HResult result = hbaseDao.queryByRowkey(hquery);
			// String fileId = fileInfo.getFileId();
			// FileUtil.byte2file(result.getValue(),path , fileId);
			fileInfo.setData(result.getValue());

		}

		// contentService.getContent(batchInfo);
		return batchInfo;
	}*/
	
	@Override
	public BatchInfo getBatch(String batchId) throws Exception {
		BatchInfo batchInfo = batchDao.queryById(batchId);
		List<FileInfo> fileInfos = fileDao.queryListByBatchId(batchId);
		for (FileInfo fileInfo : fileInfos) {
			fileInfo.setFileUrl(GlobalVars.download_url + buildRelaPath(batchInfo) + fileInfo.getFileName());
		}
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
		contentService.delContent(batchInfo, delFiles);
	}
	
	
	public void addBatchInfo(BatchInfo batchInfo) throws Exception {
		batchDao.save(batchInfo);
		contentService.saveContent(batchInfo);
		
		//TODO save invoice and notify
	}

	@Override
	public void upsertBatch(BatchInfo batchInfo) throws Exception {
		List<FileInfo> fileInfos = batchInfo.getFileInfos();

		// 将需要更新和删除的文件分开
		List<FileInfo> delFiles = new ArrayList<FileInfo>();
		for (FileInfo fileInfo : fileInfos) {
			if (fileInfo.getOperation() == EOperType.eDEL) {
				delFiles.add(fileInfo);
			}
		}
		fileInfos.removeAll(delFiles);

		batchDao.save(batchInfo);
		if (fileInfos.size() > 0) {
			fileDao.save(fileInfos);
			contentService.updContent(batchInfo, fileInfos);// Hbase处理，把二进制数据放到Hbase中
		}
		if (delFiles.size() > 0) {
			fileDao.delete(delFiles);
			contentService.delContent(batchInfo, delFiles);
		}
	}

	private String buildPath(BatchInfo batchInfo) {

		StringBuffer sb = new StringBuffer(GlobalVars.local_root_path);
		sb.append(SEPARATOR);
		String sid = batchInfo.getSysId();
		String oid = batchInfo.getOrgId();
		if (sid == null || "".equals(sid)) {
			sid = "1212";
		}
		if (oid == null || "".equals(oid)) {
			oid = "test";
		}
		sb.append(sid);
		sb.append(SEPARATOR);
		sb.append(DateUtil.format(batchInfo.getCreateTime(), GlobalVars.fs_date_format));
		sb.append(SEPARATOR);
		sb.append(oid);
		sb.append(SEPARATOR);
		sb.append(batchInfo.getBatchId());
		return sb.toString();
	}
	
	private String buildRelaPath(BatchInfo batchInfo) {
		StringBuffer sb = new StringBuffer(SEPARATOR);
		String sid = batchInfo.getSysId();
		String oid = batchInfo.getOrgId();
		if (sid == null || "".equals(sid)) {
			sid = "1212";
		}
		if (oid == null || "".equals(oid)) {
			oid = "test";
		}
		sb.append(sid);
		sb.append(SEPARATOR);
		sb.append(DateUtil.format(batchInfo.getCreateTime(), GlobalVars.fs_date_format));
		sb.append(SEPARATOR);
		sb.append(oid);
		sb.append(SEPARATOR);
		sb.append(batchInfo.getBatchId());
		sb.append(SEPARATOR);
		return sb.toString();
	}

	@Override
	public void addFile(BatchInfo batchInfo, FileInfo fileInfo) throws Exception {
		fileDao.save(fileInfo);
		contentService.updContent(batchInfo, fileInfo);
		batchInfo.updateFileState(fileInfo);
	}

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
	 * 提交批次信息
	 */
	@Override
	public List<FileInfo> submitBatch(BatchInfo batchInfo) throws Exception {
		List<FileInfo> uploadedfiles = null;
		String batchId = batchInfo.getBatchId();
		//查询已有批次信息
		BatchInfo orgiBatchInfo = batchDao.queryById(batchId);
		if(orgiBatchInfo == null){	//批次不存在，为新上传批次
			batchDao.save(batchInfo);
		}else if(orgiBatchInfo.getState() == EnumBatchState.SUSPEND.ordinal()){	//批次已存在, 上传中断状态，需要续传
			uploadedfiles = fileDao.queryListByBatchId(batchId);
		}else{	//批次已存在，需要更新批次信息
			batchDao.evict(orgiBatchInfo);	//断开原对象连接，保存新对象
			batchDao.save(batchInfo);
		}
		return uploadedfiles;
	}

	@Override
	public void addBatchWithoutData(BatchInfo batchInfo) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
