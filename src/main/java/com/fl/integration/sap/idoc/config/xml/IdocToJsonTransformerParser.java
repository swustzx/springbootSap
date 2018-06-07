package com.fl.integration.sap.idoc.config.xml;

import com.fl.integration.sap.idoc.inbound.transformer.IdocInboundXml2JsonTransformer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.AbstractTransformerParser;
import org.springframework.integration.config.xml.IntegrationNamespaceUtils;
import org.w3c.dom.Element;

/**
 * @author david
 * @create 2018-03-07 09:15
 **/
public class IdocToJsonTransformerParser extends AbstractTransformerParser {

	@Override
	protected String getTransformerClassName() {
		return IdocInboundXml2JsonTransformer.class.getName();
	}

	@Override
	protected void parseTransformer(Element element, ParserContext parserContext,
			BeanDefinitionBuilder beanDefinitionBuilder) {
		IntegrationNamespaceUtils.setValueIfAttributeDefined(beanDefinitionBuilder, element, "charset");
	}
}
