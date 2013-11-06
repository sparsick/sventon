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
package org.sventon.export;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

import static org.sventon.export.DefaultExportDirectory.DATE_FORMAT_PATTERN;
import static org.sventon.export.DefaultExportDirectory.FILE_NAME_PATTERN;

/**
 * File expiration rule.
 *
 * @author jesper@sventon.org
 */
public final class ExportFileExpirationRule implements ExpirationRule {

  /**
   * The logging instance.
   */
  private final Log logger = LogFactory.getLog(getClass());

  /**
   * Temporary file expire time.
   */
  private final long temporaryFileExpireTimeMs;

  /**
   * Constructor.
   *
   * @param temporaryFileExpireTimeMs Expire time in milliseconds.
   */
  public ExportFileExpirationRule(final long temporaryFileExpireTimeMs) {
    this.temporaryFileExpireTimeMs = temporaryFileExpireTimeMs;
  }

  /**
   * Returns true if this file has expired and should be deleted.
   *
   * @param tempFile Temporary file
   * @return True if file is old enough, according to the threshold value.
   */
  public boolean hasExpired(final File tempFile) {
    final Matcher matcher = FILE_NAME_PATTERN.matcher(tempFile.getName());
    matcher.find();
    try {
      final String dateInMillis = matcher.group(2);
      final Date fileDate = new SimpleDateFormat(DATE_FORMAT_PATTERN).parse(dateInMillis);
      return System.currentTimeMillis() - fileDate.getTime() > temporaryFileExpireTimeMs;
    } catch (final ParseException pe) {
      logger.warn("Unable to parse date part of filename: " + tempFile.getName(), pe);
      return false;
    }
  }
}