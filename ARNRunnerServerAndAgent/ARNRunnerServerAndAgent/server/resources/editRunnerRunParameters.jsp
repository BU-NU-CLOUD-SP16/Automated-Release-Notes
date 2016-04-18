<%@ include file="/include-internal.jsp"%>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>

<script src="/plugins/ARNServerPlugin/js/script.js"></script>
<style>
.buttonStyle{
  width : 32.5em;
  background-color : #3B5998;
  color : #ffffff;
}

.helpDiv{
  display:none;
  border:thin solid #000000;
  width:32.5em;
}

</style>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<tr>
<th>
**** ARN Runner Parameters ****
</th>
</tr>
<tr>
    <th>
        <label for="file_path">File Path: </label>
    </th>
    <td>
        <props:textProperty name="file_path" style="width:32em;"/>
        <span class="error" id="error_file_path"></span>
    </td>
</tr>

<tr>
    <th>
        <label for="vsts_url">VSTS URL: </label>
    </th>
    <td>
        <props:textProperty name="vsts_url" style="width:32em;"/>
        <span class="error" id="error_vsts_url"></span>
    </td>
</tr>

<tr>
    <th>
        <label for="vsts_user_name">VSTS User Name: </label>
    </th>
    <td>
        <props:textProperty name="vsts_user_name" style="width:32em;"/>
        <span class="error" id="error_vsts_user_name"></span>
    </td>
</tr>

<tr>
    <th>
        <label for="vsts_password">VSTS Password: </label>
    </th>
    <td>
        <props:passwordProperty name="vsts_password" style="width:32em;"/>
        <span class="error" id="error_vsts_password"></span>
    </td>
</tr>

<tr>
    <th>
        <label for="tc_url">Teamcity URL: </label>
    </th>
    <td>
        <props:textProperty name="tc_url" style="width:32em;"/>
        <span class="error" id="error_tc_url"></span>
    </td>
</tr>

<tr>
    <th>
        <label for="tc_user_name">Teamcity User Name: </label>
    </th>
    <td>
        <props:textProperty name="tc_user_name" style="width:32em;"/>
        <span class="error" id="error_tc_user_name"></span>
    </td>
</tr>

<tr>
    <th>
        <label for="tc_password">Teamcity Password: </label>
    </th>
    <td>
        <props:passwordProperty name="tc_password" style="width:32em;"/>
        <span class="error" id="error_tc_password"></span>
    </td>
</tr>

<tr>
    <th>
        Release Notes file formats: </label>
    </th>
    <td>
        <props:checkboxProperty name="text_format" /> Text
        <span class="error" id="error_text_format"></span>
        <props:checkboxProperty name="pdf_format" /> PDF
        <span class="error" id="error_pdf_format"></span>
        <props:checkboxProperty name="doc_format" /> Doc
        <span class="error" id="error_doc_format}"></span>
    </td>
</tr>

<tr>
    <th>
        Format String:
    </th>
    <td>
        <props:multilineProperty name="format_string" rows="5" cols="50" linkTitle="Format String Template" expanded="true"  />
        <span class="smallNote">

         </span>
         <span class="error" id="error_format_string"></span>
         <button type="button" class="buttonStyle" id="helpButton" onclick="showDiv()">Click for information about format strings</button>
         <button type="button" class="buttonStyle" id="lessHelpButton" onclick="hideDiv()" style="display:none">Hide</button>

         <div id="helpDiv" class="helpDiv">
         <span> Enter a format string similar to the one below : <br /><br/>
                           Work Item: \${WorkItemId} - \${WorkItemTitle} <br />
                            \${WorkItemDescription}<br />
                            Done by: \${WorkItemAssignedTo}<br />
                            Story Points: \${WorkItemStoryPoints} <br /></span>

          <p>
          Following is the list of supported parameters that you can include in your Format String :

          <ul>
          <li>\${WorkItemId}</li>
          <li>\${WorkItemTitle}</li>
          <li>\${WorkItemDescription}</li>
          <li>\${WorkItemAssignedTo}</li>
          <li>\${WorkItemStoryPoints}</li>
          <li>\${TeamProject}</li>
          <li>\${WorkItemType}</li>
          <li>\${WorkItemState}</li>
          <li>\${WorkItemPriority}</li>
          <li>\${WorkItemRisk}</li>
          <li>\${WorkItemArea}</li>
          <li>\${WorkItemIteration}</li>
          <li>\${WorkItemLastUpdateBy}</li>
          <li>\${WorkItemStatusReason}</li>
          <li>\${WorkItemTags}</li>
          <li>\${WorkItemAcceptanceCriteria}</li>
          <li>\${WorkItemValueArea}</li>
          </ul>

          </p>
         </div>
    </td>

</tr>
