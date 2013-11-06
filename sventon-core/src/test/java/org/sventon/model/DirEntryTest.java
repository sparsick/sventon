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

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DirEntryTest {

  @Test
  public void testGetShortenedFullEntryName() throws Exception {
    DirEntry entry;

    entry = toEntry("/source/com/bli/bla/blu/saaaaaoooooooooooaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa/", "test.fil");
    assertEquals("/source/com/bli/bla/blu/saaaaaoooooooooooaaaaaaaaaaaaaaaaa.../test.fil", entry.getShortenedFullEntryName());
    assertEquals(DirEntry.FULL_ENTRY_NAME_MAX_LENGTH, entry.getShortenedFullEntryName().length());

    entry = toEntry("/source/com/bli/bla/blu/saaaaa/bbbbbb/ccccccccc/dddddddd/aaaaaaaaaaaaaaaaaa/", "reallylongfilenamejustfortestingpurposes.fil");
    assertEquals("/source/com/bli/bla/bl.../reallylongfilenamejustfortestingpurposes.fil", entry.getShortenedFullEntryName());
    assertEquals(DirEntry.FULL_ENTRY_NAME_MAX_LENGTH, entry.getShortenedFullEntryName().length());

    entry = toEntry("/source/com/bli/bla/blu/", "test.fil");
    assertEquals("/source/com/bli/bla/blu/test.fil", entry.getShortenedFullEntryName());

    entry = toEntry("/source/com/bli/bla/blu/", "thisisafilenamewithmorethanenoughcharactersinitreallynotperfectlookingontheweb.fil");
    assertEquals(".../thisisafilenamewithmorethanenoughcharactersinitreallynotperfectlookingontheweb.fil", entry.getShortenedFullEntryName());
    assertEquals(DirEntry.FULL_ENTRY_NAME_MAX_LENGTH + 16, entry.getShortenedFullEntryName().length());

    entry = toEntry("/source/com/bli/bla/blu/saaaaa/bbbbbb/ccccccccc/dddddddd/aaaaaaaaaaaaaaaaaa/", "reallylongfilenamejustfortestingpurposesonly.fil");
    assertEquals("/source/com/bli/bl.../reallylongfilenamejustfortestingpurposesonly.fil", entry.getShortenedFullEntryName());
    assertEquals(DirEntry.FULL_ENTRY_NAME_MAX_LENGTH, entry.getShortenedFullEntryName().length());

    final String path = "/abcde/fghijk/lmnopqrs/";

    assertEquals("/abcde/fghijk/lmnopqrs/abc", toEntry(path, "abc").getShortenedFullEntryName(26));
    assertEquals("/abcde/fghijk/lmno.../abc", toEntry(path, "abc").getShortenedFullEntryName(25));
    assertEquals("/abcde/fghijk/lmn.../abc", toEntry(path, "abc").getShortenedFullEntryName(24));
    assertEquals("/abcde/fghijk/lm.../abc", toEntry(path, "abc").getShortenedFullEntryName(23));
    assertEquals("/abcde/fghijk/l.../abc", toEntry(path, "abc").getShortenedFullEntryName(22));
    assertEquals("/abcde/fghijk/.../abc", toEntry(path, "abc").getShortenedFullEntryName(21));
    assertEquals("/abcde/fghijk.../abc", toEntry(path, "abc").getShortenedFullEntryName(20));
    assertEquals("/abcde/fghij.../abc", toEntry(path, "abc").getShortenedFullEntryName(19));
    assertEquals("/abcde/fghi.../abc", toEntry(path, "abc").getShortenedFullEntryName(18));
    assertEquals("/abcde/fgh.../abc", toEntry(path, "abc").getShortenedFullEntryName(17));
    assertEquals("/abcde/fg.../abc", toEntry(path, "abc").getShortenedFullEntryName(16));
    assertEquals("/abcde/f.../abc", toEntry(path, "abc").getShortenedFullEntryName(15));
    assertEquals("/abcde/.../abc", toEntry(path, "abc").getShortenedFullEntryName(14));
    assertEquals("/abcde.../abc", toEntry(path, "abc").getShortenedFullEntryName(13));
    assertEquals("/abcd.../abc", toEntry(path, "abc").getShortenedFullEntryName(12));
    assertEquals("/abc.../abc", toEntry(path, "abc").getShortenedFullEntryName(11));
    assertEquals("/ab.../abc", toEntry(path, "abc").getShortenedFullEntryName(10));
    assertEquals("/a.../abc", toEntry(path, "abc").getShortenedFullEntryName(9));
    assertEquals("/.../abc", toEntry(path, "abc").getShortenedFullEntryName(8));
    assertEquals(".../abc", toEntry(path, "abc").getShortenedFullEntryName(7));
    assertEquals(".../abc", toEntry(path, "abc").getShortenedFullEntryName(0));
  }

  @Test
  public void testGetCamelCasePattern() throws Exception {
    final DirEntry entry = new DirEntry("/trunk/", "ATestFile.java", "A", new Date(), DirEntry.Kind.FILE, 1, 1);
    assertEquals("ATF", entry.getCamelCasePattern().getPattern());
  }

  @Test
  public void testGetId() throws Exception {
    final DirEntry dirEntry = new DirEntry("/trunk/", "ATestFile.java", "A", new Date(), DirEntry.Kind.DIR, 1, 1);
    final DirEntry fileEntry = new DirEntry("/trunk/", "ATestFile.java", "A", new Date(), DirEntry.Kind.FILE, 1, 1);
    assertEquals("/trunk/ATestFile.javaDIR", dirEntry.getId());
    assertEquals("/trunk/ATestFile.javaFILE", fileEntry.getId());
  }

  @Test
  public void testGetNameFragments() throws Exception {
    final DirEntry entry = new DirEntry("/trunk/", "ATestFile.java", "A", new Date(), DirEntry.Kind.FILE, 1, 1);
    assertEquals("[ATest, File, java]", entry.getNameFragments().toString());
  }

  private DirEntry toEntry(final String path, final String filename) {
    return new DirEntry(path, filename, "A", new Date(), DirEntry.Kind.FILE, 1, 1);
  }

}
