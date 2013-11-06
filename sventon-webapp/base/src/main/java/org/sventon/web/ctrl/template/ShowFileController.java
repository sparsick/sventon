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
package org.sventon.web.ctrl.template;

import org.apache.commons.io.FilenameUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.sventon.Colorer;
import org.sventon.SVNConnection;
import org.sventon.model.*;
import org.sventon.util.EncodingUtils;
import org.sventon.util.WebUtils;
import org.sventon.web.UserRepositoryContext;
import org.sventon.web.command.BaseCommand;

import javax.activation.FileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ShowFileController.
 *
 * @author patrik@sventon.org
 * @author jesper@sventon.org
 */
public final class ShowFileController extends AbstractTemplateController {

  /**
   * The colorer instance.
   */
  private Colorer colorer;

  /**
   * The mime/file type map.
   */
  private final FileTypeMap mimeFileTypeMap;

  /**
   * Regex pattern that identifies text file extensions.
   */
  private String textFileExtensionPattern;

  /**
   * Regex pattern that identifies binary file extensions.
   */
  private String binaryFileExtensionPattern;

  /**
   * Regex pattern that identifies archive file extensions.
   */
  private String archiveFileExtensionPattern;

  /**
   * FORMAT_REQUEST_PARAMETER = format.
   */
  private static final String FORMAT_REQUEST_PARAMETER = "format";

  /**
   * Request parameter identifying the arcived entry to display.
   */
  private static final String ARCHIVED_ENTRY = "archivedEntry";

  /**
   * Request parameter controlling if archived entry should be displayed
   * independently of it's mime-type.
   */
  private static final String FORCE_ARCHIVED_ENTRY_DISPLAY = "forceDisplay";

  /**
   * Request parameter indicating display should be done in a raw, unprocessed format.
   */
  private static final String RAW_DISPLAY_FORMAT = "raw";

  /**
   * Constructor.
   *
   * @param mimeFileTypeMap Mime file type map.
   */
  public ShowFileController(final FileTypeMap mimeFileTypeMap) {
    this.mimeFileTypeMap = mimeFileTypeMap;
  }

  @Override
  protected ModelAndView svnHandle(final SVNConnection connection, final BaseCommand command,
                                   final long headRevision, final UserRepositoryContext userRepositoryContext,
                                   final HttpServletRequest request, final HttpServletResponse response,
                                   final BindException exception) throws Exception {

    logger.debug("Assembling file contents for: " + command);

    final String formatParameter = ServletRequestUtils.getStringParameter(request, FORMAT_REQUEST_PARAMETER, null);
    final String archivedEntry = ServletRequestUtils.getStringParameter(request, ARCHIVED_ENTRY, null);
    final boolean forceDisplay = ServletRequestUtils.getBooleanParameter(request, FORCE_ARCHIVED_ENTRY_DISPLAY, false);
    final Map<String, Object> model = new HashMap<String, Object>();
    final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    final Properties fileProperties = getRepositoryService().listProperties(
        connection, command.getPath(), command.getRevisionNumber());

    final String charset = userRepositoryContext.getCharset();
    logger.debug("Using charset encoding: " + charset);

    model.put("properties", fileProperties);
    final ModelAndView modelAndView;

    if (isImageFileExtension(command)) {
      logger.debug("File identified as an image file");
      modelAndView = new ModelAndView("showImageFile", model);
    } else if (isArchiveFileExtension(command)) {
      if (archivedEntry == null) {
        logger.debug("File identified as an archive file");
        getRepositoryService().getFileContents(connection, command.getPath(), command.getRevisionNumber(), outStream);
        final ArchiveFile archiveFile = new ArchiveFile(outStream.toByteArray());
        model.put("entries", archiveFile.getEntries());
        modelAndView = new ModelAndView("showArchiveFile", model);
      } else {
        logger.debug("Archived entry: " + archivedEntry);
        model.put("archivedEntry", archivedEntry);
        final String contentType = mimeFileTypeMap.getContentType(archivedEntry);
        logger.debug("Detected content-type: " + contentType);

        if (contentType != null && contentType.startsWith("text") || forceDisplay) {
          getRepositoryService().getFileContents(connection, command.getPath(), command.getRevisionNumber(), outStream);
          logger.debug("Extracting [" + archivedEntry + "] from archive [" + command.getPath() + "]");
          final ZipFileWrapper zipFileWrapper = new ZipFileWrapper(outStream.toByteArray());
          final TextFile textFile = new TextFile(new String(zipFileWrapper.extractFile(archivedEntry), charset),
              archivedEntry, charset, colorer);
          model.put("file", textFile);
          modelAndView = new ModelAndView("showTextFile", model);
        } else {
          modelAndView = new ModelAndView("showBinaryFile", model);
        }
      }
    } else if (isBinaryFileExtension(command)) {
      logger.debug("File identified as a binary file");
      modelAndView = new ModelAndView("showBinaryFile", model);
    } else if (isTextFileExtension(command) || isTextMimeType(fileProperties)) {
      getRepositoryService().getFileContents(connection, command.getPath(), command.getRevisionNumber(), outStream);

      if (RAW_DISPLAY_FORMAT.equals(formatParameter)) {
        final String content = outStream.toString(charset);
        response.setHeader(WebUtils.CONTENT_DISPOSITION_HEADER, "inline; filename=\"" +
            EncodingUtils.encodeFilename(command.getTarget(), request.getHeader("USER-AGENT")) + "\"");
        response.setContentType(WebUtils.CONTENT_TYPE_TEXT_PLAIN);
        response.getOutputStream().write(content.getBytes(charset));
        return null;
      } else {
        final TextFile textFile = new TextFile(outStream.toString(charset), command.getPath(), charset, colorer);
        model.put("file", textFile);
      }
      modelAndView = new ModelAndView("showTextFile", model);
    } else {
      logger.debug("File unidentified - showing as binary");
      modelAndView = new ModelAndView("showBinaryFile", model);
    }
    return modelAndView;
  }

  /**
   * Checks if given map of svn properties contains a text file mime type.
   *
   * @param properties The svn properties for given file.
   * @return True if text file, false if not.
   */
  protected boolean isTextMimeType(final Properties properties) {
    final String mimeType = properties.getStringValue(Property.MIME_TYPE);
    return (mimeType == null || mimeType.startsWith("text/"));
  }

  /**
   * Checks if given file name indicates a text file.
   *
   * @param command Command
   * @return True if text file, false if not.
   */
  boolean isTextFileExtension(final BaseCommand command) {
    return FilenameUtils.getExtension(command.getPath()).toLowerCase().matches(textFileExtensionPattern);
  }

  /**
   * Checks if given file name indicates a binary file.
   *
   * @param command Command
   * @return True if binary file, false if not.
   */
  boolean isBinaryFileExtension(final BaseCommand command) {
    return FilenameUtils.getExtension(command.getPath()).toLowerCase().matches(binaryFileExtensionPattern);
  }

  /**
   * Checks if given file name indicates an archive file.
   *
   * @param command Command
   * @return True if archive file, false if not.
   */
  protected boolean isArchiveFileExtension(final BaseCommand command) {
    return FilenameUtils.getExtension(command.getPath()).toLowerCase().matches(archiveFileExtensionPattern);
  }

  /**
   * Checks if given file name indicates an image file.
   *
   * @param command Command
   * @return True if image file, false if not.
   */
  protected boolean isImageFileExtension(final BaseCommand command) {
    return mimeFileTypeMap.getContentType(command.getPath()).startsWith("image");
  }

  /**
   * Sets the <tt>Colorer</tt> instance.
   *
   * @param colorer The instance.
   */
  public void setColorer(final Colorer colorer) {
    this.colorer = colorer;
  }

  /**
   * Sets the text file extension pattern.
   *
   * @param fileExtensionPattern The pattern
   */
  public void setTextFileExtensionPattern(final String fileExtensionPattern) {
    textFileExtensionPattern = fileExtensionPattern;
  }

  /**
   * Sets the binary file extension pattern.
   *
   * @param fileExtensionPattern The pattern
   */
  public void setBinaryFileExtensionPattern(final String fileExtensionPattern) {
    binaryFileExtensionPattern = fileExtensionPattern;
  }

  /**
   * Sets the archive file extension pattern.
   *
   * @param fileExtensionPattern The pattern
   */
  public void setArchiveFileExtensionPattern(final String fileExtensionPattern) {
    archiveFileExtensionPattern = fileExtensionPattern;
  }

}
