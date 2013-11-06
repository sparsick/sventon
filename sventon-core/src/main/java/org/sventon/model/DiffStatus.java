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

import java.io.Serializable;

/**
 * Represents the diff status for a single diffed entry.
 */
public class DiffStatus implements Serializable {

  private static final long serialVersionUID = -1064565009806896861L;

  private final ChangeType modificationType;

  private final String path;

  private final boolean propertyModified;

  /**
   * Constructor.
   *
   * @param modificationType Type of change
   * @param path             Relative path to entry
   * @param propertyModified True to indicate that properties were also changed.
   */
  public DiffStatus(ChangeType modificationType, String path, boolean propertyModified) {
    this.modificationType = modificationType;
    this.path = path;
    this.propertyModified = propertyModified;
  }

  /**
   * Returns the type of modification for the current
   * item.
   *
   * @return a path change type
   */
  public ChangeType getModificationType() {
    return modificationType;
  }

  /**
   * Returns a relative path of the item.
   * Set for Working Copy items and relative to the anchor of diff status operation.
   *
   * @return item path
   */
  public String getPath() {
    return path;
  }

  /**
   * Says whether properties of the Working Copy item are modified.
   *
   * @return <span class="javakeyword">true</span> if properties were modified
   *         in a particular revision, <span class="javakeyword">false</span>
   *         otherwise
   */
  public boolean isPropertyModified() {
    return propertyModified;
  }

  @Override
  public String toString() {
    return modificationType.getCode() + " " + path + ", propertyModified=" + propertyModified;
  }
}
