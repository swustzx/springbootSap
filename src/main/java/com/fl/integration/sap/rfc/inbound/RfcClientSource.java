package com.fl.integration.sap.rfc.inbound;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

/**
 * @author hasee
 * @create 2018-06-07 15:28
 **/
public class RfcClientSource {

	static String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";

	//private JCoDestination destination;
	private synchronized JCoDestination createDestination() throws JCoException {
		JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
		return destination;
	}

}
