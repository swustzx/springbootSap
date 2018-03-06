/**
 * # * MyIDocHandler.java Create on 2011-3-23 下午06:10:31
 * # *
 * # * Copyright (c) 2011 by biz.
 * #
 */
package com.fl.integration.sap.idoc.inbound;

import com.sap.conn.idoc.IDocDocument;
import com.sap.conn.idoc.IDocDocumentIterator;
import com.sap.conn.idoc.IDocDocumentList;
import com.sap.conn.idoc.IDocRepository;
import com.sap.conn.idoc.jco.JCoIDoc;
import com.sap.conn.idoc.jco.JCoIDocHandler;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.server.JCoServerContext;
import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc

/**
 * The Class MyIDocHandler.
 *
 * @author 作者 yangyz
 * @version 创建时间：2011-3-23 下午06:10:31 类说明
 */
public abstract class AbstractIdocHandler implements JCoIDocHandler {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AbstractIdocHandler.class);

	/**
	 * The Constant LOGGER.
	 */

	public boolean executeIdoc(IDocDocument doc) {
		if (logger.isDebugEnabled()) {
			logger.debug("executeIdoc(IDocDocument) - start"); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("executeIdoc(IDocDocument) - end"); //$NON-NLS-1$
		}
		return true;

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.sap.conn.idoc.jco.JCoIDocHandler#handleRequest(com.sap.conn.jco.
	 * server
	 * .JCoServerContext, com.sap.conn.idoc.IDocDocumentList)
	 */
	public final void handleRequest(JCoServerContext serverCtx, IDocDocumentList idocList) {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"handleRequest(JCoServerContext, IDocDocumentList) - start"); //$NON-NLS-1$
		}

		try {

			JCoDestination destination = JCoDestinationManager
					.getDestination("BCE");
			IDocRepository iDocRepository = JCoIDoc
					.getIDocRepository(destination);
			destination.ping();
			logger.info("docList size:: " + idocList.size());
			logger.info("iDocRepository Name:" + iDocRepository.getName());
			IDocDocumentIterator iterator = idocList.iterator();

			while (iterator.hasNext()) {
				IDocDocument doc = iterator.next();

				if (doc != null) {
					logger.info("IDoc number: " + doc.getIDocNumber());

					executeIdoc(doc);
				} else {
					logger.info("IDoc Type is not matching");
				}

			}

		} catch (Throwable thr) {
			logger.error("handleRequest(JCoServerContext, IDocDocumentList)", //$NON-NLS-1$
					thr);

			thr.printStackTrace();
			logger.error("handleRequest error:", thr);
		}

		if (logger.isDebugEnabled()) {
			logger.debug(
					"handleRequest(JCoServerContext, IDocDocumentList) - end"); //$NON-NLS-1$
		}
	}
}
