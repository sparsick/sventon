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
  <title>Show Thumbnails</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jhighlight.css" >
</head>

<body>
  <%@ include file="/WEB-INF/jspf/pageTop.jspf"%>
  <sventon:currentTargetHeader title="show.thumbnails" target="${command.target}" properties="${properties}"/>

  <form name="searchForm" action="#" method="get" onsubmit="return doSearch(this, '${command.name}', '${command.encodedPath}');">
  <table class="sventonFunctionLinksTable">
    <tr>
      <td style="white-space: nowrap;">
        <sventon:thumbnailsFunctionButtons command="${command}"/>
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

  <br>

  <table style="border-collapse: collapse;">
    <c:forEach items="${thumbnailentries}" var="entry">
      <tr height="160px">
        <s:url value="/repos/${command.name}/get${entry.path}" var="downloadUrl" >
          <s:param name="revision" value="${command.revision}" />
          <s:param name="disp" value="inline" />
        </s:url>
        <s:url value="/repos/${command.name}/get${entry.path}" var="getThumbUrl" >
          <s:param name="revision" value="${command.revision}" />
          <s:param name="disp" value="thumbnail" />
        </s:url>
        <s:url value="/repos/${command.name}/info" var="showRevInfoUrl">
          <s:param name="revision" value="${entry.revision}" />
        </s:url>
        <s:url value="/repos/${command.name}/show${entry.path}" var="showFileUrl">
          <s:param name="revision" value="${command.revision}" />
        </s:url>

        <td valign="top" style="border: 1px dashed black;">
          <spring:message code="file"/>:
          <a href="${showFileUrl}">
            <b>${entry.path}</b>
          </a>
          <br>
          <spring:message code="revision"/>:
          <a href="${showRevInfoUrl}">
            <b>${entry.revision}</b>
          </a>
        </td>
        <td width="210px" style="text-align:center; border: 1px dashed black;">
          <a href="${downloadUrl}">
            <img src="${getThumbUrl}" alt="Thumbnail of ${entry.path} @ ${entry.revision}"></a>
        </td>
      </tr>
    </c:forEach>
  </table>

<%@ include file="/WEB-INF/jspf/pageFoot.jspf"%>
</body>
</html>
