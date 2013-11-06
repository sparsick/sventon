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

import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Represents a repository name.
 *
 * @author jesper@sventon.org
 */
public final class RepositoryName implements Serializable, Comparable {

  private static final long serialVersionUID = 5044457892450351810L;

  /**
   * Name of the repository.
   */
  private final String name;

  /**
   * Constructor.
   *
   * @param name Repository name.
   */
  public RepositoryName(final String name) {
    if (isValid(name)) {
      this.name = name;
    } else {
      throw new IllegalArgumentException("Illegal name: " + name);
    }

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RepositoryName)) return false;
    final RepositoryName that = (RepositoryName) o;
    return !(name != null ? !name.equals(that.name) : that.name != null);
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  /**
   * Validates given repository name.
   *
   * @param name Repository name to validate.
   * @return True if valid, false if not.
   */
  public static boolean isValid(final String name) {
    return !(name == null || name.length() == 0) && !StringUtils.containsWhitespace(name);
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public int compareTo(Object o) {
    return name.compareTo(o.toString());
  }

}
