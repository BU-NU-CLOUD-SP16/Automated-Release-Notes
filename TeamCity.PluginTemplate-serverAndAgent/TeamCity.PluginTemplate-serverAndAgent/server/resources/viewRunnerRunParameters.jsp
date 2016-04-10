<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
  <c:choose>
    <c:when test="${not empty propertiesBean.properties['argument.arn_version']}">
      Unity Version: <strong><props:displayValue name="argument.arn_version" /></strong>
    </c:when>
  </c:choose>
</div>

<div class="parameter">
  <c:choose>
    <c:when test="${not empty propertiesBean.properties['arn.build_no']}">
      Unity Executable: <strong><props:displayValue name="arn.build_no" /></strong>
    </c:when>
  </c:choose>
</div>
