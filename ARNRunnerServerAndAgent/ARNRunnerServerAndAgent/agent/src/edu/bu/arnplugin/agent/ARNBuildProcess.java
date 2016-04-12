package edu.bu.arnplugin.agent;

import edu.bu.arnplugin.bean.Authorization;
import edu.bu.arnplugin.bean.WorkItemResponse;
import edu.bu.arnplugin.tc.GetBuildDetails;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.vcs.VcsChangeInfo;
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
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ganesh on 4/8/2016.
 */
public class ARNBuildProcess implements BuildProcess {

  private final AgentRunningBuild runningBuild;
  private final BuildRunnerContext context;
  jetbrains.buildServer.agent.BuildProgressLogger logger;
  String filePath;

  ARNBuildProcess(@NotNull AgentRunningBuild runningBuild, @NotNull BuildRunnerContext context){

    this.runningBuild = runningBuild;
    this.context = context;
  }
  public void start() throws RunBuildException {
    this.logger = runningBuild.getBuildLogger();
    logger.message("***************************************ARN Build Started************************************************");
    String buildNo = this.runningBuild.getBuildNumber();

    logger.message("Build No. :"+ buildNo);
    ArrayList<String> workItems = new ArrayList<String>();
    final Map<String, String> runnerParameters = context.getRunnerParameters();
    for(String s : runnerParameters.keySet()){
        logger.message("Key : "+ s + "value : "+ runnerParameters.get(s));
    }

    final Map<String, String> parameters = context.getBuildParameters().getAllParameters();
    for(String s : parameters.keySet()){
      logger.message("Key Build : "+ s + "value : "+ parameters.get(s));
    }
    this.filePath = parameters.get("system.agent.work.dir");
    logger.message("filePath :"+this.filePath);
    logger.message("getting work items");
    try {
      workItems = GetBuildDetails.getWorkItems(buildNo,logger);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }
    logger.message("fetched work items");

    for(String x : workItems){
      logger.message("------> " + x);
    }

    try {
      if(workItems.size()>0) {
        getAllVstsWorkItems(workItems);
      }else{
        logger.message("No Changes in the current build");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }



  private void writeTokensToFile(Authorization auth) throws IOException {

    //String path = this.context.getRealPath("/WEB-INF/");
    File file = new File("codes.txt");
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

//ServletContext servletContext = getServletContext();
    //String path = servletContext.getRealPath("/WEB-INF/");

    FileReader reader = new FileReader("codes.txt");
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

    Authorization auth = null;



      auth = mapper.readValue(stream, Authorization.class);



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

//String path = servletContext.getRealPath("/WEB-INF/");
    //ServletContext servletContext = getServletContext();

    FileOutputStream fos = new FileOutputStream("WorkitemResponse.txt");

    int read = 0;
    byte[] bytes = new byte[1024];

    while ((read = responseStream.read(bytes)) != -1) {
      fos.write(bytes, 0, read);
    }
  }

  public void getAllVstsWorkItems(ArrayList<String> workItems) throws ClientProtocolException, IOException{
    String commaSeperatedIds = "";
    for(String id : workItems){
      commaSeperatedIds = commaSeperatedIds+id+",";
    }
    jetbrains.buildServer.agent.BuildProgressLogger logger = runningBuild.getBuildLogger();
    logger.message("commaSeperatedIds :"+commaSeperatedIds);
    commaSeperatedIds = commaSeperatedIds.substring(0, commaSeperatedIds.length()-1);
    getVstsWorkItems(commaSeperatedIds);

  }
  public void getVstsWorkItems(String ids) throws ClientProtocolException, IOException, JsonMappingException {
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
    logger.message("vsts response : "+ response.getStatusLine().getStatusCode());
    InputStream responseStream = response.getEntity().getContent();

/*ServletContext servletContext = getServletContext();*/


    ObjectMapper mapper = new ObjectMapper();


      WorkItemResponse workItemResponse = mapper.readValue(responseStream, WorkItemResponse.class);

      if(workItemResponse!=null) {
        logger.message("size : "+workItemResponse.getValues().size());
        logger.message(workItemResponse.getValues().get(0).getId());
        createFormattedFile(workItemResponse);


      }
    /*System.out.println("asd");

    //String path = servletContext.getRealPath("/WEB-INF/");
       FileOutputStream fos = new FileOutputStream("C:\\TeamCity\\WorkitemResponse.txt");

       int read = 0;
   	byte[] bytes = new byte[1024];

   	while ((read = responseStream.read(bytes)) != -1) {
   		fos.write(bytes, 0, read);
   	}*/
    jetbrains.buildServer.agent.BuildProgressLogger logger = runningBuild.getBuildLogger();

    logger.message("getting work items");
  }

  private void createFormattedFile(WorkItemResponse workItemResponse) throws IOException {



      /*logger.message("i ="+i +" :"+ filePath) ;
      File file = new File(filePath+"\\changes.txt");
      try {
       boolean isCreated = file.createNewFile();
        logger.message("File created :"+isCreated);
      } catch (IOException e) {
        e.printStackTrace();
      }
      BufferedReader bf = null;
      try {
        bf = new BufferedReader(new FileReader(file));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      String preContent = "";
      StringBuilder sb = new StringBuilder("");

      try {
        while (preContent != null) {
          preContent = bf.readLine();
          sb.append(preContent);
          sb.append("\n");
        }
        bf.close();
      }catch (IOException e){
        e.printStackTrace();
      }
      logger.message("sb.toString : "+sb.toString());
      byte[] b2 = sb.toString().getBytes();*/

      FileOutputStream fos = new FileOutputStream(filePath+"\\changes.txt");

    for(int i=0;i<workItemResponse.getValues().size();i++){

      String id = workItemResponse.getValues().get(i).getId();
      String description = workItemResponse.getValues().get(i).getFields().getDescription();
      String title = workItemResponse.getValues().get(i).getFields().getTitle();
      String content;
      String eol = System.getProperty("line.separator");
      content = "Work Item Id: " +id + eol + "Title: " + title  + eol +"Description: " + description + eol + eol;
      logger.message("content : "+content);
      try {

        byte[] b1 = content.getBytes();
        fos.write(b2);
        fos.write(b1);


        logger.message("done");
      }catch (IOException e){
        e.printStackTrace();
      }

    }
    fos.close();

  }


  public boolean isInterrupted() {
    return false;
  }

  public boolean isFinished() {

    return false;
  }

  public void interrupt() {

  }

  @NotNull
  public BuildFinishedStatus waitFor() throws RunBuildException {
    jetbrains.buildServer.agent.BuildProgressLogger logger = runningBuild.getBuildLogger();
    logger.message("***************************************ARN Build finished************************************************");

    return BuildFinishedStatus.FINISHED_SUCCESS;
  }
}
