package com.fl.integration.sap.idoc.inbound;

import org.springframework.integration.context.OrderlyShutdownCapable;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.endpoint.Pausable;
import org.springframework.util.Assert;

/**
 * @author david
 * @create 2018-03-05 09:03
 **/
public class IdocMessageDrivenChannelAdapter<K, V> extends MessageProducerSupport implements OrderlyShutdownCapable,
		Pausable {

	private IDocServerSource iDocServerSource;

	public IdocMessageDrivenChannelAdapter() {
	}

	public IdocMessageDrivenChannelAdapter(IDocServerSource iDocServerSource) {
		Assert.notNull(iDocServerSource, "iDocServerSource is required");
		this.iDocServerSource = iDocServerSource;
	}

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

	@Override
	protected void onInit() {
		super.onInit();

		iDocServerSource.init();
	}

	@Override
	protected void doStart() {
		this.iDocServerSource.start();
	}
}
