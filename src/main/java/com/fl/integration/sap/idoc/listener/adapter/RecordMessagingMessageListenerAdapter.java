package com.fl.integration.sap.idoc.listener.adapter;

import com.fl.integration.sap.idoc.listener.MessageListener;
import com.fl.integration.sap.idoc.support.converter.IdocMessageHeaders;
import com.sap.conn.idoc.IDocDocument;
import java.util.Map;
import org.springframework.messaging.Message;

/**
 * @author david
 * @create 2018-03-06 14:38
 **/
public class RecordMessagingMessageListenerAdapter<T> extends MessagingMessageListenerAdapter implements
		MessageListener<T> {

	private boolean generateMessageId = false;
	private boolean generateTimestamp = false;

	@Override
	public Message<?> onMessage(IDocDocument data) {
		IdocMessageHeaders idocMessageHeaders = createHeader(data);
		return this.toMessagingMessage(data);
	}

	protected IdocMessageHeaders createHeader(IDocDocument docDocument) {
		IdocMessageHeaders idocMessageHeaders = new IdocMessageHeaders(this.generateMessageId, this.generateTimestamp);
		Map<String, Object> rawHeaders = idocMessageHeaders.getRawHeaders();
		rawHeaders.put("idocNumber", docDocument.getIDocNumber());
		return idocMessageHeaders;
	}

}
