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

import org.sventon.model.LogMessageSearchItem;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * <code>java.util.Comparator&lt;T&gt;</code> implementation to support
 * ordering of <code>LogMessage</code> objects.
 * <p/>
 * The comparator can be configured during construction to tweak sorting behavior.
 *
 * @author jesper@sventon.org
 */
public final class LogMessageComparator implements Comparator<LogMessageSearchItem>, Serializable {

  private static final long serialVersionUID = -123291078109887289L;

  /**
   * Constant for comparing by ascending revision numbers.
   */
  public static final int ASCENDING = 0;

  /**
   * Constant for comparing by descending revision numbers.
   */
  public static final int DESCENDING = 1;

  private int sortType = 0;

  private static final Set<Integer> legalTypes = new HashSet<Integer>();

  static {
    legalTypes.add(ASCENDING);
    legalTypes.add(DESCENDING);
  }

  /**
   * Create a new comparator for comparing <code>LogMessage</code> objects.
   *
   * @param sortType See constants defined in this class.
   */
  public LogMessageComparator(final int sortType) {
    if (!legalTypes.contains(sortType)) {
      throw new IllegalArgumentException("Not a valid sort type: " + sortType);
    }
    this.sortType = sortType;
  }

  @Override
  public int compare(final LogMessageSearchItem message1, final LogMessageSearchItem message2) {
    final long revision1 = message1.getRevision();
    final long revision2 = message2.getRevision();
    if (sortType == ASCENDING) {
      return revision1 < revision2 ? -1 : 1;
    } else {
      return revision2 < revision1 ? -1 : 1;
    }
  }
}
