package edu.bu.arnplugin.agent;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
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

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
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
  private String filePath;
  private String vstsURL;
  private String vstsUserName;
  private  String vstsPassword;
  private  String tcURL;
  private String tcUserName;
  private String tcPassword;
  private String inputFormatString;

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



    vstsURL = runnerParameters.get("vsts_url");
    vstsUserName = runnerParameters.get("vsts_user_name");
    vstsPassword = runnerParameters.get("vsts_password");
    tcURL = runnerParameters.get("tc_url");
    tcUserName = runnerParameters.get("tc_user_name");
    tcPassword = runnerParameters.get("tc_password");
    inputFormatString = runnerParameters.get("format_string");

    for(String s : parameters.keySet()){
      logger.message("Key Build : "+ s + "value : "+ parameters.get(s));
    }
    //this.filePath = parameters.get("system.agent.work.dir");
    this.filePath = runnerParameters.get("file_path");
    logger.message("filePath :"+this.filePath);
    logger.message("getting work items");

    try {
      workItems = GetBuildDetails.getWorkItems(buildNo,logger, tcURL, tcUserName, tcPassword);
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
        getAllVstsWorkItems(workItems, vstsURL, vstsUserName, vstsPassword);
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

    if(response.getStatusLine().getStatusCode()!=200){
      logger.error("Incorrect VSTS URL/Credentials");
    }

//String path = servletContext.getRealPath("/WEB-INF/");
    //ServletContext servletContext = getServletContext();

    FileOutputStream fos = new FileOutputStream("WorkitemResponse.txt");

    int read = 0;
    byte[] bytes = new byte[1024];

    while ((read = responseStream.read(bytes)) != -1) {
      fos.write(bytes, 0, read);
    }
  }

  public void getAllVstsWorkItems(ArrayList<String> workItems, String vstsURL, String vstsUserName, String vstsPassword) throws ClientProtocolException, IOException{
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

    String url = vstsURL+"/DefaultCollection/_apis/wit/workitems?ids="+ids+"&api-version=1.0";
    String userName = vstsUserName;
    String password = vstsPassword;
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
        try {
          createFormattedFile(workItemResponse);
        } catch (DocumentException e) {
          e.printStackTrace();
        }


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

  private void createFormattedFile(WorkItemResponse workItemResponse) throws IOException, DocumentException {



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

    //text file
    FileOutputStream fos = new FileOutputStream(filePath+"\\Release_Notes_Build"+runningBuild.getBuildNumber()+".txt");

    //pdf file
    File file = new File(filePath+"\\Release_Notes_Build"+runningBuild.getBuildNumber()+".pdf");
    FileOutputStream pdfFileout = new FileOutputStream(file);
    Document doc = new Document();
    PdfWriter.getInstance(doc, pdfFileout);
    doc.addAuthor("Karunesh Mahajan");
    doc.addTitle("This is title");
    doc.open();

    //word doc
    String wordContent=null;
    XWPFDocument document = new XWPFDocument();
    FileOutputStream wordFos = new FileOutputStream(filePath+"\\Release_Notes_Build"+runningBuild.getBuildNumber()+".doc");
    XWPFParagraph paragraph = document.createParagraph();



    for(int i=0;i<workItemResponse.getValues().size();i++){

      String id = workItemResponse.getValues().get(i).getId();
      String description = workItemResponse.getValues().get(i).getFields().getDescription();
      String title = workItemResponse.getValues().get(i).getFields().getTitle();
      String assignedTo = workItemResponse.getValues().get(i).getFields().getAssignedTo();
      String storyPoints = workItemResponse.getValues().get(i).getFields().getStoryPoints();
      String teamProject = workItemResponse.getValues().get(i).getFields().getTeamProject();
      String workItemType = workItemResponse.getValues().get(i).getFields().getType();
      String state = workItemResponse.getValues().get(i).getFields().getState();
      String priority = workItemResponse.getValues().get(i).getFields().getPriority();
      String risk = workItemResponse.getValues().get(i).getFields().getRisk();
      String area = workItemResponse.getValues().get(i).getFields().getArea();
      String iteration = workItemResponse.getValues().get(i).getFields().getIteration();
      String lastUpdatedBy = workItemResponse.getValues().get(i).getFields().getLastUpdatedBy();
      String statusReason = workItemResponse.getValues().get(i).getFields().getStatusReason();
      String workItemTag = workItemResponse.getValues().get(i).getFields().getWorkItemTags();
      String acceptanceCriteria = workItemResponse.getValues().get(i).getFields().getAcceptanceCriteria();
      String valueArea = workItemResponse.getValues().get(i).getFields().getWorkItemValueArea();


      String content;
      String eol = System.getProperty("line.separator");
      String tempFormatString = this.inputFormatString;

      if(tempFormatString==null){
        content = "Work Item Id: " +id + eol + "Title: " + title  + eol +"Description: " + description + eol + eol;
      }
      else {
        if(id!=null) {
          tempFormatString = tempFormatString.replace("${WorkItemId}", id);
        } else {
          tempFormatString = tempFormatString.replace("${WorkItemId}", "");
        }
        if(title !=null) {
          tempFormatString = tempFormatString.replace("${WorkItemTitle}", title);
        } else {
          tempFormatString = tempFormatString.replace("${WorkItemTitle}", "");
        }
        if(description != null) {
          tempFormatString = tempFormatString.replace("${WorkItemDescription}", description);
        } else{
          tempFormatString = tempFormatString.replace("${WorkItemDescription}", "");
        }
        if(assignedTo != null) {
          tempFormatString = tempFormatString.replace("${WorkItemAssignedTo}", assignedTo);
        }else {
          tempFormatString = tempFormatString.replace("${WorkItemAssignedTo}", "");
        }
        if(storyPoints != null) {
          tempFormatString = tempFormatString.replace("${WorkItemStoryPoints}", storyPoints);
        } else {
          tempFormatString = tempFormatString.replace("${WorkItemStoryPoints}", "");
        }
        if(teamProject != null) {
          tempFormatString = tempFormatString.replace("${TeamProject}", teamProject);
        } else {
          tempFormatString = tempFormatString.replace("${TeamProject}", "");
        }
        if(workItemType != null) {
          tempFormatString = tempFormatString.replace("${WorkItemType}", workItemType);
        } else {
          tempFormatString = tempFormatString.replace("${WorkItemType}", "");
        }
        if(state != null) {
          tempFormatString = tempFormatString.replace("${WorkItemState}", state);
        } else {
          tempFormatString = tempFormatString.replace("${WorkItemState}", "");
        }
        if(priority != null) {
          tempFormatString = tempFormatString.replace("${WorkItemPriority}", priority);
        } else {
          tempFormatString = tempFormatString.replace("${WorkItemPriority}", "");
        }
        if(risk != null) {
          tempFormatString = tempFormatString.replace("${WorkItemRisk}", risk);
        } else {
          tempFormatString = tempFormatString.replace("${WorkItemRisk}", "");
        }
        if(area != null) {
          tempFormatString = tempFormatString.replace("${WorkItemArea}", area);
        } else {
          tempFormatString = tempFormatString.replace("${WorkItemArea}", "");
        }
        if(iteration != null) {
          tempFormatString = tempFormatString.replace("${WorkItemIteration}", iteration);
        } else {
          tempFormatString = tempFormatString.replace("${WorkItemIteration}", "");
        }
        if(lastUpdatedBy != null) {
          tempFormatString = tempFormatString.replace("${WorkItemLastUpdateBy}", lastUpdatedBy);
        } else {
          tempFormatString = tempFormatString.replace("${WorkItemLastUpdateBy}", "");
        }
        if(statusReason != null) {
          tempFormatString = tempFormatString.replace("${WorkItemStatusReason}", statusReason);
        } else {
          tempFormatString = tempFormatString.replace("${WorkItemStatusReason}", "");
        }
        if(workItemTag != null) {
          tempFormatString = tempFormatString.replace("${WorkItemTags}", workItemTag);
        } else {
          tempFormatString = tempFormatString.replace("${WorkItemTags}", "");
        }
        if(acceptanceCriteria != null) {
          tempFormatString = tempFormatString.replace("${WorkItemAcceptanceCriteria}", acceptanceCriteria);
        } else {
          tempFormatString = tempFormatString.replace("${WorkItemAcceptanceCriteria}", "");
        }
        if(valueArea != null) {
          tempFormatString = tempFormatString.replace("${WorkItemValueArea}", valueArea);
        } else {
          tempFormatString = tempFormatString.replace("${WorkItemValueArea}", "");
        }

        content = tempFormatString;
        content = content.replaceAll("\\n",eol)+eol+eol;
      }

      logger.message("content : "+content);


      try {
        byte[] b1 = content.getBytes();
        fos.write(b1);
        logger.message("done");
      }catch (IOException e){
        e.printStackTrace();
      }

      try {
        Paragraph para = new Paragraph();
        para.add(content+eol+eol);
        doc.add(para);
      } catch (Exception e) {
        e.printStackTrace();
      }

      wordContent = wordContent+eol+content;


    }

    XWPFRun run = paragraph.createRun();
    //run.setFontSize(18);
    run.setText(wordContent);
    document.write(wordFos);

    fos.close();
    doc.close();
    pdfFileout.close();
    wordFos.close();
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
