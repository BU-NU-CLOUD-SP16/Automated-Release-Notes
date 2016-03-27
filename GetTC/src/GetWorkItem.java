

import java.io.File;
import java.io.FileOutputStream;
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

public class GetWorkItem {
	
	public static void main(String args[]) throws ClientProtocolException, IOException {
		
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
    
    FileOutputStream fos = new FileOutputStream("src/WorkitemResponse.txt");
    
    int read = 0;
	byte[] bytes = new byte[1024];
	
	while ((read = responseStream.read(bytes)) != -1) {
		fos.write(bytes, 0, read);
	}
    
	}
}
