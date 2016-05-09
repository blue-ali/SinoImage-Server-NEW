/**
 * 
 */
package cn.net.sinodata.cm.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;
import cn.net.sinodata.cm.pb.ProtoBufInfo.EResultStatus;
import cn.net.sinodata.cm.pb.ProtoBufInfo.MsgBatchInfo;
import cn.net.sinodata.cm.pb.ProtoBufInfo.MsgFileInfo;
import cn.net.sinodata.cm.pb.bean.ResultInfo;
import cn.net.sinodata.cm.util.OpeMetaFileUtils;

/**
 * @author manan
 *
 */
@Controller
@SuppressWarnings("serial")
public class SubmitBrokeFileService extends BaseServletService {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 指定在内存中缓存数据大小,单位为byte,这里设为1Mb
		factory.setSizeThreshold(1024 * 1024);
		// 设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录
		factory.setRepository(new File("D:\\temp"));
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 指定单个上传文件的最大尺寸,单位:字节，这里设为50Mb
		upload.setFileSizeMax(50 * 1024 * 1024);
		// 指定一次上传多个文件的总尺寸,单位:字节，这里设为50Mb
		upload.setSizeMax(100 * 1024 * 1024);
		upload.setHeaderEncoding("UTF-8");

		List<FileItem> items = null;
		try {
			// 解析request请求
			items = upload.parseRequest(request);
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = iter.next();
				if (item != null) {
					// 表单内容，ignore
					if (!item.isFormField()) {
						logger.info("处理传送文件");
						processFromUpload(item, response);
					}
				} else {
					getResult().setStatus(EResultStatus.eFailed);
					getResult().setMsg("解析请求失败，请求中不包含任何对象");
				}

			}
		} catch (Exception e) {
			getResult().setStatus(EResultStatus.eFailed);
			getResult().setMsg("提交批次失败: " + e.getMessage());
			logger.error(e);
		} finally {
			response.setCharacterEncoding("UTF-8");
			getResult().toNetMsg().writeTo(response.getOutputStream());
		}
	}

	private void processFromUpload(FileItem item, HttpServletResponse hsr) throws Exception {
		String fname = item.getName();

		// fname = fname.substring(fname.lastIndexOf("\\") + 1, fname.length());

		if (fname.endsWith(".pb")) { // 处理批次信息
			// pb对象转换为po对象
			MsgFileInfo mFileInfo = MsgFileInfo.parseFrom(item.getInputStream());
			FileInfo fileInfo = FileInfo.FromPBMsg(mFileInfo);
			logger.info("获得批次元数据信息, BatchId:[" + fileInfo.getBatchId() + "], FileId:[" + fileInfo.getFileId() + "]");

//			manageService.submitFile(fileInfo); // 提交批次信息和内容
			getResult().setStatus(EResultStatus.eSuccess);
		} else {
			// 上传数据内容不对
			throw new Exception("上传数据内容的扩展名非" + OpeMetaFileUtils.PBDataExt + "或者" + OpeMetaFileUtils.PBOPEEXT + "服务拒绝");
		}
	}

	/*
	 * private NResultInfo ProcessBatchData(FileItem item) throws IOException {
	 * NResultInfo nresult = new NResultInfo();
	 * 
	 * TigEraLogger.Log("上传.0.获得元数据");
	 * 
	 * MsgBatchInfo msgbatch = MsgBatchInfo.parseFrom(item.getInputStream());
	 * NBatchInfo nbatch = NBatchInfo.FromNetMsg(msgbatch);
	 * 
	 * if (nbatch.IsFileDataComplete()) { TigEraLogger.Log("上传.1.批次采用单文件");
	 * 
	 * nresult = BatchOpeFactory.GetBatchOpe().ProcessBatch(nbatch);
	 * 
	 * } else { TigEraLogger.Log("上传.2.1. 批次采分文件");
	 * 
	 * /////// // 创建目录信息等。
	 * BatchOpeFactory.GetBatchOpe().ProcessBatchHead(nbatch);
	 * _batchinfocache.put(nbatch.getBatchNO(), nbatch);// 暂时先缓存下来 } return
	 * nresult; }
	 * 
	 * private NResultInfo ProcessBatchFileData(FileItem item) throws
	 * IOException {
	 * 
	 * NResultInfo nresult = new NResultInfo();
	 * TigEraLogger.Log("上传。2.2.获取文件数据");
	 * 
	 * MsgFileInfo mfileinfo = MsgFileInfo.parseFrom(item.getInputStream()); if
	 * (_batchinfocache.containsKey(mfileinfo.getBatchNO13())) { NFileInfo
	 * nfileinfo = NFileInfo.FromPBMsg(mfileinfo);
	 * 
	 * NBatchInfo nbatch = _batchinfocache.get(mfileinfo.getBatchNO13());
	 * 
	 * BatchOpeFactory.GetBatchOpe().ProcessBatchFile(nbatch, nfileinfo);
	 * 
	 * // TODO: 文件数据完整后，进行ProcessBatch if (nbatch.IsFileDataComplete()) {
	 * TigEraLogger.Log("2.3采用分文件传送批次，批次完成"); // nresult =
	 * BatchOpeFactory.GetBatchOpe().ProcessBatch(nbatch);
	 * _batchinfocache.remove(nbatch.getBatchNO());; }
	 * 
	 * } else { nresult = new NResultInfo();
	 * ngetResult().setStatus(EResultStatus.eFailed);
	 * ngetResult().setMsg("上传未知批次的内容，请重新上传整个批次"); } return nresult; }
	 */

	public static void main(String[] args) {
		// String
		// json="{'createTime':'','files':[{'fileSize':'','fileId':'0001','createTime':'','fileMd5':'','localPath':'f:\\tmp\\0005.jpg','fileName':'0005.jpg','lastModTime':'','mimeType':'image/png','version':0,'creator':''},{'fileSize':'','fileId':'0002','createTime':'','fileMd5':'','localPath':'f:\\tmp\\111.png','fileName':'111.png','lastModTime':'','mimeType':'image/gif','version':0,'creator':''}],'syncTime':'','state':'','serialNumber':'','batchId':'3ac99a22-cf15-4870-a6c5-9ec8b95f62f3','lastModTime':'','orgId':'o999','syncTotalTime':'','syncType':'','sysId':'111','syncState':'','sourceIp':'','tellerNo':'','spareOne':''}";

	}
}
