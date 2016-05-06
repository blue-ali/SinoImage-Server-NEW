/**
 * 
 */
package cn.net.sinodata.cm.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.pb.ProtoBufInfo.EOperType;
import cn.net.sinodata.cm.pb.bean.ResultInfo;
import cn.net.sinodata.cm.service.IContentManagerService;
import cn.net.sinodata.framework.log.SinoLogger;

/**
 * servlet 基类
 * @author manan
 *
 */
@SuppressWarnings("serial")
public class BaseServletService extends HttpServlet {

	protected SinoLogger logger = SinoLogger.getLogger(this.getClass());

	@Resource
	protected IContentManagerService manageService;

	protected ThreadLocal<ResultInfo> result = new ThreadLocal<ResultInfo>();
}
