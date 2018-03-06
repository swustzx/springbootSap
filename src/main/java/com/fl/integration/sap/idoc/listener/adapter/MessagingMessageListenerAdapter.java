package com.fl.integration.sap.idoc.listener.adapter;

import com.fl.integration.sap.idoc.support.converter.IdocMessageHeaders;
import com.sap.conn.idoc.IDocDocument;
import com.sap.conn.idoc.IDocXMLProcessor;
import com.sap.conn.idoc.jco.JCoIDoc;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author david
 * @create 2018-03-06 16:37
 **/
public abstract class MessagingMessageListenerAdapter {

	protected Message<?> toMessagingMessage(IDocDocument data) {
		IdocMessageHeaders idocMessageHeaders = createHeader(data);
		return MessageBuilder.createMessage(this.convertPayload(data), idocMessageHeaders);
	}

	protected Object convertPayload(IDocDocument data) {
		IDocXMLProcessor xmlProcessor =
				JCoIDoc.getIDocFactory().getIDocXMLProcessor();
		String string = xmlProcessor.render(data,
				IDocXMLProcessor.RENDER_WITH_TABS_AND_CRLF);
		return string;
	}

	protected abstract IdocMessageHeaders createHeader(IDocDocument docDocument);
}
