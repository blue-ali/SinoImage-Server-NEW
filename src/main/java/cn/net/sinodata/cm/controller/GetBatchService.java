/**
 * 
 */
package cn.net.sinodata.cm.controller;

import java.io.IOException;
import java.text.ParseException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.loader.collection.BatchingCollectionInitializer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.pb.ProtoBufInfo.EOperType;
import cn.net.sinodata.cm.pb.ProtoBufInfo.EResultStatus;
import cn.net.sinodata.cm.service.IContentManagerService;
import cn.net.sinodata.framework.log.SinoLogger;

/**
 * @author manan
 *
 */
@Controller
@Scope("prototype")
@SuppressWarnings("serial")
public class GetBatchService extends BaseServletService {

//	private SinoLogger logger = SinoLogger.getLogger(this.getClass());

	@Resource
	private IContentManagerService manageService;


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String batchId = request.getParameter("batchNo");
		// MsgBatchInfo mBatchInfo =
		// MsgBatchInfo.parseFrom(request.getInputStream());
		// BatchInfo querybatchinfo = BatchInfo.FromNetMsg(mBatchInfo);
		BatchInfo batchInfo = null;
		try {
			batchInfo = manageService.getBatch(batchId);
			if (batchInfo != null) {
				batchInfo.setOperation(EOperType.eFROM_SERVER_NOTCHANGE);
			}
			batchInfo.getResultInfo().setStatus(EResultStatus.eSuccess);
			batchInfo.toNetMsg().writeTo(response.getOutputStream());
		} catch (Exception e) {
			logger.error(e);
			getResult().setStatus(EResultStatus.eFailed);
			getResult().setMsg(e.getMessage());
			getResult().toNetMsg().writeTo(response.getOutputStream());
		} 
	}

}
