package edu.bu.arnplugin.agent;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
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
  private String buildCheckoutDir;

  ARNBuildProcess(@NotNull AgentRunningBuild runningBuild, @NotNull BuildRunnerContext context){

    this.runningBuild = runningBuild;
    this.context = context;
  }
  public void start() throws RunBuildException {
    this.logger = runningBuild.getBuildLogger();
    logger.message("***************************************ARN Build Started************************************************");
    String buildNo = this.runningBuild.getBuildNumber();

    final Map<String,String> configurationParameters = context.getConfigParameters();

    tcUserName = runningBuild.getAccessUser();
    tcPassword = runningBuild.getAccessCode();
    tcURL = configurationParameters.get("teamcity.serverUrl");

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
    //tcURL = runnerParameters.get("tc_url");
    //tcUserName = runnerParameters.get("tc_user_name");
    //tcPassword = runnerParameters.get("tc_password");
    inputFormatString = runnerParameters.get("format_string");
    buildCheckoutDir = runnerParameters.get("teamcity.build.checkoutDir");


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
        getAllVstsWorkItems(workItems, vstsURL, vstsUserName, vstsPassword, runnerParameters);
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

  public void getAllVstsWorkItems(ArrayList<String> workItems, String vstsURL, String vstsUserName, String vstsPassword, Map<String, String> runnerParameters) throws ClientProtocolException{
    String commaSeperatedIds = "";
    Document doc=null;
    PdfWriter writer = null;
    FileOutputStream pdfFileout=null;
    XWPFDocument document=null;
    FileOutputStream wordFos=null;
    FileOutputStream fos=null;
    File file = null;
    PrintWriter out =null;
  try {
    if(runnerParameters.get("text_format") !=null && runnerParameters.get("text_format").equals("true")) {
      //fos = new FileOutputStream(buildCheckoutDir+"\\" + filePath + "\\Release_Notes_Build" + runningBuild.getBuildNumber() + ".txt");
      FileWriter fw = new FileWriter(buildCheckoutDir+"\\" +filePath + "\\Release_Notes_Build"+runningBuild.getBuildNumber()+".txt",true);
      BufferedWriter bw = new BufferedWriter(fw);
      out = new PrintWriter(bw);
    }
    if (runnerParameters.get("doc_format") != null && runnerParameters.get("doc_format").equals("true")) {
      document = new XWPFDocument();
      wordFos = new FileOutputStream(buildCheckoutDir + "\\" + filePath + "\\Release_Notes_Build" + runningBuild.getBuildNumber() + ".doc");
    }

    File fileDir = new File(buildCheckoutDir + "\\" + filePath);
    logger.message("fileDir" + fileDir.toString());
    if (!fileDir.exists()) {
      fileDir.mkdir();
      logger.message("inside make dir");
    }
    if (runnerParameters.get("pdf_format") != null && runnerParameters.get("pdf_format").equals("true")) {
      file = new File(buildCheckoutDir + "\\" + filePath + "\\Release_Notes_Build" + runningBuild.getBuildNumber() + ".pdf");
      pdfFileout = new FileOutputStream(file);
      doc = new Document();

      try {
        writer = PdfWriter.getInstance(doc, pdfFileout);
      } catch (DocumentException e) {
        e.printStackTrace();
      }
      doc.addAuthor("Mingle Analytics");
      doc.addTitle("Automated Release Notes");
      doc.open();
    }
    for (String id : workItems) {
      //commaSeperatedIds = commaSeperatedIds+id+",";
      getVstsWorkItems(id, runnerParameters, doc, document,out);
    }
    if (doc != null) {
      try {
        doc.add(new Chunk(""));
      } catch (DocumentException e) {
        e.printStackTrace();
      }
      doc.close();
    }
    if (wordFos != null) {
      document.write(wordFos);
    }
    if (wordFos != null) {
      wordFos.close();
    }
    if(out!=null) {
      out.close();
    }
    //jetbrains.buildServer.agent.BuildProgressLogger logger = runningBuild.getBuildLogger();
    //logger.message("commaSeperatedIds :"+commaSeperatedIds);
    //commaSeperatedIds = commaSeperatedIds.substring(0, commaSeperatedIds.length()-1);
  }catch (IOException e){
    logger.message("File exception");
  }finally {
    if (doc != null) {
      try {
        doc.add(new Chunk(""));
      } catch (DocumentException e) {
        e.printStackTrace();
      }
      doc.close();
    }
    if (wordFos != null) {
      try {
        wordFos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if(out!=null) {
      out.close();
    }
  }

  }
  public void getVstsWorkItems(String ids, Map<String, String> runnerParameters, Document doc, XWPFDocument document, PrintWriter out) throws ClientProtocolException, IOException, JsonMappingException {
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
    if(response !=null) {
      if(response.getStatusLine() !=null) {
        logger.message("vsts response : " + response.getStatusLine().getStatusCode());
      }
      if(response.getEntity() !=null) {
        InputStream responseStream = response.getEntity().getContent();

    ObjectMapper mapper = new ObjectMapper();
      WorkItemResponse workItemResponse = mapper.readValue(responseStream, WorkItemResponse.class);

      if(workItemResponse!=null) {
        if(workItemResponse.getValues() !=null && workItemResponse.getValues().size() >0 ) {
          logger.message("size : " + workItemResponse.getValues().size());
          logger.message(workItemResponse.getValues().get(0).getId());
          try {
            createFormattedFile(workItemResponse, runnerParameters, doc,document,out);
          } catch (DocumentException e) {
            e.printStackTrace();
          }
        }

      }

    jetbrains.buildServer.agent.BuildProgressLogger logger = runningBuild.getBuildLogger();

    logger.message("getting work items");
      }
    }
  }

  private void createFormattedFile(WorkItemResponse workItemResponse, Map<String, String> runnerParameters, Document doc, XWPFDocument document, PrintWriter out) throws IOException, DocumentException {


    String wordContent = "";

    //text file


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
        wordContent = content;
        content = content.replaceAll("\\n",eol)+eol+eol;

      }


      logger.message("content : "+content);

      if(runnerParameters.get("text_format") != null && runnerParameters.get("text_format").equals("true")) {
        /* byte[] b1 = content.getBytes();
         fos.write(b1);*/
        out.print(content);
        out.println("");

        out.flush();
        logger.message("done");
      }

      if(runnerParameters.get("pdf_format") != null && runnerParameters.get("pdf_format").equals("true")) {
        try {

          Paragraph para = new Paragraph();
          para.add(content + eol + eol);
          doc.add(para);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      if(runnerParameters.get("doc_format")!= null && runnerParameters.get("doc_format").equals("true")) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        //run.setFontSize(18);

        if (wordContent.contains("\n")) {
          logger.message("in the if");
          String[] lines = wordContent.split("\n");
          run.setText(lines[0], 0); // set first line into XWPFRun
          for (int j = 1; j < lines.length; j++) {
            // add break and insert new text
            run.addBreak();
            run.setText(lines[j]);
          }
        } else {
          logger.message("in the else");
          run.setText(wordContent, 0);
        }
      }

    }



   /* if(doc!=null) {
      doc.close();
    }
    if(pdfFileout!=null) {
      pdfFileout.close();
    }*/

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
