package com.fl.integration.sap.utils;

import com.sap.conn.idoc.jco.JCoIDoc;
import com.sap.conn.idoc.jco.JCoIDocServer;
import com.sap.conn.jco.JCoException;

/**
 * @author david
 * @create 2018-03-05 17:43
 **/
public class JCoIDocUtils extends com.sap.conn.idoc.jco.JCoIDoc {


	public static JCoIDocServer getServer(String serverName) throws JCoException {

		return JCoIDoc.getServer(serverName);
	}
}
