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
package org.sventon.web.command;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.sventon.diff.DiffException;
import org.sventon.model.DiffStyle;
import org.sventon.model.PathRevision;
import org.sventon.model.Revision;
import org.sventon.util.EncodingUtils;
import org.sventon.util.PathUtil;

import java.util.Arrays;

/**
 * DiffCommand.
 * <p/>
 * Command class used to parse and bundle diffing from/to details.
 * <p/>
 * A diff can be made between two arbitrary entries or by a single entry and
 * it's previous revision. In the first case, the method
 * {@link #setEntries(PathRevision[])} setEntries} will be used.
 * A diff with previous revision will use the main path set by calling {@link #setPath(String)} }
 *
 * @author jesper@sventon.org
 */
public final class DiffCommand extends BaseCommand {

  /**
   * From revision.
   */
  private PathRevision fromFileRevision;

  /**
   * To revision.
   */
  private PathRevision toFileRevision;

  /**
   * The requested diff style.
   */
  private DiffStyle style = DiffStyle.unspecified;

  /**
   * Sets the requested diff style.
   *
   * @param style Style
   */
  public void setStyle(final DiffStyle style) {
    Validate.notNull(style);
    this.style = style;
  }

  /**
   * Used when diffing two arbitrary entries.
   *
   * @param entries Array containing two <code>PathRevision</code> objects.
   * @throws IllegalArgumentException       if given list does not contain two entries.
   * @throws org.sventon.diff.DiffException if entry does not have a history.
   */
  public void setEntries(final PathRevision[] entries) {
    Validate.notNull(entries);

    if (entries.length < 2) {
      throw new DiffException("The entry does not have a history.");
    }

    Arrays.sort(entries);
    toFileRevision = entries[0];
    fromFileRevision = entries[1];
  }

  public void setFrom(final PathRevision pathRevision) {
    fromFileRevision = pathRevision;

  }

  public void setTo(final PathRevision pathRevision) {
    toFileRevision = pathRevision;
  }

  /**
   * @return True if entries has been set (using {@link #setEntries(PathRevision[])}).
   */
  public boolean hasEntries() {
    return toFileRevision != null && fromFileRevision != null;
  }

  /**
   * Gets the requested diff style.
   *
   * @return Style
   */
  public DiffStyle getStyle() {
    return style;
  }

  /**
   * Gets the diff <i>from</i> path.
   *
   * @return The path.
   */
  public String getFromPath() {
    return fromFileRevision != null ? fromFileRevision.getPath() : "";
  }

  /**
   * Gets the encoded <i>from</i> target.
   *
   * @return From target, i.e. file name without path.
   */
  public String getFromTarget() {
    return EncodingUtils.encodeUrl(PathUtil.getTarget(getFromPath()));
  }

  /**
   * Gets the diff <i>from</i> revision.
   *
   * @return The revision.
   */
  public Revision getFromRevision() {
    return fromFileRevision != null ? fromFileRevision.getRevision() : Revision.UNDEFINED;
  }

  /**
   * Gets the diff <i>from</i> entry.
   *
   * @return The entry.
   */
  public PathRevision getFrom() {
    return fromFileRevision;
  }

  /**
   * Gets the diff <i>to</i> path.
   *
   * @return The path.
   */
  public String getToPath() {
    return toFileRevision != null ? toFileRevision.getPath() : "";
  }

  /**
   * Gets the diff <i>to</i> entry.
   *
   * @return The entry.
   */
  public PathRevision getTo() {
    return toFileRevision;
  }

  /**
   * Gets the encoded <i>to</i> target.
   *
   * @return To target, i.e. file name without path.
   */
  public String getToTarget() {
    return EncodingUtils.encodeUrl(PathUtil.getTarget(getToPath()));
  }

  /**
   * Gets the diff <i>to</i> revision.
   *
   * @return The revision.
   */
  public Revision getToRevision() {
    return toFileRevision != null ? toFileRevision.getRevision() : Revision.UNDEFINED;
  }

  @Override
  public boolean equals(final Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("DiffCommand {");
    if (hasEntries()) {
      sb.append("from: ");
      sb.append(getFromPath());
      sb.append("@");
      sb.append(getFromRevision());
      sb.append(", to: ");
      sb.append(getToPath());
      sb.append("@");
      sb.append(getToRevision());
    } else {
      sb.append("from: ");
      sb.append(getPath());
      sb.append("@");
      sb.append(getRevision());
      sb.append(", to: ");
      sb.append(getPath());
      sb.append("@");
      sb.append(getRevision());
      sb.append("-1");
    }
    sb.append(", style: ");
    sb.append(getStyle());
    sb.append(", revision: ");
    sb.append(getRevision());
    sb.append(", pegRevision: ");
    sb.append(getPegRevision());
    sb.append("}");
    return sb.toString();
  }

}
