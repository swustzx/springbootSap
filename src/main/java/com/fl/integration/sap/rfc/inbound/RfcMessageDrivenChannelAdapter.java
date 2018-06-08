package com.fl.integration.sap.rfc.inbound;

/**
 * @author hasee
 * @create 2018-06-07 16:03
 **/
public class RfcMessageDrivenChannelAdapter {

	private RfcClientSource rfcClientSource;

	public RfcClientSource getRfcClientSource() {
		return rfcClientSource;
	}

	public void setRfcClientSource(RfcClientSource rfcClientSource) {
		this.rfcClientSource = rfcClientSource;
	}

	public void handleMessage(String text) {
		System.out.println("Received: " + text);

		//	System.out.println(new Date());
	}
}
