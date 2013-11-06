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
package org.sventon.util;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.sventon.util.WebUtils.NL;

public class WebUtilsTest {

  @Test
  public void testNl2br() throws Exception {
    assertEquals("one<br>two", WebUtils.nl2br("one\ntwo"));
  }

  @Test
  public void testReplaceLeadingSpaces() throws Exception {
    try {
      WebUtils.replaceLeadingSpaces(null);
    } catch (NullPointerException npe) {
      // expected
    }
    assertEquals("", WebUtils.replaceLeadingSpaces(""));
    assertEquals("&nbsp;c", WebUtils.replaceLeadingSpaces(" c"));
    assertEquals("&nbsp;&nbsp;class {", WebUtils.replaceLeadingSpaces("  class {"));
  }

  @Test
  public void testReplaceLeadingSpacesMultiline() throws Exception {
    String line = " one " +
        NL + "  two  " +
        NL + "          " +
        NL + " three  four" + NL;

    String expected = "&nbsp;one " +
        NL + "&nbsp;&nbsp;two  " +
        NL + "          " +
        NL + "&nbsp;three  four" + NL;

    assertEquals(expected, WebUtils.replaceLeadingSpaces(line));

    line = NL + " one " +
        NL + "  two  ";

    expected = NL + "&nbsp;one " +
        NL + "&nbsp;&nbsp;two  " + NL;

    assertEquals(expected, WebUtils.replaceLeadingSpaces(line));
  }

  @Test
  public void testExtractBaseURLFromRequest() throws Exception {
    MockHttpServletRequest request;

    request = new MockHttpServletRequest("GET", "foo/bar");
    request.setServerName("www.test.com");
    request.setServerPort(80);
    assertEquals("http://www.test.com/", WebUtils.extractBaseURLFromRequest(request));

    request = new MockHttpServletRequest("GET", "foo/bar");
    request.setServerName("www.test.com");
    request.setServerPort(123);
    assertEquals("http://www.test.com:123/", WebUtils.extractBaseURLFromRequest(request));
  }

}
