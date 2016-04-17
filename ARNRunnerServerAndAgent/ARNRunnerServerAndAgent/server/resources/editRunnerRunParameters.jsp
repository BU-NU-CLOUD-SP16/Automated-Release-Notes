<%@ include file="/include-internal.jsp"%>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<tr>
<th>
ARN Runner Parameters **********************************
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
        <label for="vsts_url">Teamcity URL: </label>
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
        Format String:
    </th>
    <td>
        <props:multilineProperty name="format_string" rows="5" cols="50" linkTitle="Format String Template" expanded="true"  />
        <span class="smallNote">
               Enter a format string similar to the one below : <br />
               Work Item: \${WorkItemId} - \${WorkItemTitle} <br />
                \${WorkItemDescription}<br />
                Done by: \${WorkItemAssignedTo}<br />
                Story Points: \${WorkItemStoryPoints} <br />
         </span>
         <span class="error" id="error_format_string"></span>
    </td>
</tr>
