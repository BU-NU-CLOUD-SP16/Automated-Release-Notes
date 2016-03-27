import java.io.IOException;
import java.io.InputStream;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class GetBuildDetails {
	public static void main(String args[]) throws ClientProtocolException, IOException, ParserConfigurationException, SAXException {
		
		String buildNum=null;
		
		buildNum = getLatestBuild();
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		String url = "http://localhost/httpAuth/app/rest/changes?locator=build:(number:" + buildNum + ")&fields=count,change:(comment)";
	    String userName = "karunesh";
	    String password = "teamcity";
	    String authString = userName + ":" + password;
	    byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	    String authStringEnc = new String(authEncBytes);
	    
	    HttpGet httpGet = new HttpGet(url);
	    //httpPost.setHeader("Accept", "application/json");
	    httpGet.setHeader("Authorization", "Basic " + authStringEnc);
	    
	    HttpResponse response = httpClient.execute(httpGet);
	    InputStream responseStream = response.getEntity().getContent();
    
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document doc = db.parse(responseStream);
	    Element e  = (Element)doc.getDocumentElement().getElementsByTagName("comment").item(0);
	    String comment = e.getTextContent();
	    String requiredString = comment.substring(comment.indexOf("#")+1);
	    requiredString = requiredString.substring(0, comment.indexOf(" "));
	    System.out.println();
	    System.out.println("Work Item Number is "+requiredString);
	}
	
	static String getLatestBuild() throws ClientProtocolException, IOException, ParserConfigurationException, SAXException{
		String bNumber;
		HttpClient httpClient = HttpClientBuilder.create().build();
		String url = "http://localhost/httpAuth/api/buildTypes/id:CloudcomputingARN_BuildF/builds?count=1";
		String userName = "karunesh";
	    String password = "teamcity";
	    String authString = userName + ":" + password;
	    byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	    String authStringEnc = new String(authEncBytes);
	    
	    HttpGet httpGet = new HttpGet(url);
	    //httpPost.setHeader("Accept", "application/json");
	    httpGet.setHeader("Authorization", "Basic " + authStringEnc);
	    
	    HttpResponse response = httpClient.execute(httpGet);
	    InputStream responseStream = response.getEntity().getContent();
	   	    
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document doc = db.parse(responseStream);
	    Element e  = (Element)doc.getDocumentElement().getElementsByTagName("build").item(0);
	    bNumber = e.getAttribute("number");
	    
	    
	    return bNumber;
		
	}

}
