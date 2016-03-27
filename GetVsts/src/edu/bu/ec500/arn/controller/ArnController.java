package edu.bu.ec500.arn.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class ArnController extends HttpServlet{
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//String s = (String) request.getAttribute("code");
		getAuthenticationCode();
		//response.sendRedirect("https://app.vssps.visualstudio.com/oauth2/authorize?client_id=1C7BB5A5-D464-487C-A1FE-084C56A68954&response_type=Assertion&state=karunesh&scope=vso.build_execute%20vso.chat_manage%20vso.code_manage%20vso.extension.data_write%20vso.extension_manage%20vso.gallery_acquire%20vso.loadtest_write%20vso.packaging_manage%20vso.profile_write%20vso.project_manage%20vso.release_execute%20vso.test_write%20vso.work_write&redirect_uri=https://arnbu.com:8080/GetVsts/oauth/callback");
	}
	private void getAuthenticationCode() throws ClientProtocolException, IOException {
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		String url = "https://app.vssps.visualstudio.com/oauth2/authorize?client_id=1C7BB5A5-D464-487C-A1FE-084C56A68954&response_type=Assertion&state=karunesh&scope=vso.build_execute%20vso.chat_manage%20vso.code_manage%20vso.extension.data_write%20vso.extension_manage%20vso.gallery_acquire%20vso.loadtest_write%20vso.packaging_manage%20vso.profile_write%20vso.project_manage%20vso.release_execute%20vso.test_write%20vso.work_write&redirect_uri=http://arnbu.com:8080/GetVsts/oauth/callback";
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpClient.execute(httpGet);
		
			System.out.println(response.getAllHeaders());
	}
	public void getVstsWorkItem() throws ClientProtocolException, IOException{
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
}
