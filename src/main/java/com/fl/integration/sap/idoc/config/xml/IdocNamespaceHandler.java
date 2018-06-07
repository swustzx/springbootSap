package com.fl.integration.sap.idoc.config.xml;

import org.springframework.integration.config.xml.AbstractIntegrationNamespaceHandler;
/**
 * @author david
 * @create 2018-03-01 14:46
 **/
public class IdocNamespaceHandler extends AbstractIntegrationNamespaceHandler {

	public IdocNamespaceHandler() {
	}

	public void init() {
		registerBeanDefinitionParser("message-driven-channel-adapter", new IdocMessageDrivenChannelAdapterParser());
		this.registerBeanDefinitionParser("idoc-to-json-transformer", new IdocToJsonTransformerParser());
	}
}
