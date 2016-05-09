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
import cn.net.sinodata.cm.pb.ProtoBufInfo.EOperType;
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
public class FinishBrokeBatchService extends BaseServletService {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String batchId = request.getParameter("batchNo");
		try {
			manageService.finishBatch(batchId);
			getResult().setStatus(EResultStatus.eSuccess);
		} catch (Exception e) {
			logger.error(e);
			getResult().setStatus(EResultStatus.eFailed);
			getResult().setMsg(e.getMessage());
		} finally {
			response.setCharacterEncoding("UTF-8");
			getResult().toNetMsg().writeTo(response.getOutputStream());
		}
	}

}
