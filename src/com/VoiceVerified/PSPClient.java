package com.VoiceVerified;

import android.util.Log;

import com.mindprod.base64.Base64;
import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;


public class PSPClient {
	private static final String exception = "PSPClientException";
	public PSPClient(String brokerurl) throws Exception {

		requesttype = "";
		responsetype = "";
		params = new Hashtable();
		responsevalues = new Hashtable();
		url = new URL(brokerurl);
	}

	public void clearParameters() {
		requesttype = "";
		params.clear();
	}

	public void clearParameter(String paramname) {
		params.remove(paramname);
	}

	public void setParameter(String paramname, String paramvalue) {
		if (paramname != null && paramvalue != null)
			params.put(paramname, paramvalue);
	}

	public void setRequestType(String requesttype) {
		if (requesttype != null)
			this.requesttype = requesttype;
	}

	public String getResponseValue(String valuename) {
		return (String) responsevalues.get(valuename);
	}

	public Hashtable getResponseValues() {
		return (Hashtable) responsevalues.clone();
	}

	public String getResponseType() {
		return responsetype;
	}

	public void clearResponse() {
		responsetype = "";
		responsevalues.clear();
	}

	public String encodeSound(byte input[]) throws IOException {
		Base64 b64 = new Base64();
		return b64.encode(input).toString();
	}

	public byte[] decodeSound(String sound) throws IOException{
		Base64 b64 = new Base64();
		return b64.decode(sound);
	}
	public void sendRequest(String fieldname) throws Exception {

		String xml = buildXMLRequest();
		clearResponse();
		xml = URLEncoder.encode(xml, "UTF-8");
				
		if (fieldname != null)
			xml = (new StringBuilder(String.valueOf(fieldname))).append("=").append(xml).toString();

		
		HttpClient hc = new DefaultHttpClient(); 
		HttpPost postMethod = new HttpPost(url.toString());
		StringEntity se = new StringEntity(xml, HTTP.UTF_8);
		se.setContentType("text/xml");
		postMethod.setEntity(se);
		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = hc.execute(postMethod, responseHandler);
			Log.i("response handler", responseBody);
			StringReader sr = new StringReader(responseBody);
			BufferedReader reader = new BufferedReader(sr);
			StringBuffer sb = new StringBuffer();
			String s;
			
			while ((s = reader.readLine()) != null)
				sb.append((new StringBuilder(String.valueOf(s))).append("\n").toString());
			
			parseXMLResponse(sb.toString());
			reader.close();
			sr.close();
			
		} catch (Throwable t) {
			Log.i(exception, t.toString());
		}
		hc.getConnectionManager().shutdown();	
		
	}

	public void sendRequest() throws Exception {
		sendRequest(null);
	}

	private String buildXMLRequest() throws UnknownRequestException {

		StringBuffer xml = new StringBuffer();
		if (requesttype.equals(""))
			throw new UnknownRequestException();
		xml.append((new StringBuilder("<")).append(requesttype)
				.append(">\n").toString());
		String field;
		for (Enumeration fields = params.keys(); fields.hasMoreElements(); xml
				.append((new StringBuilder("<")).append(field).append(">")
						.append((String) params.get(field)).append("</")
						.append(field).append(">\n").toString()))
			field = (String) fields.nextElement();
		
		xml.append((new StringBuilder("</")).append(requesttype).append(">\n").toString());

		return xml.toString();
	}

	/*private void parseXMLResponse(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docbuilder = factory.newDocumentBuilder();
		Document doc = docbuilder.parse(new InputSource(new StringReader(xml)));
		doc.getDocumentElement().normalize();
		String docType = doc.getDocumentElement().getTagName();
		Node node = doc.getFirstChild();
		Log.i("PSPCLIENT","  "+node.getChildNodes().getLength());
		NodeList nodes = node.getChildNodes();
		responsetype = docType;
		
		//Log.i("ResponseValues", "   "+doc.getTextContent()+"  "+node.getNodeName());
		for (int i = 0; i < nodes.getLength(); i++) {
			node = nodes.item(i);
			if (node.getNodeType() == 1){
				Log.i("Node Val", " "+ node.getFirstChild().getNodeValue());
				responsevalues.put(node.getNodeName(), node.getFirstChild().getNodeValue());
			}
		}
		
	}*/
	
	private void parseXMLResponse(String xml) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xml)));
		doc.getDocumentElement().normalize();
		String docType = doc.getDocumentElement().getTagName();
		Node node ;
		Element ele = doc.getDocumentElement(); 
		NodeList nodes = ele.getChildNodes();
		responsetype = docType;
		for(int i = 0; i<nodes.getLength(); i++){
			node = nodes.item(i);
			if (node.getNodeType() == 1){
				responsevalues.put(node.getNodeName(), node.getFirstChild().getNodeValue());
			}
		}
	}
	
	private String requesttype;
	private String responsetype;
	private Hashtable params;
	private Hashtable responsevalues;
	private URL url;
}