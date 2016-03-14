
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


	public class GetTeamCityInfo {
		public static void main(String args[]) throws ClientProtocolException, IOException {
			HttpClient httpClient = HttpClientBuilder.create().build();
		    String url = "http://localhost/httpAuth/app/rest/changes?locator=build:(number:8)&fields=count,change:(comment)";
		    String userName = "hitesh";
		    String password = "hitesh";
		    String authString = userName + ":" + password;
		    byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		    String authStringEnc = new String(authEncBytes);
		    
		    HttpGet httpGet = new HttpGet(url);
		    //httpPost.setHeader("Accept", "application/json");
		    httpGet.setHeader("Authorization", "Basic " + authStringEnc);
		    
		    HttpResponse response = httpClient.execute(httpGet);
		    InputStream responseStream = response.getEntity().getContent();
		    System.out.println("Hello");
		}

	}
