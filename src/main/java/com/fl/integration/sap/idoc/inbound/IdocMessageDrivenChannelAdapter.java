package com.fl.integration.sap.idoc.inbound;

import com.fl.integration.sap.idoc.listener.adapter.RecordMessagingMessageListenerAdapter;
import com.sap.conn.idoc.IDocDocument;
import org.springframework.integration.context.OrderlyShutdownCapable;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.endpoint.Pausable;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

/**
 * @author david
 * @create 2018-03-05 09:03
 **/
public class IdocMessageDrivenChannelAdapter<K, V> extends MessageProducerSupport implements OrderlyShutdownCapable,
		Pausable {

	private IDocServerSource iDocServerSource;

	private final IdocMessageDrivenChannelAdapter<K, V>.IntegrationRecordMessageListener recordListener;
	private volatile boolean initialized;

	@Override
	public String getComponentType() {
		return "idoc:message-driven-channel-adapter";
	}

	@Override
	public int beforeShutdown() {
		this.iDocServerSource.destroy();
		return getPhase();
	}

	@Override
	public int afterShutdown() {

		return getPhase();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	public IdocMessageDrivenChannelAdapter(IDocServerSource iDocServerSource) {
		Assert.notNull(iDocServerSource, "iDocServerSource is required");
		this.iDocServerSource = iDocServerSource;
		this.recordListener = new IdocMessageDrivenChannelAdapter.IntegrationRecordMessageListener();
	}

	private class IntegrationRecordMessageListener extends RecordMessagingMessageListenerAdapter {

		IntegrationRecordMessageListener() {

		}

		@Override
		public Message<?> onMessage(IDocDocument data) {
			Message message = null;

			try {
				message = this.toMessagingMessage(data);
			} catch (RuntimeException exception) {
				exception.printStackTrace();
			}
			if (message != null) {

				IdocMessageDrivenChannelAdapter.this.sendMessage(message);

			} else {
				IdocMessageDrivenChannelAdapter.this.logger.debug("Converter returned a null message for: " + data);
			}
			return message;
		}


	}

	@Override
	protected void doStart() {
		this.iDocServerSource.start();
	}

	@Override
	protected void onInit() {
		super.onInit();

		iDocServerSource.init();
		iDocServerSource.setupMessageListener(recordListener);
		initialized = true;
	}

}
