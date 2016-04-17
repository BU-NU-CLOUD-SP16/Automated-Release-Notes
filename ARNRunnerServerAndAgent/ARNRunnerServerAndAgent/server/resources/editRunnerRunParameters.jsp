<%@ include file="/include-internal.jsp"%>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<tr>
    <th>
        <label for="file_path">File Path: </label>
    </th>
    <td>
        <props:textProperty name="file_path" style="width:32em;"/>

    </td>
</tr>

<tr>
    <th>
        <label for="vsts_url">VSTS URL: </label>
    </th>
    <td>
        <props:textProperty name="vsts_url" style="width:32em;"/>
    </td>
</tr>

<tr>
    <th>
        <label for="vsts_user_name">VSTS User Name: </label>
    </th>
    <td>
        <props:textProperty name="vsts_user_name" style="width:32em;"/>
    </td>
</tr>

<tr>
    <th>
        <label for="vsts_password">VSTS Password: </label>
    </th>
    <td>
        <props:passwordProperty name="vsts_password" style="width:32em;"/>
    </td>
</tr>

<tr>
    <th>
        <label for="tc_user_name">Teamcity User Name: </label>
    </th>
    <td>
        <props:textProperty name="tc_user_name" style="width:32em;"/>
    </td>
</tr>

<tr>
    <th>
        <label for="tc_password">Teamcity Password: </label>
    </th>
    <td>
        <props:passwordProperty name="tc_password" style="width:32em;"/>
    </td>
</tr>

<tr>
    <th>
        <label for="format_string">Format String: </label>
    </th>
    <td>
        <props:passwordProperty name="format_string" style="width:32em;"/>
    </td>
</tr>
