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

import org.sventon.SVNConnection;
import org.sventon.web.command.MultipleEntriesCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * ExportExecutor.
 *
 * @author jesper@sventon.org
 */
public interface ExportExecutor {

  /**
   * Creates an export directory.
   *
   * @param command     Command
   * @param connection  Repository
   * @param pegRevision Peg revision
   * @return Directory instance.
   */
  UUID submit(final MultipleEntriesCommand command, final SVNConnection connection, long pegRevision);

  /**
   * Downloads the compressed export file.
   *
   * @param uuid     Compressed file UUID
   * @param request  Request
   * @param response Response
   * @throws IOException if an IO error occur.
   */
  void downloadByUUID(final UUID uuid, final HttpServletRequest request, final HttpServletResponse response)
      throws IOException;

  /**
   * Gets the current export progress for given UUID.
   *
   * @param uuid Export UUID
   * @return Progress (0..100)
   */
  int getProgress(final UUID uuid);

  /**
   * Deletes the export file given an UUID.
   *
   * @param uuid UUID of export file to delete.
   */
  void delete(final UUID uuid);
}
