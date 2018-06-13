package com.fl.integration.sap.rfc.inbound;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import org.apache.log4j.Logger;
/**
 * @author hasee
 * @create 2018-06-07 15:28
 **/
public class RfcClientSource {

	private static final Logger logger = Logger.getLogger(RfcClientSource.class);
	private String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";

	private JCoDestination destination;

	public JCoDestination getDestination() throws JCoException {
		try {
			return createDestination();
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.info("RfcClientSource error,get JCoDestination error");
			}

			e.printStackTrace();
		}
		return null;
	}

	public void setDestination(JCoDestination destination) {
		this.destination = destination;
	}

	private synchronized JCoDestination createDestination() throws JCoException {
		JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
		return destination;
	}

}
