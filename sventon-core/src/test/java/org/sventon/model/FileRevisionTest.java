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
package org.sventon.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileRevisionTest {
  private static final String THE_AUTHOR = "Edgar Allan Poe";

  @Test
  public void properties() throws Exception {
    final FileRevision revision = new FileRevision("/poems/The Raven", Revision.create(1));

    revision.addProperty(RevisionProperty.AUTHOR, THE_AUTHOR);
    revision.addProperty(RevisionProperty.DATE, "1845-01-29");

    final String author = revision.getProperty(RevisionProperty.AUTHOR);

    assertEquals(THE_AUTHOR, author);
  }
}
