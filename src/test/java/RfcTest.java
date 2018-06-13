import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import java.io.StringWriter;

/**
 * @author bourne
 * @create 2018-06-12 8:47
 **/
public class RfcTest {

	static String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";

	private static ObjectMapper objectMapper = new ObjectMapper();

	private static XmlMapper xmlMapper = new XmlMapper();

	public static void main(String[] args) throws JCoException {
		RfcTest rfcTest = new RfcTest();
		rfcTest.rfcTest();
	 	/*
		String[] list= new String[2];
		String string = "{\"TYPE\":\"\",\"ID\":\"\",\"NUMBER\":\"000\",\"MESSAGE\":\"\",\"LOG_NO\":\"\","
				+ "\"LOG_MSG_NO\":\"000000\",\"MESSAGE_V1\":\"\",\"MESSAGE_V2\":\"\",\"MESSAGE_V3\":\"\",\"MESSAGE_V4\":\"\"}";
		list[0]=string;
		list[1]=string;

	 String str = 	list.toString();
		System.out.println(list.toString());*/
	}

	public void rfcTest() throws JCoException {

		JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
		JCoFunction function = destination.getRepository().getFunction("BAPI_CUSTOMER_GETDETAIL2");
		function.getImportParameterList().setValue("CUSTOMERNO", "0010000492");//客户名称
		function.getImportParameterList().setValue("COMPANYCODE", "2000");//公司代码固定值
		String inputString = function.getImportParameterList().toXML();
		System.out.println(inputString);

		function.execute(destination);

		JCoStructure returnStructure = function.getExportParameterList().getStructure("RETURN");
		String returnString = returnStructure.toXML();

		returnString = returnString.replace("BAPIRET1", "RETURN");
		System.out.println(returnString);
		JCoStructure addressStructure = function.getExportParameterList().getStructure("CUSTOMERADDRESS");
		String addressString = addressStructure.toXML();
		addressString = addressString.replace("BAPICUSTOMER_04", "CUSTOMERADDRESS");

		System.out.println(addressString);
		JCoStructure gendetailStructure = function.getExportParameterList().getStructure("CUSTOMERGENERALDETAIL");
		String gendetailString = gendetailStructure.toXML();
		gendetailString = gendetailString.replace("BAPICUSTOMER_KNA1", "CUSTOMERGENERALDETAIL");
		System.out.println(gendetailString);
		JCoStructure compdetailStructure = function.getExportParameterList().getStructure("CUSTOMERCOMPANYDETAIL");
		String compdetailString = compdetailStructure.toXML();
		compdetailString = compdetailString.replace("BAPICUSTOMER_05", "CUSTOMERCOMPANYDETAIL");
		System.out.println(compdetailString);
		JCoTable bankdetailTable = function.getTableParameterList().getTable("CUSTOMERBANKDETAIL");
		String bankdetailString = bankdetailTable.toXML();
		bankdetailString = bankdetailString.replace("BAPICUSTOMER_02", "CUSTOMERBANKDETAIL");
		System.out.println(bankdetailString);

		try {

			String xmlString = "<data><BAPICUSTOMERGETDETAIL2>" + inputString + returnString +
					addressString + gendetailString + compdetailString + bankdetailString + "</BAPICUSTOMERGETDETAIL2></data>";
			System.out.println(xmlString);
			String resJson = this.xml2Json(xmlString);
			System.out.println(resJson);

		} catch (Exception e) {
			e.printStackTrace();
		}


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
