package com.fl.integration.sap.idoc.config.transformer;

import org.springframework.messaging.Message;

/**
 * @author david
 * @create 2018-03-01 14:57
 **/
public class IdocInboundTransformer extends AbstractIdocTransformer {

	@Override
	protected Object doTransform(Message<?> message) throws Exception {
		return null;
	}
}
