package com.fl.integration.sap.idoc.inbound.transformer;

import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.util.Assert;

/**
 * @author david
 * @create 2018-03-01 14:56
 **/
public abstract class AbstractIdocTransformer<T> extends AbstractTransformer {

	@Override
	protected Object doTransform(Message<?> message) throws Exception {
		try {
			Assert.notNull(message, "Message must not be null");
			Object payload = message.getPayload();
			Assert.notNull(payload, "Message payload must not be null");
			Assert.isInstanceOf(String.class, payload, "Message payload must be of type [java.lang.String]");
			String data = (String) payload;
			T result = this.transform(data);
			Message<?> transformedMessage = this.getMessageBuilderFactory().withPayload(result)
					.copyHeaders(message.getHeaders()).build();

			return transformedMessage;
		} catch (Exception ex) {
			throw new MessagingException(message, "failed to transform String Message", ex);
		}
	}

	protected abstract T transform(String data) throws Exception;
}
