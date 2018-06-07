/**
 * # * MaraIDocHandler.java Create on 2012-4-27 上午09:05:56 # * # * Copyright (c)
 * 2012 by biz. #
 */
package com.fl.integration.sap.idoc.inbound.exec;

import com.fl.integration.sap.idoc.inbound.AbstractIdocHandler;
import com.sap.conn.idoc.IDocDocument;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author 作者 yangyz
 * @version 创建时间：2012-4-27 上午09:05:56 类说明
 */
public class ExecIDocHandler extends AbstractIdocHandler {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ExecIDocHandler.class);

	private List<IExecIdocHandler> execIdocHandlers = new ArrayList<IExecIdocHandler>();

	public List<IExecIdocHandler> getExecIdocHandlers() {
		return execIdocHandlers;
	}

	public void setExecIdocHandlers(List<IExecIdocHandler> execIdocHandlers) {
		this.execIdocHandlers = execIdocHandlers;
	}


	/**
	 * 注册观察者对象
	 *
	 * @param observer 观察者对象
	 */
	public void attach(IExecIdocHandler execIdocHandler) {
		if (logger.isDebugEnabled()) {
			logger.debug("attach(IExecIdocHandler) - start"); //$NON-NLS-1$
		}

		execIdocHandlers.add(execIdocHandler);

		if (logger.isDebugEnabled()) {
			logger.debug("attach(IExecIdocHandler) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 删除观察者对象
	 *
	 * @param observer 观察者对象
	 */
	public void dettach(IExecIdocHandler execIdocHandler) {
		if (logger.isDebugEnabled()) {
			logger.debug("dettach(IExecIdocHandler) - start"); //$NON-NLS-1$
		}

		execIdocHandlers.remove(execIdocHandler);

		if (logger.isDebugEnabled()) {
			logger.debug("dettach(IExecIdocHandler) - end"); //$NON-NLS-1$
		}
	}

	@Override
	public final boolean executeIdoc(IDocDocument doc) {
		super.executeIdoc(doc);
		if (logger.isDebugEnabled()) {
			logger.debug("executeIdoc(IDocDocument) - start"); //$NON-NLS-1$
		}

		if (execIdocHandlers != null && execIdocHandlers.size() > 0) {
			for (int i = 0; i < execIdocHandlers.size(); i++) {
				IExecIdocHandler execIdocHandler = execIdocHandlers.get(i);
				logger.info("进入消息类型为" + doc.getMessageType() + "的IDOC");
				logger.info("获取到的messgeType" + execIdocHandler.getMessageType()
						+ "");
				if (execIdocHandler != null
						&& StringUtils.equals(doc.getMessageType(),
						execIdocHandler.getMessageType())) {
					logger.info("进入消息类型为" + doc.getMessageType() + "的IDOC");
					execIdocHandler.execute(doc);
				}
			}

		}

		if (logger.isDebugEnabled()) {
			logger.debug("executeIdoc(IDocDocument) - end"); //$NON-NLS-1$
		}
		return false;
	}

}
