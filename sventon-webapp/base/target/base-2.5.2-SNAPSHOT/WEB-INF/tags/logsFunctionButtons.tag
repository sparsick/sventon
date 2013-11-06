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
<%@ tag body-content="empty" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/WEB-INF/sventon.tld" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="command" required="true" type="org.sventon.web.command.BaseCommand" %>
<%@ attribute name="isFile" required="true" type="java.lang.Boolean" %>

<s:url var="showFileUrl" value="/repos/${command.name}/show${command.path}">
  <s:param name="revision" value="${command.revision}" />
</s:url>
<s:url var="showDirLinkUrl" value="/repos/${command.name}/list${command.path}">
  <s:param name="revision" value="${command.revision}" />
</s:url>
<s:url var="downloadLinkUrl" value="/repos/${command.name}/get${command.path}">
  <s:param name="revision" value="${command.revision}" />
</s:url>
<s:url var="showLogToggleSoCUrl" value="/repos/${command.name}/log${command.path}">
  <s:param name="revision" value="${command.revision}"/>
  <s:param name="paging" value="${paging}"/>
  <s:param name="stopOnCopy" value="${!stopOnCopy}"/>
  <c:if test="${nextPath ne null}">
    <s:param name="nextPath" value="${nextPath}" />
  </c:if>
  <c:if test="${nextRevision ne null}">
    <s:param name="nextRevision" value="${nextRevision}" />
  </c:if>
</s:url>
<s:url var="showLogTogglePagingUrl" value="/repos/${command.name}/log${command.path}">
  <s:param name="revision" value="${command.revision}"/>
  <s:param name="stopOnCopy" value="${stopOnCopy}"/>
  <s:param name="paging" value="${!paging}"/>
  <c:if test="${nextPath ne null}">
    <s:param name="nextPath" value="${nextPath}" />
  </c:if>
  <c:if test="${nextRevision ne null}">
    <s:param name="nextRevision" value="${nextRevision}" />
  </c:if>
</s:url>

<c:choose>
  <c:when test="${isFile}">
    <input type="button" class="btn" value="<spring:message code="showfile.button.text"/>" onclick="document.location.href='${showFileUrl}';">
    <input type="button" class="btn" value="<spring:message code="download.button.text"/>" onclick="document.location.href='${downloadLinkUrl}';">
  </c:when>
  <c:otherwise>
    <input type="button" class="btn" value="<spring:message code="showdir.button.text"/>" onclick="document.location.href='${showDirLinkUrl}';" onmouseover="Tip('<spring:message code="showdir.button.tooltip" arguments="${command.encodedPath}"/>')">
  </c:otherwise>
</c:choose>
<input type="button" class="btn" value="<spring:message code="show-all-details.button.text"/>" onclick="toggleLogDetails(${fn:length(logEntriesPage)}); toggleButtonText(this, '<spring:message code="show-all-details.button.text"/>', '<spring:message code="hide-all-details.button.text"/>');"/>
<input type="checkbox" <c:if test="${stopOnCopy}">checked="checked"</c:if> onclick="document.location.href='${showLogToggleSoCUrl}';"/><span class="funcText"><spring:message code="stop-on-copy.button.text"/></span>
<input type="checkbox" <c:if test="${paging}">checked="checked"</c:if> onclick="document.location.href='${showLogTogglePagingUrl}';"/><span class="funcText"><spring:message code="paging.button.text"/></span>
