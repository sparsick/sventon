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
<%@ include file="/WEB-INF/jspf/pageInclude.jspf"%>

<html>
<head>
  <%@ include file="/WEB-INF/jspf/pageHead.jspf"%>
  <%@ include file="/WEB-INF/jspf/pageHeadRssLink.jspf"%>
  <title>Revision information details</title>
</head>

<body>
  <%@ include file="/WEB-INF/jspf/pageTop.jspf"%>
  <sventon:currentTargetHeader title="revision.info" target="${command.revision}" properties="${properties}"/>

  <form name="searchForm" action="#" method="get" onsubmit="return doSearch(this, '${command.name}', '${command.encodedPath}');">
  <table class="sventonFunctionLinksTable">
    <tr>
      <td style="white-space: nowrap;">
        <sventon:revisionInfoFunctionButtons command="${command}"/>
      </td>
      <td style="text-align: right;">
        <c:if test="${useCache}">
          <sventon:searchField command="${command}" isUpdating="${isUpdating}" isHead="${isHead}" searchMode="${userRepositoryContext.searchMode}"/>
        </c:if>
      </td>
    </tr>
  </table>
    <!-- Needed by ASVNTC -->
    <input type="hidden" name="revision" value="${command.revision}">
  </form>

  <s:url value="/repos/${command.name}/info" var="showPrevRevInfoUrl">
    <s:param name="revision" value="${command.revisionNumber - 1}" />
  </s:url>

  <s:url value="/repos/${command.name}/info" var="showNextRevInfoUrl">
    <s:param name="revision" value="${command.revisionNumber + 1}" />
  </s:url>

  <c:if test="${command.revisionNumber - 1 gt 0}">
    <a href="${showPrevRevInfoUrl}"><img src="images/arrow_left.png" alt="Previous revision" title="<spring:message code="revision.info.previous"/>"></a>
  </c:if>
  <c:if test="${!(command.revisionNumber + 1 gt headRevision)}">
    <a href="${showNextRevInfoUrl}"><img src="images/arrow_right.png" alt="Next revision" title="<spring:message code="revision.info.next"/>"></a>
  </c:if>

  <br>

  <table class="sventonLatestCommitInfoTableWide">
    <tr>
      <td>
        <sventon:revisionInfo name="${command.name}" logEntry="${revisionInfo}" keepVisible="false" linkToHead="false" />
      </td>
    </tr>
  </table>

<%@ include file="/WEB-INF/jspf/pageFoot.jspf"%>
</body>
</html>
