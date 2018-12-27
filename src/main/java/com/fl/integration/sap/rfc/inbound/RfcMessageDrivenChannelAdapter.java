package com.fl.integration.sap.rfc.inbound;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import java.io.StringWriter;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author hasee
 * @create 2018-06-07 16:03
 **/
public class RfcMessageDrivenChannelAdapter {

	private static final Logger logger = Logger.getLogger(RfcMessageDrivenChannelAdapter.class);
	private static ObjectMapper objectMapper = new ObjectMapper();
	private RfcClientSource rfcClientSource;
	private static XmlMapper xmlMapper = new XmlMapper();
	private RabbitTemplate rabbitTemplate;
	private RedisTemplate<String,String> redisTemplate;
	public RfcClientSource getRfcClientSource() {
		return rfcClientSource;
	}

	public RabbitTemplate getRabbitTemplate() {
		return rabbitTemplate;
	}

	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void setRfcClientSource(RfcClientSource rfcClientSource) {
		this.rfcClientSource = rfcClientSource;
	}

	public void handleMessage(String text) throws Exception, JCoException {
		if (logger.isDebugEnabled()) {
			logger.info("Received: " + text);
		}

		try {
			JsonNode jsonNode = objectMapper.readTree(text);
			JsonNode IDOC = jsonNode.path("IDOC");
			JsonNode EDI_DC40 = IDOC.path("EDI_DC40");
			String mestype = EDI_DC40.path("MESTYP").textValue();

			if ("DEBMAS".equals(mestype)) {
				JsonFactory jfactory = new JsonFactory();
				JsonParser jParser = jfactory.createParser(text);

				while (jParser.nextToken() != JsonToken.END_OBJECT) {
					String fieldname = jParser.getCurrentName();


						logger.info("fieldname-->" + fieldname);
						logger.info("value-->" + jParser.getText());


					if ("KUNNR".equals(fieldname)) {
						JCoFunction function = rfcClientSource.getDestination().getRepository()
								.getFunction("BAPI_CUSTOMER_GETDETAIL2");
						if (function == null) {
							throw new RuntimeException("BAPI_CUSTOMER_GETDETAIL2 not found in SAP.");
						}
						jParser.nextToken();

						System.out.println(jParser.getText());
						System.out.println(jParser.getValueAsString());

						logger.info("customer no-->"+jParser.getText());

						function.getImportParameterList().setValue("CUSTOMERNO", jParser.getText());//客户名称
						function.getImportParameterList().setValue("COMPANYCODE", "2000");//公司代码固定值

						String inputString = function.getImportParameterList().toXML();

						function.execute(rfcClientSource.getDestination());
						//返回参数-替换xml中的标签，与sap rfc参数名称一致，不使用参照结构名称
						JCoStructure returnStructure = function.getExportParameterList().getStructure("RETURN");
						logger.info("type:-->"+returnStructure.getString("TYPE"));


						String returnString = returnStructure.toXML();

						returnString = returnString.replace("BAPIRET1", "RETURN");

						JCoStructure addressStructure = function.getExportParameterList().getStructure("CUSTOMERADDRESS");
						String addressString = addressStructure.toXML();
						addressString = addressString.replace("BAPICUSTOMER_04", "CUSTOMERADDRESS");

						JCoStructure gendetailStructure = function.getExportParameterList().getStructure("CUSTOMERGENERALDETAIL");
						String gendetailString = gendetailStructure.toXML();
						gendetailString = gendetailString.replace("BAPICUSTOMER_KNA1", "CUSTOMERGENERALDETAIL");

						JCoStructure compdetailStructure = function.getExportParameterList().getStructure("CUSTOMERCOMPANYDETAIL");
						String compdetailString = compdetailStructure.toXML();
						compdetailString = compdetailString.replace("BAPICUSTOMER_05", "CUSTOMERCOMPANYDETAIL");

						JCoTable bankdetailTable = function.getTableParameterList().getTable("CUSTOMERBANKDETAIL");
						String bankdetailString = bankdetailTable.toXML();
						bankdetailString = bankdetailString.replace("BAPICUSTOMER_02", "CUSTOMERBANKDETAIL");

//拼接XML参数，输入参数，返回参数，方便转换JSON
						String xmlString = "<data><BAPICUSTOMERGETDETAIL2>" + inputString + returnString +
								addressString + gendetailString + compdetailString + bankdetailString
								+ "</BAPICUSTOMERGETDETAIL2></data>";

						String resJson = this.xml2Json(xmlString);

						if (logger.isDebugEnabled()) {
							logger.info("rfc xml message: -->" + xmlString);
							logger.info("rfc xml2json result:-->" + resJson);
						}
						if (logger.isDebugEnabled()) {
							logger.info("send to mq: --> start");

						}
						//exchange--si.rfc.exchange,routing key--si.rfc.binding,object--发送的json
						rabbitTemplate.convertAndSend("si.rfc.exchange", "si.rfc.binding",
								resJson);

						if (logger.isDebugEnabled()) {
							logger.info("send to mq: --> end");

						}

						if (returnStructure.getString("TYPE").equals("")||returnStructure.getString("TYPE").equals("S")){
							//read rfc success
							ValueOperations<String, String> valueOper = redisTemplate.opsForValue();
							logger.info("check redis ustomer no-->  "+jParser.getText());
							String test1 = valueOper.get("test1");
							logger.info("check redis: test1-->  "+test1);
							String getValue = valueOper.get(jParser.getText());
							logger.info("check redis: -->  "+getValue);

							if (getValue==null||("").equals(getValue)){
								valueOper.set(jParser.getText(),resJson);
							}
						}

						break;
					}
					jParser.nextToken();


				}


			}

		} catch (Exception e) {
		} finally {

		}

		//	System.out.println(new Date());
	}

	public String xml2Json(String data) throws Exception {
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
