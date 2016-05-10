package cn.net.sinodata.cm.log;

import org.apache.log4j.Logger;

/**
 * 
 * @author manan
 *
 */
public class CmLogger {

	private Logger logger;

	public static CmLogger getLogger(Class<?> clazz) {
		return new CmLogger(clazz);
	}

	protected CmLogger(Class<?> clazz) {
		logger = Logger.getLogger(clazz);
	}

	public void info(Object obj) {
		if (obj instanceof Throwable) {
			logger.info("", (Throwable) obj);
		}
		logger.info(obj);
	}

	public void info(Object message, Throwable t) {
		logger.info(message, t);
	}

	public void debug(Object obj) {
		if (logger.isDebugEnabled()) {
			if (obj instanceof Throwable) {
				logger.debug("", (Throwable) obj);
			}
			logger.debug(obj);
		}
	}

	public void debug(Object message, Throwable t) {
		if (logger.isDebugEnabled()) {
			logger.debug(message, t);
		}
	}
	
	public void warn(Object obj) {
		if (obj instanceof Throwable) {
			logger.warn("", (Throwable) obj);
		}
		logger.warn(obj);
	}

	public void warn(Object message, Throwable t) {
		logger.warn(message, t);
	}
	
	public void error(Object obj) {
		if (obj instanceof Throwable) {
			logger.error("", (Throwable) obj);
		}
		logger.error(obj);
	}

	public void error(Object message, Throwable t) {
		logger.error(message, t);
	}
}
