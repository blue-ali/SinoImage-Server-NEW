/**
 * 
 */
package cn.net.sinodata.cm.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.NDC;
import org.springframework.stereotype.Controller;

import cn.net.sinodata.cm.pb.ProtoBufInfo.EResultStatus;

/**
 * 完成批次请求，用于断点续传模式，批次所有内容都提交完成后做确认并修改批次状态
 * @author manan
 *
 */
@Controller
@SuppressWarnings("serial")
public class FinishBrokeBatchService extends BaseServletService {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String batchId = request.getParameter("batchNo");
		NDC.push(batchId);
		logger.info("完成批次[" + batchId + "]");
		try {
			manageService.finishBatch(batchId);
			getResult().setStatus(EResultStatus.eSuccess);
		} catch (Exception e) {
			logger.error(e);
			getResult().setStatus(EResultStatus.eFailed);
			getResult().setMsg(e.getMessage());
		} finally {
			NDC.pop();
			response.setCharacterEncoding("UTF-8");
			getResult().toNetMsg().writeTo(response.getOutputStream());
		}
	}

}
