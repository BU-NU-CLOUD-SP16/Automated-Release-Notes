import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.map.ObjectMapper;

public class TestService {

    public static void main(String args[]) throws ClientProtocolException, IOException  {
    	
       
    	//getVstsWorkItem();
    	//getWorkItemId();
    	test();
    }
    public static void test() throws ClientProtocolException, IOException{
    	HttpClient httpClient = HttpClientBuilder.create().build(); 
		String url = "https://app.vssps.visualstudio.com/oauth2/authorize?client_id=1C7BB5A5-D464-487C-A1FE-084C56A68954&response_type=Assertion&state=karunesh&scope=vso.build_execute%20vso.chat_manage%20vso.code_manage%20vso.extension.data_write%20vso.extension_manage%20vso.gallery_acquire%20vso.loadtest_write%20vso.packaging_manage%20vso.profile_write%20vso.project_manage%20vso.release_execute%20vso.test_write%20vso.work_write&redirect_uri=http://arnbu.com:8080/GetVsts/oauth/callback";
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpClient.execute(httpGet);
    }
    public static void getVstsWorkItem() throws ClientProtocolException, IOException{
    	 HttpClient httpClient = HttpClientBuilder.create().build();
         String url = "https://automatedreleasenotes.visualstudio.com/DefaultCollection/_apis/wit/workitems?ids=15&api-version=1.0";
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
         System.out.println("Hello");
    }
    public static void getWorkItemId() throws ClientProtocolException, IOException{
    	 HttpClient httpClient = HttpClientBuilder.create().build();
         //String url = "http://hitesh:hitesg@100.73.64.42:9090/httpAuth/app/rest/builds";
    	 //String url ="http://seshank4:Password1@localhost/httpAuth/app/rest";
    	 String url = "http://seshank4:Password1@localhost/httpAuth/app/rest/builds";
         String userName = "seshank4";
         String password = "Password1";
         String authString = userName + ":" + password;
         byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
         String authStringEnc = new String(authEncBytes);

         HttpGet httpGet = new HttpGet(url);
         //httpPost.setHeader("Accept", "application/json");
         //httpPost.setHeader("Authorization", "Basic " + authStringEnc);
         
         HttpResponse response = httpClient.execute(httpGet);

         InputStream responseStream = response.getEntity().getContent();
         System.out.println("Hello");
    }
    }