<%@ include file="/include-internal.jsp"%>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
    File Path:
    <strong><props:display name="file_path" emptyValue="not specified"/></strong>
</div>

<div class="parameter">
    VSTS URL:
    <strong><props:display name="vsts_url" emptyValue="not specified"/></strong>
</div>

<div class="parameter">
    Teamcity URL:
    <strong><props:display name="tc_url" emptyValue="not specified"/></strong>
</div>

<div class="parameter">
    Format String:
    <strong><props:display name="format_string" emptyValue="not specified"/></strong>
</div>

