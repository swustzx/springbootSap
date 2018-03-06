package com.fl.integration.sap.idoc.support.converter;

import java.util.Map;
import org.springframework.messaging.MessageHeaders;

/**
 * @author david
 * @create 2018-03-06 15:38
 **/
public class IdocMessageHeaders extends MessageHeaders {

	public IdocMessageHeaders(boolean generateId, boolean generateTimestamp) {
		super((Map) null, generateId ? null : ID_VALUE_NONE, generateTimestamp ? null : -1L);
	}

	public Map<String, Object> getRawHeaders() {
		return super.getRawHeaders();
	}
}
