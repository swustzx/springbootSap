package com.fl.integration.sap.idoc.inbound;

import com.fl.integration.sap.idoc.inbound.exec.ExecIDocHandler;
import com.fl.integration.sap.idoc.listener.MessageListener;
import com.fl.integration.sap.idoc.listener.MessageListenerContainer;
import com.fl.integration.sap.idoc.listener.config.ContainerProperties;
import com.sap.conn.idoc.jco.JCoIDoc;
import com.sap.conn.idoc.jco.JCoIDocHandlerFactory;
import com.sap.conn.idoc.jco.JCoIDocServer;
import com.sap.conn.jco.server.JCoServer;
import com.sap.conn.jco.server.JCoServerContext;
import com.sap.conn.jco.server.JCoServerContextInfo;
import com.sap.conn.jco.server.JCoServerErrorListener;
import com.sap.conn.jco.server.JCoServerExceptionListener;
import com.sap.conn.jco.server.JCoServerState;
import com.sap.conn.jco.server.JCoServerStateChangedListener;
import com.sap.conn.jco.server.JCoServerTIDHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

// TODO: Auto-generated Javadoc

/**
 * The Class IDocServerExample.
 */
public class IDocServerSource implements MessageListenerContainer {

	/**
	 * The Constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(IDocServerSource.class);
	/**
	 * The my i doc handler factory.
	 */
	private JCoIDocHandlerFactory jCoIDocHandlerFactory;
	private JCoIDocServer server = null;

	private ContainerProperties containerProperties;

	public IDocServerSource(ContainerProperties containerProperties) {
		Assert.notNull(containerProperties, "'containerProperties' cannot be null");
		this.containerProperties = containerProperties;
	}

	public ContainerProperties getContainerProperties() {
		return containerProperties;
	}

	public JCoIDocHandlerFactory getjCoIDocHandlerFactory() {
		return jCoIDocHandlerFactory;
	}

	public void setjCoIDocHandlerFactory(JCoIDocHandlerFactory jCoIDocHandlerFactory) {
		this.jCoIDocHandlerFactory = jCoIDocHandlerFactory;
	}

	/**
	 * The listener interface for receiving myThrowable events. The class that
	 * is interested in processing a myThrowable event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addMyThrowableListener<code> method. When
	 * the myThrowable event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see MyThrowableEvent
	 */
	static class MyThrowableListener implements JCoServerErrorListener,
			JCoServerExceptionListener, JCoServerStateChangedListener {

		/*
		 * (non-Javadoc)
		 * @see
		 * com.sap.conn.jco.server.JCoServerErrorListener#serverErrorOccurred
		 * (com.sap.conn.jco.server.JCoServer, java.lang.String,
		 * com.sap.conn.jco.server.JCoServerContextInfo, java.lang.Error)
		 */
		public void serverErrorOccurred(JCoServer server, String connectionId,
				JCoServerContextInfo ctx, Error error) {
			// LOGGER.error(">>> Error occured on " + server.getProgramID()
			// + " connection " + connectionId);
			error.printStackTrace();
		}

		/*
		 * (non-Javadoc)
		 * @seecom.sap.conn.jco.server.JCoServerExceptionListener#
		 * serverExceptionOccurred(com.sap.conn.jco.server.JCoServer,
		 * java.lang.String, com.sap.conn.jco.server.JCoServerContextInfo,
		 * java.lang.Exception)
		 */
		public void serverExceptionOccurred(JCoServer server,
				String connectionId, JCoServerContextInfo ctx, Exception error) {
			// LOGGER.error(">>> Error occured on " + server.getProgramID()
			// + " connection " + connectionId);
			error.printStackTrace();
		}

		/*
		 * (non-Javadoc)
		 * @seecom.sap.conn.jco.server.JCoServerStateChangedListener#
		 * serverStateChangeOccurred(com.sap.conn.jco.server.JCoServer,
		 * com.sap.conn.jco.server.JCoServerState,
		 * com.sap.conn.jco.server.JCoServerState)
		 */
		public void serverStateChangeOccurred(JCoServer jcoserver,
				JCoServerState jcoserverstate, JCoServerState jcoserverstate1) {
			// LOGGER.info("JCoServer state name:" + jcoserverstate.name());
		}
	}

	/**
	 * The Class MyTidHandler.
	 */
	static class MyTidHandler implements JCoServerTIDHandler {

		/*
		 * (non-Javadoc)
		 * @see
		 * com.sap.conn.jco.server.JCoServerTIDHandler#checkTID(com.sap.conn
		 * .jco.server.JCoServerContext, java.lang.String)
		 */
		public boolean checkTID(JCoServerContext serverCtx, String tid) {
			// LOGGER.error("checkTID called for TID=" + tid);
			return true;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.sap.conn.jco.server.JCoServerTIDHandler#confirmTID(com.sap.conn
		 * .jco.server.JCoServerContext, java.lang.String)
		 */
		public void confirmTID(JCoServerContext serverCtx, String tid) {
			// LOGGER.error("confirmTID called for TID=" + tid);
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.sap.conn.jco.server.JCoServerTIDHandler#commit(com.sap.conn.jco
		 * .server.JCoServerContext, java.lang.String)
		 */
		public void commit(JCoServerContext serverCtx, String tid) {
			// LOGGER.error("commit called for TID=" + tid);
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.sap.conn.jco.server.JCoServerTIDHandler#rollback(com.sap.conn
		 * .jco.server.JCoServerContext, java.lang.String)
		 */
		public void rollback(JCoServerContext serverCtx, String tid) {
			// LOGGER.error("rollback called for TID=" + tid);
		}
	}

	/**
	 * Inits the.
	 */
	public synchronized void init() {
		if (server != null) {
			LOGGER.info("MYSERVER inited: " + server);
			return;
		}
		try {

			// JCo.setTrace(Trace.PATH3, "c:\\jcolog");
			server = JCoIDoc.getServer("MYSERVER");
//			String path= "src/main/resources";
//			DestinationDataProvider destinationDataProvider=new FileDestinationsDataProviderUtils(path);
//			//	Environment.registerDestinationDataProvider(destinationDataProvider);
//			if (!RuntimeEnvironment.isDestinationDataProviderRegistered()) {
//				RuntimeEnvironment.registerDestinationDataProvider(destinationDataProvider);
//			}
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("MySncName:" + server.getMySncName()
						+ "\tConnectionCount:" + server.getConnectionCount()
						+ "\tGatewayHost:" + server.getGatewayHost()
						+ "\tGatewayService:" + server.getGatewayService()
						+ "\tProgramID:" + server.getProgramID()
						+ "\tRepositoryDestination:"
						+ server.getRepositoryDestination() + server.getSncMode()
						+ "\tState:" + server.getState() + "\tSAPRouterString:"
						+ server.getSAPRouterString() + "\tSncLibrary:"
						+ server.getSncLibrary() + "\tSncMode:");
			}

			server.setIDocHandlerFactory(getjCoIDocHandlerFactory());

			server.setTIDHandler(new MyTidHandler());

			MyThrowableListener listener = new MyThrowableListener();
			server.addServerErrorListener(listener);
			server.addServerExceptionListener(listener);
			server.addServerStateChangedListener(listener);
			server.setConnectionCount(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void start() {
		if (server != null) {
			LOGGER.info("Start: " + server);
			server.start();
		}
	}

	public synchronized void destroy() {
		if (server != null) {
			LOGGER.info("Stop: " + server);
			server.stop();
			server = null;
		}
	}

	@Override
	public void setupMessageListener(Object messageListener) {

		this.getContainerProperties().setMessageListener(messageListener);
		ExecIDocHandler handler = (ExecIDocHandler) getjCoIDocHandlerFactory().getIDocHandler(null);

		handler.setMessageListener((MessageListener) getContainerProperties().getMessageListener());
	}
}
