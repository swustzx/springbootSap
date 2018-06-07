/*
 * Copyright 2015-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fl.integration.sap.idoc.config.xml;

import com.fl.integration.sap.idoc.inbound.IdocMessageDrivenChannelAdapter;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.AbstractChannelAdapterParser;
import org.springframework.integration.config.xml.IntegrationNamespaceUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * Parser for the message driven channel adapter.
 *
 * @author Artem Bilan
 * @author Gary Russell
 */
public class IdocMessageDrivenChannelAdapterParser extends AbstractChannelAdapterParser {

	@Override
	protected AbstractBeanDefinition doParse(Element element, ParserContext parserContext, String channelName) {
		BeanDefinitionBuilder builder =
				BeanDefinitionBuilder.genericBeanDefinition(IdocMessageDrivenChannelAdapter.class);

		String container = element.getAttribute("listener-container");
		if (StringUtils.hasText(container)) {
			builder.addConstructorArgReference(container);
		} else {
			parserContext.getReaderContext().error("The 'listener-container' attribute is required.", element);
		}

		builder.addPropertyReference("outputChannel", channelName);
		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "send-timeout");
		IntegrationNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "error-channel");

		return builder.getBeanDefinition();
	}

}
