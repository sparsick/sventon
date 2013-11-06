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

import org.apache.commons.lang.Validate;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

/**
 * <code>java.util.Comparator&lt;T&gt;</code> implementation to support
 * ordering of <code>DirEntry</code> objects.
 * <p/>
 * The comparator can be configured during construction to tweak sorting behavior.
 *
 * @author patrik@sventon.org
 * @author jesper@sventon.org
 */
public final class DirEntryComparator implements Comparator<DirEntry>, Serializable {

  private static final long serialVersionUID = -823291078109887289L;

  public enum SortType {
    NAME, AUTHOR, REVISION, DATE, FULL_NAME, SIZE
  }

  private boolean groupDirs = false;

  private final SortType sortType;

  /**
   * Create a new comparator for comparing <code>DirEntry</code> objects.
   *
   * @param sortType  Entry type property to perform the comparisons on. See enum constants
   *                  defined in this class.
   * @param groupDirs <code>true</code> to group directories, this will sort an entry
   *                  of kind <code>SVNNodeKind.DIR</code> before an entries of other
   *                  kinds.
   */
  public DirEntryComparator(final SortType sortType, final boolean groupDirs) {
    Validate.notNull(sortType, "sortType cannot be null");
    this.groupDirs = groupDirs;
    this.sortType = sortType;
  }

  @Override
  public int compare(final DirEntry entry1, final DirEntry entry2) {
    if (groupDirs) {
      final DirEntry.Kind kind1 = entry1.getKind();
      final DirEntry.Kind kind2 = entry2.getKind();
      if (!kind1.equals(kind2)) {
        // Not equal kinds, have to inspect.
        if (DirEntry.Kind.DIR == kind1) {
          return -1;
        } else if (DirEntry.Kind.DIR == kind2) {
          return 1;
        }
      } // not equal kind, but neither is DIR
    }

    final String entryName1 = entry1.getName();
    final String entryName2 = entry2.getName();

    // Natural ordering of strings as used below may not always be desirable?
    switch (sortType) {
      case NAME:
        final int nameCompare = nullSafeCompare(entryName1, entryName2);
        if (nameCompare == 0) {
          final long revision1 = entry1.getRevision();
          final long revision2 = entry2.getRevision();
          return revision1 == revision2 ? 0 : revision1 < revision2 ? -1 : 1;
        } else {
          return nameCompare;
        }
      case AUTHOR:
        final String author1 = entry1.getAuthor();
        final String author2 = entry2.getAuthor();
        final int authCompare = nullSafeCompare(author1, author2);
        return authCompare == 0 ? nullSafeCompare(entryName1, entryName2) : authCompare;
      case REVISION:
        final long revision1 = entry1.getRevision();
        final long revision2 = entry2.getRevision();
        if (revision1 == revision2) {
          return nullSafeCompare(entryName1, entryName2);
        } else {
          return revision1 < revision2 ? -1 : 1;
        }
      case SIZE:
        final long size1 = entry1.getSize();
        final long size2 = entry2.getSize();
        if (size1 == size2) {
          return nullSafeCompare(entryName1, entryName2);
        } else {
          return size1 < size2 ? -1 : 1;
        }
      case DATE:
        final Date date1 = entry1.getDate();
        final Date date2 = entry2.getDate();
        final int dateCompare = nullSafeCompare(date1, date2);
        return dateCompare == 0 ? nullSafeCompare(entryName1, entryName2) : dateCompare;
      case FULL_NAME:
        return nullSafeCompare(entry1.getFullEntryName(), entry2.getFullEntryName());
      default:
        throw new IllegalStateException("Illegal sort type: " + sortType);
    }
  }

  private <T> int nullSafeCompare(final Comparable<T> o1, final T o2) {
    if (o1 != null && o2 != null) {
      return o1.compareTo(o2);
    } else if (o1 == null && o2 == null) {
      return 0;
    } else {
      return o1 == null ? -1 : 1;
    }

  }
}
