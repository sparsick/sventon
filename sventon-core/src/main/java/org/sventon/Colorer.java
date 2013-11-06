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
package org.sventon;

import java.io.IOException;

/**
 * Colorer interface. Used for converting source files into colorized <code>HTML</code> code.
 *
 * @author jesper@sventon.org
 */
public interface Colorer {

  /**
   * Converts given contents into colorized HTML code.
   *
   * @param content       The contents.
   * @param fileExtension The filename, used to determine formatter.
   * @param encoding      Encoding
   * @return The <code>HTML</code> formatted, colorized, string. If no suitable
   *         formatter was found for given file extension the content will still
   *         be formatted with <code>HTML</code> entities to be properly displayed on the web.
   *         If given content was <code>null</code> an empty string will be returned.
   * @throws IOException if unable to colorize content.
   */
  String getColorizedContent(final String content, final String fileExtension, final String encoding) throws IOException;
}
