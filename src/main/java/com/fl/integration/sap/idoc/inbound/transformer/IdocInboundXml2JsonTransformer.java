package com.fl.integration.sap.idoc.inbound.transformer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.StringWriter;

/**
 * @author david
 * @create 2018-03-01 14:57
 **/
public class IdocInboundXml2JsonTransformer extends AbstractIdocTransformer<String> {

	private static XmlMapper xmlMapper = new XmlMapper();
	private static ObjectMapper objectMapper = new ObjectMapper();

	public IdocInboundXml2JsonTransformer() {
	}

	public String getComponentType() {
		return "idoc-to-json-transformer";
	}

	@Override
	protected String transform(String data) throws Exception {
		StringWriter w = new StringWriter();
		JsonParser jp = null;
		JsonGenerator jg = null;
		try {
			jp = xmlMapper.getFactory().createParser(data);
			jg = objectMapper.getFactory().createGenerator(w);
			while (jp.nextToken() != null) {
				jg.copyCurrentEvent(jp);
			}


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jp != null) {
				jp.close();
			}

			if (jg != null) {
				jg.close();
			}
		}
		return w.toString();
	}

}
