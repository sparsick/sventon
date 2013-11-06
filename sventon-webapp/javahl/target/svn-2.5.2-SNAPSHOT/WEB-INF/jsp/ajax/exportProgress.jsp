<%
/*
 * ====================================================================
 * Copyright (c) 2005-2012 sventon project. All rights reserved.
 *
 * This software is licensed as described in the file LICENSE, which
 * you should have received as part of this distribution. The terms
 * are also available at http://www.sventon.org.
 * If newer versions of this license are posted there, you may use a
 * newer version instead, at your option.
 * ====================================================================
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="s" uri="/WEB-INF/sventon.tld" %>

<s:url value="/ajax/${command.name}/exportprogress" var="exportDownloadUrl">
  <s:param name="uuid" value="${userRepositoryContext.exportUuid}" />
  <s:param name="download" value="true" />
</s:url>

<s:url value="/ajax/${command.name}/exportprogress" var="exportDeleteUrl">
  <s:param name="uuid" value="${userRepositoryContext.exportUuid}" />
  <s:param name="delete" value="true" />
</s:url>

<c:if test="${userRepositoryContext.isWaitingForExport eq true}">
  <script language="javascript" type="text/javascript">
    jQuery(document).ready(function() {
      jQuery("#progressbar").progressbar({value: ${exportProgress}})
    });
  </script>

  <div style="text-align:center; position:relative; ">
  <div style="width: 100px; margin-left: auto; margin-right: auto;">Exporting... ${exportProgress}&#37;</div>

    <c:if test="${exportProgress eq 100}">
      <a href="${exportDownloadUrl}"><span style="font-weight:bold; vertical-align:middle; color:green;">Click to download!</span></a>
      <a href="#" onclick="deleteExportFile('${exportDeleteUrl}'); return false;">
        <img align="middle" src="images/delete.png" alt="<spring:message code="delete.tooltip"/>"
             title="<spring:message code="delete.tooltip"/>">
      </a>
    </c:if>
  </div>
</c:if>

