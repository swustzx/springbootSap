/**
 * #  *  MyIDocHandlerFactory.java Create on 2011-3-23 下午06:30:01
 * #  *
 * #  * Copyright (c) 2011 by biz.
 * #
 */
package com.fl.integration.sap.idoc.inbound;

import com.sap.conn.idoc.jco.JCoIDocHandler;
import com.sap.conn.idoc.jco.JCoIDocHandlerFactory;
import com.sap.conn.idoc.jco.JCoIDocServerContext;

// TODO: Auto-generated Javadoc

/**
 * A factory for creating MyIDocHandler objects.
 *
 * @author 作者 yangyz
 * @version 创建时间：2011-3-23 下午06:30:01 类说明
 */
public class IDocHandlerFactory implements JCoIDocHandlerFactory {

	/** The handler. */
	private JCoIDocHandler handler;

	/**
	 * Gets the handler.
	 *
	 * @return the handler
	 */
	public JCoIDocHandler getHandler() {
		return handler;
	}

	/**
	 * Sets the handler.
	 *
	 * @param handler
	 *            the new handler
	 */
	public void setHandler(JCoIDocHandler handler) {
		this.handler = handler;
	}

	/* (non-Javadoc)
	 * @see com.sap.conn.idoc.jco.JCoIDocHandlerFactory#getIDocHandler(com.sap.conn.idoc.jco.JCoIDocServerContext)
	 */
	public JCoIDocHandler getIDocHandler(JCoIDocServerContext serverCtx) {
		return handler;
	}
}
