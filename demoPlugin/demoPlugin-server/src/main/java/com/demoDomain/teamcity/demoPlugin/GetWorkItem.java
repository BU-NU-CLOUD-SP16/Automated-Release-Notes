package com.demoDomain.teamcity.demoPlugin;


import org.apache.commons.httpclient.UsernamePasswordCredentials;
import sun.net.www.http.HttpClient;
import sun.security.krb5.Credentials;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Karunesh on 2/22/2016.
 */
public class GetWorkItem {
    public static void main(String[] args) throws Exception {

        
        String httpsURL = "https://automatedreleasenotes.visualstudio.com/DefaultCollection/55e3d4dc-3b67-4463-ae18-61450e577049/_apis/wit/wiql/7ca56d29-f35b-4c40-bb8c-ea9a5889424e";
        URL myurl = new URL(httpsURL);
        HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
        InputStream ins = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(ins);
        BufferedReader in = new BufferedReader(isr);

        String inputLine;

        while ((inputLine = in.readLine()) != null)
        {
            System.out.println(inputLine);
        }

        in.close();

    }

}
