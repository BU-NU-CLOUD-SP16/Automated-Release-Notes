package edu.bu.ec500.arn.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;
import org.xml.sax.SAXException;

import edu.bu.ec500.arn.bean.Authorization;
import edu.bu.ec500.arn.bean.WorkItemDetails;
import edu.bu.ec500.arn.bean.WorkItemResponse;
import edu.bu.ec500.arn.tc.GetBuildDetails;

public class AuthorizationCallback extends HttpServlet{

	
@Override
public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	//***Code for oauth
	/*String authCode = request.getParameter("code");

	String url="https://app.vssps.visualstudio.com/oauth2/token";
	HttpClient client = HttpClientBuilder.create().build();
	HttpPost httpPost = new HttpPost(url);

	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
    nameValuePairs.add(new BasicNameValuePair("client_assertion_type","urn:ietf:params:oauth:client-assertion-type:jwt-bearer"));
    nameValuePairs.add(new BasicNameValuePair("client_assertion","eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Im9PdmN6NU1fN3AtSGpJS2xGWHo5M3VfVjBabyJ9.eyJjaWQiOiI1Yjg3MjkxNC02Yzc1LTRkOGItYTU2ZC1iY2E0MjVlY2U0NTIiLCJjc2kiOiJmMGQ4MjI0NC0yYTNlLTQ5MzgtOTU5Yi00NDA4OWJiODMwMzEiLCJuYW1laWQiOiI4ZjY4Zjc5NS0yNjNkLTRiM2YtYTA5Yy1jOTIyYWI1MDdmNWMiLCJpc3MiOiJhcHAudnNzcHMudmlzdWFsc3R1ZGlvLmNvbSIsImF1ZCI6ImFwcC52c3Nwcy52aXN1YWxzdHVkaW8uY29tIiwibmJmIjoxNDU4MDEwNzAzLCJleHAiOjE2MTU3NzcxMDN9.qH6RF04QBrlmdgO7MyDgBsEjOTKG1gDSKLtICWU8OhcG3D4VFT3z5umltZFjrS0p_6tkGU87vrwniMDg65p8OFNJ0aoG5fbRsLlFAzGan4sIc59-GIXu20LVRbcLPkSaQOmCIDKH4wP1l9IsWG6l0uEFzAdvo-WCcXSH8C_wKXcOU0ha-RAJUFiUK4tbyLp-0XCr-3TxjSyIN-iBROgwitpMew5n8qZVdLAxNv8BkHT7dSsBpUBLuOWqw84PFLu02dTYjH-wadDwP-3NOSFWpL_xijtqJbJIJ5QHH4_Z6G9TPVG0dKSA9ac5JzqIBeQcf5kGiye3-xtSb61o29IFoQ"));
    nameValuePairs.add(new BasicNameValuePair("grant_type","urn:ietf:params:oauth:grant-type:jwt-bearer"));
    nameValuePairs.add(new BasicNameValuePair("assertion",authCode));
    nameValuePairs.add(new BasicNameValuePair("redirect_uri", "http://arnbu.com:8080/GetVsts/oauth/callback"));
    nameValuePairs.add(new BasicNameValuePair("Content-type","application/x-www-form-urlencoded"));
    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    HttpResponse res = client.execute(httpPost);
    InputStream stream = res.getEntity().getContent();
    
    ObjectMapper mapper = new ObjectMapper();
    
    Authorization auth = mapper.readValue(stream, Authorization.class);
	
    String token = auth.getAccessToken();
    if(token==null){
    	auth = refreshToken();
    }
    
    writeTokensToFile(auth);*/
    
    //System.out.println(auth.getRefreshtoken());
    //getVstsWorkItem(auth.getAccessToken());
	//System.out.println("");
	ArrayList<String> workItems = new ArrayList<String>();
	try {
		workItems = GetBuildDetails.getWorkItems();
	} catch (ParserConfigurationException | SAXException e) {
		e.printStackTrace();
	}
	getAllVstsWorkItems(workItems);
	
}

private void writeTokensToFile(Authorization auth) throws IOException {
	ServletContext servletContext = getServletContext();
	 String path = servletContext.getRealPath("/WEB-INF/");
     File file = new File(path+"/codes.txt");
     file.delete();
	if (!file.exists()) {
		file.createNewFile();
	}
	FileWriter fw = new FileWriter(file.getAbsoluteFile());
	BufferedWriter bw = new BufferedWriter(fw);
	
	bw.write(auth.getAccessToken());
	bw.newLine();
	bw.write(auth.getRefreshtoken());
	bw.close();
	
}

private Authorization refreshToken() throws ClientProtocolException, IOException {
	ServletContext servletContext = getServletContext();
	 String path = servletContext.getRealPath("/WEB-INF/");
	FileReader reader = new FileReader(path+"codes.txt");
	BufferedReader bufferedReader = new BufferedReader(reader);
	
	String accessToken = bufferedReader.readLine();
	String refreshToken = bufferedReader.readLine();
	
	
	String url="https://app.vssps.visualstudio.com/oauth2/token";
	HttpClient client = HttpClientBuilder.create().build();
	HttpPost httpPost = new HttpPost(url);

	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
    nameValuePairs.add(new BasicNameValuePair("client_assertion_type","urn:ietf:params:oauth:client-assertion-type:jwt-bearer"));
    nameValuePairs.add(new BasicNameValuePair("client_assertion","eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Im9PdmN6NU1fN3AtSGpJS2xGWHo5M3VfVjBabyJ9.eyJjaWQiOiI1Yjg3MjkxNC02Yzc1LTRkOGItYTU2ZC1iY2E0MjVlY2U0NTIiLCJjc2kiOiJmMGQ4MjI0NC0yYTNlLTQ5MzgtOTU5Yi00NDA4OWJiODMwMzEiLCJuYW1laWQiOiI4ZjY4Zjc5NS0yNjNkLTRiM2YtYTA5Yy1jOTIyYWI1MDdmNWMiLCJpc3MiOiJhcHAudnNzcHMudmlzdWFsc3R1ZGlvLmNvbSIsImF1ZCI6ImFwcC52c3Nwcy52aXN1YWxzdHVkaW8uY29tIiwibmJmIjoxNDU4MDEwNzAzLCJleHAiOjE2MTU3NzcxMDN9.qH6RF04QBrlmdgO7MyDgBsEjOTKG1gDSKLtICWU8OhcG3D4VFT3z5umltZFjrS0p_6tkGU87vrwniMDg65p8OFNJ0aoG5fbRsLlFAzGan4sIc59-GIXu20LVRbcLPkSaQOmCIDKH4wP1l9IsWG6l0uEFzAdvo-WCcXSH8C_wKXcOU0ha-RAJUFiUK4tbyLp-0XCr-3TxjSyIN-iBROgwitpMew5n8qZVdLAxNv8BkHT7dSsBpUBLuOWqw84PFLu02dTYjH-wadDwP-3NOSFWpL_xijtqJbJIJ5QHH4_Z6G9TPVG0dKSA9ac5JzqIBeQcf5kGiye3-xtSb61o29IFoQ"));
    nameValuePairs.add(new BasicNameValuePair("grant_type","refresh_token"));
    nameValuePairs.add(new BasicNameValuePair("assertion", refreshToken));
    nameValuePairs.add(new BasicNameValuePair("redirect_uri","http://arnbu.com:8080/GetVsts/oauth/callback"));
    nameValuePairs.add(new BasicNameValuePair("Content-type","application/x-www-form-urlencoded"));
    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    HttpResponse res = client.execute(httpPost);
    InputStream stream = res.getEntity().getContent();
    
    ObjectMapper mapper = new ObjectMapper();
    
    Authorization auth = mapper.readValue(stream, Authorization.class);
	
	return auth;
}

private void getVstsWorkItem(String token) throws ClientProtocolException, IOException {
	HttpClient httpClient = HttpClientBuilder.create().build();
    String url = "https://automatedreleasenotes.visualstudio.com/DefaultCollection/_apis/wit/workitems?ids=15&api-version=1.0";
 
    HttpGet httpGet = new HttpGet(url);
    httpGet.setHeader("Accept", "application/json");
    httpGet.setHeader("Authorization", "Bearer "+token);
    HttpResponse response = httpClient.execute(httpGet);
    InputStream responseStream = response.getEntity().getContent();
    ServletContext servletContext = getServletContext();
	String path = servletContext.getRealPath("/WEB-INF/");
    FileOutputStream fos = new FileOutputStream(path+"WorkitemResponse.txt");
    
    int read = 0;
	byte[] bytes = new byte[1024];
	
	while ((read = responseStream.read(bytes)) != -1) {
		fos.write(bytes, 0, read);
	}
}

void getAllVstsWorkItems(ArrayList<String> workItems) throws ClientProtocolException, IOException{
	String commaSeperatedIds = "";
	for(String id : workItems){
		commaSeperatedIds = commaSeperatedIds+id+",";
	}
	commaSeperatedIds = commaSeperatedIds.substring(0, commaSeperatedIds.length()-1);
	getVstsWorkItems(commaSeperatedIds);
	
}
public void getVstsWorkItems(String ids) throws ClientProtocolException, IOException{
  	 HttpClient httpClient = HttpClientBuilder.create().build();
  	 	
       String url = "https://automatedreleasenotes.visualstudio.com/DefaultCollection/_apis/wit/workitems?ids="+ids+"&api-version=1.0";
       String userName = "karunesh";
       String password = "Password@1";
       String authString = userName + ":" + password;
       byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
       String authStringEnc = new String(authEncBytes);

       HttpGet httpGet = new HttpGet(url);
       httpGet.setHeader("Accept", "application/json");
       httpGet.setHeader("Authorization", "Basic " + authStringEnc);
       HttpResponse response = httpClient.execute(httpGet);

       InputStream responseStream = response.getEntity().getContent();
       ServletContext servletContext = getServletContext();
       
       ObjectMapper mapper = new ObjectMapper();
       
       WorkItemResponse workItemResponse = mapper.readValue(responseStream, WorkItemResponse.class);
       
       System.out.println("asd");
       
   		//String path = servletContext.getRealPath("/WEB-INF/");
       /*FileOutputStream fos = new FileOutputStream("c://Users//Karunesh//WorkitemResponse.txt");
       
       int read = 0;
   	byte[] bytes = new byte[1024];
   	
   	while ((read = responseStream.read(bytes)) != -1) {
   		fos.write(bytes, 0, read);
   	}*/
  }
}




