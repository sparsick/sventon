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

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Class for scaling images.
 *
 * @author jesper@sventon.org
 */
public class ImageScaler {

  /**
   * Gets a thumbnail version of the buffered image.
   *
   * @param image   The image to scale.
   * @param maxSize Maximum size (height and width)
   * @return The thumbnail.
   */
  public BufferedImage getThumbnail(final BufferedImage image, final int maxSize) {
    final Dimension thumbnailSize = getThumbnailSize(image.getWidth(), image.getHeight(), maxSize);

    // Re-size image.
    final Image rescaled = image.getScaledInstance((int) thumbnailSize.getWidth(),
        (int) thumbnailSize.getHeight(), Image.SCALE_AREA_AVERAGING);

    return toBufferedImage(rescaled, BufferedImage.TYPE_INT_ARGB);
  }

  /**
   * Gets the preferred thumbnail dimension for given <code>width</code>
   * and <code>height</code>.
   *
   * @param width   The width.
   * @param height  The height.
   * @param maxSize Maximum size (height and width)
   * @return The thumbnail dimension.
   */
  protected Dimension getThumbnailSize(final int width, final int height, final int maxSize) {
    final int max = Math.max(width, height);
    if (max <= maxSize) {
      // Image is smaller than maximum size - no need for a re-size
      return new Dimension(width, height);
    } else {
      final double scaleFactor = (double) max / maxSize;
      return new Dimension(((int) (width / scaleFactor)), ((int) (height / scaleFactor)));
    }
  }

  /**
   * Converts an <code>Image</code> instance into a <code>BufferedImage</code> instance.
   *
   * @param image The <code>Image</code> instance.
   * @param type  Type of the created image.
   * @return The buffered image.
   * @see java.awt.image.BufferedImage
   */
  private BufferedImage toBufferedImage(final Image image, final int type) {
    final int width = image.getWidth(null);
    final int height = image.getHeight(null);

    final BufferedImage result = new BufferedImage(width, height, type);
    final Graphics2D g = result.createGraphics();
    g.drawImage(image, 0, 0, null);
    g.dispose();
    return result;
  }

}