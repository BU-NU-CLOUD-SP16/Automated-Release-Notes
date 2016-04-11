<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<tr>
    <th>
        <label for="argument.arn_version">ARN Version: </label>
    </th>
    <td>
        <props:textProperty name="argument.arn_version" style="width:32em;"/>
        <span class="error" id="error_argument.arn_version"></span>
    </td>
</tr>
<tr>
    <th>
        <label for="argument.build_no">Build No: </label>
    </th>
    <td>
        <props:textProperty name="argument.build_no" style="width:32em;"/>
        <span class="error" id="error_argument.build_no"></span>
        <span class="smallNote">
             Build No.
        </span>
    </td>
</tr>

